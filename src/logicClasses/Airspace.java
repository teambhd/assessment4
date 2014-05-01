package logicClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class Airspace {

    private static Image backgroundImage;

    public static void init() throws SlickException {
        if (backgroundImage == null) {
            LoadingList.get().add(new DeferredFile("res/graphics/background.png") {
                public void loadFile(String filename) throws SlickException {
                    backgroundImage = new Image(filename);
                }
            });
        }
    }

    // Random number generator
    private static Random rand = new Random();

    // Constants
    private static final int MAX_FLIGHTS = 10;
    private static final int DIFFICULTY_INCREASE_INTERVAL = 900; // 15 seconds
    private static final int INITIAL_CHANCE_OF_NEW_FLIGHT = 400;
    private static final int HANDOVER_DELAY = 300; //5 seconds


    // Fields
    private int chanceOfNewFlight = INITIAL_CHANCE_OF_NEW_FLIGHT;

    private int numberOfGameLoops = 0;
    private int numberOfGameLoopsSinceLastFlightAdded = 0;
    private int numberOfGameLoopsWhenDifficultyIncreases = DIFFICULTY_INCREASE_INTERVAL;
    private int numberOfGameLoopsSinceHandover = 0;

    private List<Flight> listOfFlightsInAirspace = new ArrayList<Flight>();
    private List<Waypoint> listOfWayppoints = new ArrayList<Waypoint>();
    private List<EntryPoint> listOfEntryPoints = new ArrayList<EntryPoint>();
    private List<ExitPoint> listOfExitPoints = new ArrayList<ExitPoint>();
    private List<Airport> listOfAirports = new ArrayList<Airport>();

    // score isn't actually used in multiplayer, and vice versa, but there's no harm in just doing this the simple way
    private ScoreTracking score = new ScoreTracking();
    private ScoreTracking redScore = new ScoreTracking();
    private ScoreTracking blueScore = new ScoreTracking();

    private SeparationRules separationRules;
    private int difficultyValueOfGame;
    private boolean isMultiplayer, handoverDelay;


    // Constructor
    public Airspace(boolean multiplayer) {
        this.isMultiplayer = multiplayer;        
    }
    
    
    // STATIC METHODS

    public static double distanceBetween(Point a, Point b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }

    public static double distanceBetween(Flight a, Flight b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }
    
    public static double normalizeAngle(double angle) {
        // The first % normalises to between -359 and 359, 
        // the + 360 moves that to between 1 and 719 
        // and the final % 360 brings it between 0 and 360
        return ((angle % 360) + 360) % 360;
    }    


    // METHODS

    /**
     * resetAirspace: Reset all of the attributes in airspace back to default
     */

    public void resetAirspace() {
        this.listOfFlightsInAirspace = new ArrayList<Flight>();

        this.numberOfGameLoopsSinceLastFlightAdded = 0;
        this.numberOfGameLoops = 0;
        this.numberOfGameLoopsWhenDifficultyIncreases = DIFFICULTY_INCREASE_INTERVAL;
        
        this.chanceOfNewFlight = INITIAL_CHANCE_OF_NEW_FLIGHT;

        this.separationRules.setGameOverViolation(false); // Prevents an immediate game over on replay

        // Reset all the score variables
        this.score = new ScoreTracking();
        this.redScore = new ScoreTracking();
        this.blueScore = new ScoreTracking();
    }

    /**
     * createAndSetSeperationRules: Create and set the separation rules for the airpsace based on the difficulty value of the game
     */

    public void createAndSetSeparationRules() {
        this.separationRules = new SeparationRules(difficultyValueOfGame);
    }

    /**
     * newWaypoint: Add a new waypoint to the list of waypoints in the airspace
     * @param x The x coordinate of the waypoint
     * @param y The y coordinate of the waypoint
     * @param name The name used to reference the waypoint
     */

    public boolean newWaypoint(int x, int y, String name)  {
        if (x < 1250 && x > 0 && y < 650 && y > -50) {
            // x and y must be within these bounds to be within screen space
            Waypoint tmpWp = new Waypoint(x, y, name);

            if (this.addWaypoint(tmpWp)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * newAirport: Add a new airport to the list of airports in the airspace
     * @param x The x coordinate of the airport
     * @param y The y coordinate of the airport
     * @param runwayHeading The direction in which the airport runway should face
     * @param name The name used to reference the airport
     */
        
    public void newAirport(int x, int y, int runwayHeading, String name) {
        // Generate and add the airport object itself
        listOfAirports.add(new Airport(x, y, runwayHeading, name));
        
        // Generate and add the airport exit point, notionally in the centre of the runway
        addExitPoint(new ExitPoint(x, y, name));
        
        // Generate and add the airport entry point, on the edge (rather than the centre) of the runway image, 
        // so the planes take off in the direction of the runway heading
        int airportEntryX = x - (int)(140 * Math.sin(Math.toRadians(runwayHeading)));
        int airportEntryY = y + (int)(140 * Math.cos(Math.toRadians(runwayHeading)));
        addEntryPoint(new EntryPoint(airportEntryX, airportEntryY));
    }

    /**
     * newExitPoint: Add a new exitpoint to the list in the airspace
     * @param x The x coordinate of the exitpoint
     * @param y The y coordinate of the exitpoint
     * @param name The name used to reference the exitpoint
     */

    public boolean newExitPoint(int x, int y, String name) {
        if (x < 1250 && x > -50 && y < 650 && y > -50) { // x and y must be within these bounds to be within screen space
            if (this.addExitPoint(new ExitPoint(x, y, name))) {
                return true;
            }
        }

        return false;
    }

    /**
     * newEntryPoint: Add a new entrypoint to the the list in the airspace
     * @param x The x coordinate of the entry point
     * @param y The y coordinate of the entry point
     */

    public boolean newEntryPoint(int x, int y)  {
        if (x < 1250 && x > -50 && y < 650 && y > -50) { // x and y must be within these bounds to be within screen space
            if (this.addEntryPoint(new EntryPoint(x, y))) {
                return true;
            }
        }

        return false;
    }

    /**
     * newFlight: Add a new flight to the list of flights in the airspace if it has been long enough since the 
     * last flight was added and if random number satisfies condition
     * @throws SlickException
     */

    public boolean newFlight() throws SlickException {
        if (listOfFlightsInAirspace.size() < MAX_FLIGHTS && 
            (numberOfGameLoopsSinceLastFlightAdded >= 300 || listOfFlightsInAirspace.isEmpty())) {
            
            /*
             * The random number is generated in the range 0 to 100 if the airspace is empty, as this increases
             * the likelihood of a value of 1 being returned, and therefore a flight being generated; 
             * this stops the user having to potentially wait a long period of time for a flight to be generated.
             * If the airspace is not empty, the random number generated is in the range 0 to chanceOfNewFlight.
             */

            int checkNumber;

            if (numberOfGameLoopsSinceLastFlightAdded >= 1200 || listOfFlightsInAirspace.isEmpty()) {
                checkNumber = rand.nextInt(Math.min(100, chanceOfNewFlight));
            }

            else {
                checkNumber = rand.nextInt(chanceOfNewFlight);
            }
            
            if (checkNumber == 1) {                
                Flight tempFlight = new Flight(this);

                if (!isMultiplayer) {
                    tempFlight.setOwner("single");
                }

                else if (rand.nextBoolean()) {
                    tempFlight.setOwner("red");
                }

                else {
                    tempFlight.setOwner("blue");
                }

                double heading;

                if (tempFlight.getFlightPlan().getEntryPoint().isRunway()) {
                    if (tempFlight.getX() == listOfAirports.get(0).getX()) {
                        heading = listOfAirports.get(0).getRunwayHeading();
                    }
                    else {
                        heading = listOfAirports.get(1).getRunwayHeading();
                    }
                }

                else {
                    heading = tempFlight.calculateHeadingToFirstWaypoint(
                                  tempFlight.getFlightPlan().getPointByIndex(0).getX() ,
                                  tempFlight.getFlightPlan().getPointByIndex(0).getY());
                }

                tempFlight.setTargetHeading(heading);
                tempFlight.setCurrentHeading(heading);

                if (addFlight(tempFlight)) {
                    numberOfGameLoopsSinceLastFlightAdded = 0;
                    return true;
                }
                
            }
        }

        return false;
    }

    /**
     * checkIfFlightHasLeftAirspace: Checks is a flight has left the airspace, so it can be removed.
     * @param flight The flight to be checked
     */

    public boolean checkIfFlightHasLeftAirspace(Flight flight) {
        // x and y must be within these bounds to be within screen space
        return flight.getX() > 1250 || flight.getX() < -50 || flight.getY() > 650 || flight.getY() < -50;
    }

    /**
     * increaseDifficulty: Increases the chance of a new flight spawning, and also awards a points bonus. 
     * Called by update every DIFFICULTY_INCREASE_INTERVAL ms.
     */

    public void increaseDifficulty() {
        numberOfGameLoopsWhenDifficultyIncreases += DIFFICULTY_INCREASE_INTERVAL;

        if (chanceOfNewFlight - 25 > 0) {
            chanceOfNewFlight -= 25;
        }
        
        if (isMultiplayer) {
            redScore.applyTimeBonus();
            blueScore.applyTimeBonus();
        }

        else {
            score.applyTimeBonus();
        }

    }


    // RENDER, UPDATE

    /**
     * update: Update all logic in the airspace class
     */

    public void update() {
        numberOfGameLoops++;
        numberOfGameLoopsSinceLastFlightAdded++;
        numberOfGameLoopsSinceHandover++;

        if (numberOfGameLoops >= numberOfGameLoopsWhenDifficultyIncreases) {
            increaseDifficulty();
        }
        
        if (numberOfGameLoopsSinceHandover > HANDOVER_DELAY) {
        	setHandoverDelay();
        }
        
        // The iterator is used directly (rather than through an enhanced for loop) 
        // so as to avoid a java.util.ConcurrentModificationException when flights are removed during iteration
        for (Iterator<Flight> i = listOfFlightsInAirspace.iterator(); i.hasNext();) {
            Flight f = i.next();
            
            // Update the flight object
            f.update(getScore(f.getOwner()));
            
            // Remove the flight if it's landed and come to a stop
            if (f.isLanding() && f.isGrounded() && f.getVelocity() == 0) {
                i.remove();
                continue;
            }

            // Remove the flight if it's reached it's associated ExitPoint, and thus left the screen
            if (f.getFlightPlan().getCurrentRoute().size() == 0) {
                i.remove();
                continue;
            }

            // Remove the flight (and apply the score penalty) if it's left the screen at any other point
            if (checkIfFlightHasLeftAirspace(f)) {
                getScore(f.getOwner()).applyFlightLossPenalty();
                i.remove();
                continue;
            }            
        }

        separationRules.update(this);
    }

    /**
     * render: Render all of the graphics in the airspace
     * @param g Graphics
     * @throws SlickException
     */

    public void render(Graphics g) throws SlickException {
        backgroundImage.draw(0, 0);

        for (Airport a : listOfAirports) {
            a.render(g);
        }

        for (Waypoint w : listOfWayppoints) {
            w.render(g);
        }

        for (ExitPoint e : listOfExitPoints) {
            e.render(g);
        }

        for (EntryPoint e : listOfEntryPoints) {
            e.render(g);
        }

        for (Flight f : listOfFlightsInAirspace) {
            f.render(g);
        }

        separationRules.render(g, this);
    }


    // MUTATORS AND ACCESSORS

    public List<Flight> getListOfFlights() {
        return listOfFlightsInAirspace;
    }

    public boolean isFlightWithOwner(String owner) {
        return !getListOfFlightsWithOwner(owner).isEmpty();
    }

    public List<Flight> getListOfFlightsWithOwner(String owner) {
        List<Flight> toReturn = new ArrayList<Flight>();

        for (Flight f : listOfFlightsInAirspace) {
            if (f.getOwner() == owner) {
                toReturn.add(f);
            }
        }

        return toReturn;
    }

    public List<Waypoint> getListOfWaypoints() {
        return listOfWayppoints;
    }

    public List<EntryPoint> getListOfEntryPoints() {
        return listOfEntryPoints;
    }

    public List<ExitPoint> getListOfExitPoints() {
        return listOfExitPoints;
    }

    public boolean addWaypoint(Waypoint waypoint) {
        if (listOfWayppoints.contains(waypoint)) {
            return false;
        }

        listOfWayppoints.add(waypoint);
        return true;
    }

    public boolean addEntryPoint(EntryPoint entrypoint) {
        if (listOfEntryPoints.contains(entrypoint)) {
            return false;
        }

        listOfEntryPoints.add(entrypoint);
        return true;
    }

    public boolean addExitPoint(ExitPoint exitpoint) {
        if (listOfExitPoints.contains(exitpoint)) {
            return false;
        }

        listOfExitPoints.add(exitpoint);
        return true;
    }

    public boolean addFlight(Flight f) {
        // Checks whether the flight was already added before, and if it won't pass the maximum number of flights allowed
        if (listOfFlightsInAirspace.contains(f) && listOfFlightsInAirspace.size() > MAX_FLIGHTS - 1) {
            return false;
        }

        // A flight can't be added if it would immediately go into a state of separation violation with another flight
        for (Flight e : listOfFlightsInAirspace) {
            if (Math.abs(e.getAltitude() - f.getAltitude()) <= SeparationRules.VERTICAL_WARNING_DISTANCE &&
                distanceBetween(e, f) <= SeparationRules.LATERAL_WARNING_DISTANCE) {
                    return false;
            }
        }
        
        listOfFlightsInAirspace.add(f);
        return true;
    }
    
    public void removeSpecificFlight(Flight flight) {
        listOfFlightsInAirspace.remove(flight);
    }

    public SeparationRules getSeparationRules() {
        return separationRules;
    }

    public ScoreTracking getScore(String player) {
        if (player == "red") {
            return redScore;
        }

        if (player == "blue") {
            return blueScore;
        }

        return score;
    }

    public void setDifficultyValueOfGame(int i) {
        difficultyValueOfGame = i;
    }

    public int getDifficultyValueOfGame() {
        return difficultyValueOfGame;
    }

    public int getNumberOfGameLoops() {
        return numberOfGameLoops;
    }

    public int getNumberOfGameLoopsWhenDifficultyIncreases() {
        return numberOfGameLoopsWhenDifficultyIncreases;
    }

    public List<Airport> getListOfAirports() {
        return listOfAirports;
    }
    
    public void setHandoverDelay() {
        handoverDelay = !handoverDelay;
    }
    
    public boolean getHandoverDelay() {
    	return handoverDelay;
    }
    
    public void resetLoopsSinceLastHandover() {
    	numberOfGameLoopsSinceHandover = 0;
    }
    
    public int getLoopsSinceLastHandover() {
    	return numberOfGameLoopsSinceHandover;
    }

    @Override
    public String toString() {
        return "Airspace: " + listOfAirports.toString();
    }
    
}

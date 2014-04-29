package logicClasses;

import java.util.ArrayList;
import java.util.List;
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
     * newExitPoint: Add a new exitpoint to the list in the airspace
     * @param x The x coordinate of the exitpoint
     * @param y The y coordinate of the exitpoint
     * @param name The name used to reference the exitpoint
     */

    public boolean newExitPoint(int x, int y, String name) {
        if (x < 1250 && x > -50 && y < 650 && y > -50) {
            // x and y must be within these bounds to be within screen space
            ExitPoint tmpEp = new ExitPoint(x, y, name);
            tmpEp.setPointRef(name);
            if (name != "Exit 1" && name != "Exit 2" && name != "Exit 3") {
                Airport airport = new Airport(x, y, name);
                this.listOfAirports.add(airport);
            }

            if (this.addExitPoint(tmpEp)) {
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
        if (x < 1250 && x > -50 && y < 650 && y > -50) {
            EntryPoint tmpEp = new EntryPoint(x, y);

            if (this.addEntryPoint(tmpEp)) {
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
     * increaseDifficulty
     */

    public void increaseDifficulty() {
        this.numberOfGameLoopsWhenDifficultyIncreases += DIFFICULTY_INCREASE_INTERVAL;

        if (this.chanceOfNewFlight - 25 > 0) {
            this.chanceOfNewFlight -= 25;
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
        this.numberOfGameLoopsSinceLastFlightAdded++;
        this.numberOfGameLoops++;
        this.numberOfGameLoopsSinceHandover++;

        if (this.numberOfGameLoops >= this.numberOfGameLoopsWhenDifficultyIncreases) {
            this.increaseDifficulty();
        }
        
        if (this.numberOfGameLoopsSinceHandover>HANDOVER_DELAY) {
        	this.setHandoverDelay();
        }

        for (int i = 0; i < this.listOfFlightsInAirspace.size(); i++) {
            this.listOfFlightsInAirspace.get(i).update(this.getScore(this.listOfFlightsInAirspace.get(i).getOwner()));

            if (this.listOfFlightsInAirspace.get(i).getFlightPlan().getCurrentRoute().size() == 0) {
                this.removeSpecificFlight(i);
            }

            if (this.listOfFlightsInAirspace.get(i).getRemove()) {
                this.removeSpecificFlight(i);
            }

            else if (this.checkIfFlightHasLeftAirspace(this.getListOfFlights().get(i))) {
                this.getScore(this.listOfFlightsInAirspace.get(i).getOwner()).applyFlightLossPenalty();
                this.removeSpecificFlight(i);
            }
        }

        this.separationRules.update(this);
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
        return this.listOfFlightsInAirspace;
    }

    public boolean isFlightWithOwner(String owner) {
        return !this.getListOfFlightsWithOwner(owner).isEmpty();
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
        return this.listOfWayppoints;
    }

    public List<EntryPoint> getListOfEntryPoints() {
        return this.listOfEntryPoints;
    }

    public List<ExitPoint> getListOfExitPoints() {
        return this.listOfExitPoints;
    }

    public boolean addWaypoint(Waypoint waypoint) {
        if (this.listOfWayppoints.contains(waypoint)) {
            return false;
        }

        this.listOfWayppoints.add(waypoint);
        return true;
    }

    public boolean addEntryPoint(EntryPoint entrypoint) {
        if (this.listOfEntryPoints.contains(entrypoint)) {
            return false;
        }

        this.listOfEntryPoints.add(entrypoint);
        return true;
    }

    public boolean addExitPoint(ExitPoint exitpoint) {
        if (this.listOfExitPoints.contains(exitpoint)) {
            return false;
        }

        this.listOfExitPoints.add(exitpoint);
        return true;
    }

    public boolean addFlight(Flight f) {
        // Checks whether the flight was already added before, and if it won't pass the maximum number of flights allowed
        if (listOfFlightsInAirspace.contains(f) && listOfFlightsInAirspace.size() > MAX_FLIGHTS - 1) {
            return false;
        }

        // If the flight to be added is to start on a runway, and there's already a plane there, then it can't be added
        if (f.getFlightPlan().getEntryPoint().isRunway()) {
            for (Flight a : listOfFlightsInAirspace) {
                if (a.isGrounded() && 
                    a.getX() == f.getFlightPlan().getEntryPoint().getX() && 
                    a.getY() == f.getFlightPlan().getEntryPoint().getY()) {
                        return false;
                    }
            }
        }
        
        listOfFlightsInAirspace.add(f);
        return true;
    }

    public void removeSpecificFlight(int flight) {
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
        this.difficultyValueOfGame = i;
    }

    public int getDifficultyValueOfGame() {
        return this.difficultyValueOfGame;
    }

    public int getNumberOfGameLoops() {
        return this.numberOfGameLoops;
    }

    public int getNumberOfGameLoopsWhenDifficultyIncreases() {
        return this.numberOfGameLoopsWhenDifficultyIncreases;
    }

    public List<Airport> getAirport() {
        return this.listOfAirports;
    }
    
    public void setHandoverDelay() {
        handoverDelay = !handoverDelay;
    }
    
    public boolean getHandoverDelay() {
    	return this.handoverDelay;
    }
    
    public void resetLoopsSinceLastHandover() {
    	this.numberOfGameLoopsSinceHandover = 0;
    }
    
    public int getLoopsSinceLastHandover() {
    	return this.numberOfGameLoopsSinceHandover;
    }

    @Override
    public String toString() {
        return "Airspace: " + this.listOfAirports.toString();
    }
    
}

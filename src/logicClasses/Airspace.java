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
    
    // Fields
    private int chanceOfNewFlight = 500;
   
    private int numberOfGameLoops = 0;
    private int numberOfGameLoopsSinceLastFlightAdded = 0;
    private int numberOfGameLoopsWhenDifficultyIncreases = DIFFICULTY_INCREASE_INTERVAL;
	
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
	private boolean isMultiplayer;


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
			if (name!="Exit 1"&&name!="Exit 2"&&name!="Exit 3"){
				Airport airport = new Airport (x, y, name);
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
	 * newFlight: Add a new flight to the list of flights in the airspace if it has been long enough since the last flight was added and if random number satisfies condition
	 * The flight is also given a name
	 * @throws SlickException
	 */

	public boolean newFlight() throws SlickException {
		if (this.listOfFlightsInAirspace.size() < MAX_FLIGHTS) {
			if ((this.numberOfGameLoopsSinceLastFlightAdded >= 850  || this.listOfFlightsInAirspace.isEmpty())) {

				/*
				 * The random number is generated in the range [0, 100) if the airspace is empty, as this increases
				 * the likelihood of a value of 1 being returned, and therefore a flight being generated; this stops the user
				 * having to potentially wait a long period of time for a flight to be generated.
				 * If the airspace is not empty, the random number generated is in the range [0, randomNumberForFlight Generation)
				 * which is > 100. This decreases the likelihood of a flight being generated.
				 */

                int checkNumber;

				if (this.listOfFlightsInAirspace.isEmpty()) {
					checkNumber = rand.nextInt(100); 
				}

				else {
					checkNumber = rand.nextInt(chanceOfNewFlight);
				}

				if (checkNumber == 1) {
					Flight tempFlight = new Flight(this);
					tempFlight.setFlightName(this.generateFlightName());

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
						if ( tempFlight.getX () == this.listOfAirports.get(0).getX() ){
							heading = this.listOfAirports.get(0).getRunwayHeading();
						}
						else {
							heading = this.listOfAirports.get(1).getRunwayHeading();
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
						this.numberOfGameLoopsSinceLastFlightAdded = 0;
						return true;
					}
				}
			}
		}

		return false;
	}


	/**
	 * generateFlightName: Generate a random name for a flight, based on UK flight tail numbers
	 * @return Returns a random string that can be used to identify a flight.
	 */

	public String generateFlightName() {
		String name = "G-";

		for (int i = 0; i < 4; i++) {
			int thisChar = rand.nextInt(10) + 65; // Generates int in range [65, 74]
			name += (char) thisChar; // Generate corresponding ascii character for int
		}

		return name;
	}

	/**
	 * checkIfFlightHasLeftAirspace: Check if a flight is outside the area of the game, and if it is removed the object so it is not
	 * using unnecessary resources.
	 * @param flight The flight being checked.
	 */

	public boolean checkIfFlightHasLeftAirspace(Flight flight) {
		if (flight.getX() > 1250 || flight.getX() < -50 || flight.getY() > 650 || flight.getY() < -50) { 
			// x and y must be within these bounds to be within screen space
			return true;
		}

		else {
			return false;
		}
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
        
		if (this.numberOfGameLoops >= this.numberOfGameLoopsWhenDifficultyIncreases) {
			this.increaseDifficulty();
		}

		for (int i = 0; i < this.listOfFlightsInAirspace.size(); i++) {
			this.listOfFlightsInAirspace.get(i).update(this.getScore(this.listOfFlightsInAirspace.get(i).getOwner()));

			if (this.listOfFlightsInAirspace.get(i).getFlightPlan().getCurrentRoute().size() == 0) {
				this.removeSpecificFlight(i);
			}
            
			if (this.listOfFlightsInAirspace.get(i).getRemove()){
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
	 *
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

	public int getMaxNumberOfFlights() {
		return MAX_FLIGHTS;
	}

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

		else {
			this.listOfWayppoints.add(waypoint);
			return true;
		}
	}

	public boolean addEntryPoint(EntryPoint entrypoint) {
		if (this.listOfEntryPoints.contains(entrypoint)) {
			return false;
		}

		else {
			this.listOfEntryPoints.add(entrypoint);
			return true;
		}
	}

	public boolean addExitPoint(ExitPoint exitpoint) {
		if (this.listOfExitPoints.contains(exitpoint)) {
			return false;
		}

		else {
			this.listOfExitPoints.add(exitpoint);
			return true;
		}
	}

	public boolean addFlight(Flight flight) {
		// Checks whether the flight was already added before, and if it won't pass the maximum number of flights allowed
		if ((this.listOfFlightsInAirspace.contains(flight))
				&& (this.listOfFlightsInAirspace.size() > MAX_FLIGHTS - 1)) {
			return false;
		}

		else {
			// If the flight to be added is to start on the runway, and there's already a plane there, then it can't be added
			// TODO: Change this code to allow for multiple airports
			for (Flight a : listOfFlightsInAirspace) {
				if (a.isGrounded() && flight.getFlightPlan().getEntryPoint().isRunway()) {
					return false;
				}
			}

			this.listOfFlightsInAirspace.add(flight);
			return true;
		}
	}

	public void removeSpecificFlight(int flight) {
		this.listOfFlightsInAirspace.remove(flight);
	}

	public void removeWaypoint(Waypoint waypoint) {
		this.listOfWayppoints.remove(waypoint);
	}

	public void removeEntryPoint(EntryPoint entrypoint) {
		this.listOfEntryPoints.remove(entrypoint);
	}

	public void removeExitPoint(ExitPoint exitpoint) {
		this.listOfExitPoints.remove(exitpoint);
	}

	public SeparationRules getSeparationRules() {
		return this.separationRules;
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

	public void setListOfEntryPoints(List<EntryPoint> listOfEntryPoints) {
		this.listOfEntryPoints = listOfEntryPoints;
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

	@Override
	public String toString() {
		String s = "Airspace: " + this.listOfAirports.toString();
		return s;
	}
}

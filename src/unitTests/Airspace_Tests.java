package unitTests;

import static org.junit.Assert.*;
import logicClasses.*;

import org.junit.Test;
import org.junit.Before;

public class Airspace_Tests {
	
	private Airspace airspace;
	private  Flight flight1;
	
	@Before
	public void setUp(){
    	airspace = new Airspace();
    	// Waypoints
    	airspace.newWaypoint(350, 150, "A");
    	airspace.newWaypoint(400, 470, "B");
    	airspace.newWaypoint(700, 60,  "C");
    	airspace.newWaypoint(800, 320, "D");
    	airspace.newWaypoint(600, 418, "E");
    	airspace.newWaypoint(500, 220, "F");
    	airspace.newWaypoint(950, 188, "G");
    	airspace.newWaypoint(1050, 272,"H");
    	airspace.newWaypoint(900, 420, "I");
    	airspace.newWaypoint(240, 250, "J");
    	// EntryPoints
    	airspace.newEntryPoint(150, 400);
    	airspace.newEntryPoint(1200, 200);
    	airspace.newEntryPoint(600, 0);
    	// Exit Points
    	airspace.newExitPoint(800, 0, "1");
    	airspace.newExitPoint(150, 200, "2");
    	airspace.newExitPoint(1200, 300, "3");
    	// Get a Flight
    	flight1 = new Flight(airspace);
    	
    	airspace.setDifficultyValueOfGame(1);
    	airspace.createAndSetSeparationRules();

		
	}
	
	//Testing resetAirspace()
	@Test
	public void resetAirspaceTest(){
		// Test that the function resets the airspace.
		airspace.resetAirspace();
		assertTrue(airspace.getListOfFlights().size() == 0);
		assertTrue(airspace.getNumberOfGameLoops() == 0);
		assertTrue(airspace.getNumberOfGameLoopsWhenDifficultyIncreases() == 3600);
		assertTrue(airspace.getSeparationRules().getGameOverViolation() == false);
		assertTrue(airspace.getControls().getSelectedFlight() == null);
	}
	
	
	// Testing new_waypoint()
	@Test
	public void newWaypointTest(){
		// Tests that waypoint added when placed within airspace. Also
		// tests that waypoint isn't added when outside range of the airspace.
		assertTrue(airspace.newWaypoint(151, 500, "TEST"));
		assertFalse(airspace.newWaypoint(-10000, 151, "TEST2"));
		assertFalse(airspace.newWaypoint(50, 0, "TEST3"));
	}
	
	
	// Testing new_exit_point()
	@Test
	public void newExitPointTest(){
		// Tests that exitpoint added when placed within airspace. Also
		// tests that exitpoint isn't added when outside range of the airspace.
		assertTrue(airspace.newExitPoint(150, 500, "TEST"));
		assertFalse(airspace.newExitPoint(-100, 220, "TEST2"));
		assertFalse(airspace.newExitPoint(0, 23, "TEST3"));
	}

	// Testing new_entry_point()
	@Test
	public void newEntryPointTest(){
		// Tests that entrypoint added when placed within airspace. Also
		// tests that entrypoint isn't added when outside range of the airspace.
		assertTrue(airspace.newEntryPoint(150, 500));
		assertFalse(airspace.newEntryPoint(60, -540));
		assertFalse(airspace.newEntryPoint(0, 0));
	}

	
	// Testing generate_flight_name()
	@Test
	public void generateFlightNameTest(){
		// Test that the name generated is the correct length.
		String name = airspace.generateFlightName();
		assertTrue((name.length() == 6));
	}
	
	// Testing check_if_flight_has_left_airspace()
	@Test
	public void checkIfFlightHasLeftAirspaceTest(){
			//Testing that a flight leaves the airspace when appropriate.
			flight1.setX(100);
			flight1.setY(-50);
			assertFalse(airspace.checkIfFlightHasLeftAirspace(flight1));
			
			flight1.setX(1250);
			flight1.setY(650);
			assertFalse(airspace.checkIfFlightHasLeftAirspace(flight1));
			
			flight1.setX(1251);
			flight1.setY(5);
			assertTrue(airspace.checkIfFlightHasLeftAirspace(flight1));

			flight1.setX(101);
			flight1.setY(0);
			assertFalse(airspace.checkIfFlightHasLeftAirspace(flight1));
			
			flight1.setX(-143401);
			flight1.setY(101010);
			assertTrue(airspace.checkIfFlightHasLeftAirspace(flight1));
	}
}



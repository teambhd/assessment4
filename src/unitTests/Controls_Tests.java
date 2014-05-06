package unitTests;

import static org.junit.Assert.*;

import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.EntryPoint;
import logicClasses.Flight;
import logicClasses.SeparationRules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;

import stateContainer.Game;

import util.KeyBindings;

public class Controls_Tests {

    /**
	 * @uml.property  name="controlsInstance"
	 * @uml.associationEnd  
	 */
    private Controls controlsInstance;
    /**
	 * @uml.property  name="airspace"
	 * @uml.associationEnd  
	 */
    private Airspace airspace;
    /**
	 * @uml.property  name="flight1"
	 * @uml.associationEnd  
	 */
    private Flight flight1;

    @Before
    public void Setup() {
        controlsInstance = new Controls(KeyBindings.singlePlayerKeys, "single");
        airspace = new Airspace(false);
        // Waypoints
        airspace.newWaypoint(350, 150, "A");
        airspace.newWaypoint(400, 470, "B");
        airspace.newWaypoint(700, 60,  "C");
        airspace.newWaypoint(800, 320, "D");
        airspace.newWaypoint(600, 418, "E");
        airspace.newWaypoint(500, 220, "F");
        airspace.newWaypoint(950, 188, "G");
        airspace.newWaypoint(1050, 272, "H");
        airspace.newWaypoint(900, 420, "I");
        airspace.newWaypoint(240, 250, "J");
        //EntryPoints
        airspace.newEntryPoint(150, 400);
        airspace.newEntryPoint(1200, 200);
        airspace.newEntryPoint(600, 0);
        airspace.newEntryPoint(760, 405);
        // Exit Points
        airspace.newExitPoint(800, 0, "1");
        airspace.newExitPoint(150, 200, "2");
        airspace.newExitPoint(1200, 300, "3");
        airspace.newExitPoint(590, 195, "4");
        // Airport
        airspace.newAirport(700, 300, 0, "BHD");
        // Get a Flight
        flight1 = new Flight(airspace);
        airspace.setDifficultyValueOfGame(1);

    }

    @After
    public void tearDown() {
        controlsInstance = null;
    }
    
    @Test
    public void testUnavailableControls () {
    	//Test that controls are not available to landing flights
    	flight1.setX(airspace.getListOfAirports().get(0).getX());
    	flight1.setY(airspace.getListOfAirports().get(0).getY());
    	flight1.setCurrentHeading(airspace.getListOfAirports().get(0).getRunwayHeading());
    	flight1.setTargetHeading(flight1.getCurrentHeading());
    	flight1.setVelocity(Flight.MIN_VELOCITY);
    	flight1.setAltitude(Flight.MIN_ALTITUDE);
    	flight1.land();
    	assertFalse (flight1.isCommandable());
    }
    
}
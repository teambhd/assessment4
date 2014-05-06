package unitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logicClasses.*;

import org.junit.Test;
import org.junit.Before;

public class FlightPlan_Tests {

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
    /**
	 * @uml.property  name="flightplan"
	 * @uml.associationEnd  
	 */
    private FlightPlan flightplan;

    @Before
    public void setUp() {
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
        // Get a Flight
        flight1 = new Flight(airspace);
        airspace.setDifficultyValueOfGame(1);
        airspace.createAndSetSeparationRules();

        flightplan = new FlightPlan(airspace, flight1);
    }

    // Testing generate_entry_point()

    @Test
    public void generateEntryPointTest1() {
        // Testing that the function generates a entrypoint that is contained within the entrypoint list
        EntryPoint result = flight1.getFlightPlan().generateEntryPoint(airspace);
        System.out.println(airspace.getListOfEntryPoints());
        assertTrue(airspace.getListOfEntryPoints().contains(result));
    }

    @Test
    public void generateVelocityTest() {
        // Testing that the velocity generated is always within the constraints applied.
        for (int i = 0; i < 100; i++) {
            double velocity = flightplan.generateVelocity();
            assertTrue((velocity < 400 && velocity >= 200) || velocity == 0);
        }
    }

    @Test
    public void buildRouteTest1() {
        // Testing Length of Route
        for (int i = 0; i < 100; i++) {
            ArrayList<Point> route = flightplan.buildRoute(airspace, flight1.getFlightPlan().getEntryPoint());
            assertTrue(route.size() >= 2 && route.size() <= 5);
        }
    }

    @Test
    public void buildRouteTest2() {
        //Testing that it doesn't repeat waypoints
        for (int i = 0; i < 100; i++) {
            ArrayList<Point> route = flightplan.buildRoute(airspace, flight1.getFlightPlan().getEntryPoint());
            boolean samePoint = false;

            for (int j = 0; j < route.size(); j++) {
                for (int k = j + 1; k < route.size(); k++) {
                    if (route.get(j).equals(route.get(k))) {
                        samePoint = true;
                    }
                }
            }

            assertFalse(samePoint);
        }
    }

    @Test
    public void buildRouteTest3() {
        // Testing that it doesn't build a route if no airspace has no waypoints
        Airspace airspaceMissingExitPoints = new Airspace(false);
        //EntryPoints
        airspaceMissingExitPoints.newEntryPoint(150, 400);
        airspaceMissingExitPoints.newEntryPoint(1200, 200);
        airspaceMissingExitPoints.newEntryPoint(600, 0);
        //Waypoints
        airspaceMissingExitPoints.newWaypoint(350, 150, "A");
        airspaceMissingExitPoints.newWaypoint(400, 470, "B");
        airspaceMissingExitPoints.newWaypoint(700, 60,  "C");
        airspaceMissingExitPoints.newWaypoint(800, 320, "D");
        airspaceMissingExitPoints.newWaypoint(600, 418, "E");
        airspaceMissingExitPoints.newWaypoint(500, 220, "F");
        airspaceMissingExitPoints.newWaypoint(950, 188, "G");
        airspaceMissingExitPoints.newWaypoint(1050, 272, "H");
        airspaceMissingExitPoints.newWaypoint(900, 420, "I");
        airspaceMissingExitPoints.newWaypoint(240, 250, "J");
        Flight flight1 = new Flight(airspaceMissingExitPoints);
        ArrayList<Point> route = flightplan.buildRoute(airspaceMissingExitPoints, flight1.getFlightPlan().getEntryPoint());
        assertTrue(route.size() == 0);
    }

    @Test
    public void buildRouteTest4() {
        // Testing that it doesn't build a route if no airspace has no exitpoints
        Airspace airspaceMissingWaypoints = new Airspace(false);
        //EntryPoints
        airspaceMissingWaypoints.newEntryPoint(150, 400);
        airspaceMissingWaypoints.newEntryPoint(1200, 200);
        airspaceMissingWaypoints.newEntryPoint(600, 0);
        Flight flight1 = new Flight(airspaceMissingWaypoints);
        ArrayList<Point> route = flightplan.buildRoute(airspaceMissingWaypoints, flight1.getFlightPlan().getEntryPoint());
        assertTrue(route.size() == 0);
    }

    @Test
    public void updateFlightPlanTest() {
        // Tests that a waypoint is removed from the flightplan when visited.
    	System.out.println(flight1.getFlightPlan());
        int previousSize = flight1.getFlightPlan().getCurrentRoute().size();
        flight1.takeOff();
        flight1.setX(flight1.getFlightPlan().getCurrentRoute().get(0).getX());
        flight1.setY(flight1.getFlightPlan().getCurrentRoute().get(0).getY());
        System.out.println(flight1.getFlightPlan());
        airspace.update();
        flight1.getFlightPlan().update(new ScoreTracking()); // {!} need to test these properly
        System.out.println(flight1.getFlightPlan());
        assertEquals(previousSize - 1, flight1.getFlightPlan().getCurrentRoute().size());
    }

    @Test
    public void updateFlightPlanTest2() {
        // Tests that a waypoint is not removed from the flightplan when not visited.
        int previousSize = flight1.getFlightPlan().getCurrentRoute().size();
        flight1.setX(flight1.getFlightPlan().getCurrentRoute().get(0).getX() + 100);
        flight1.setY(flight1.getFlightPlan().getCurrentRoute().get(0).getY() + 100);
        flight1.getFlightPlan().update(new ScoreTracking()); // {!} need to test these properly
        assertEquals(previousSize, flight1.getFlightPlan().getCurrentRoute().size(), 0);
    }

}

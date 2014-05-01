package logicClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static logicClasses.Airspace.distanceBetween;

public class FlightPlan {

    // The colours for the lines that trace the routes
    private static final Color RED_COLOR = new Color(197, 131, 138);
    private static final Color BLUE_COLOR = new Color(141, 191, 244);
    
    // Random number generator
    private static final Random rand = new Random();

    // Fields
    private EntryPoint entryPoint;
    private ExitPoint exitPoint;
    
    private List<Point> currentRoute = new ArrayList<Point>(); // Array that stores the current list of waypoints
    private List<Point> waypointsAlreadyVisited = new ArrayList<Point>(); // All the waypoints the flight has passed through
    
    private Flight flight; // The flight object associated with the flight plan
    
    private int closestDistance;

    // Constructor
    public FlightPlan(Airspace airspace, Flight flight) {
        this.flight = flight;
        
        this.entryPoint = generateEntryPoint(airspace);

        flight.setVelocity(generateVelocity());
        flight.setTargetVelocity(flight.getVelocity());

        this.currentRoute = buildRoute(airspace, this.entryPoint);
    }


    // METHODS

    /**
     * generateEntryPoint: Creates the entry point for the flight
     * @param airspace airspace object
     * @return airspace.getListofEntryPoints
     */

    public EntryPoint generateEntryPoint(Airspace airspace) {
        int randomNumber = rand.nextInt(airspace.getListOfEntryPoints().size());

        // Setting flights x and y to the coordinates of it's entrypoint
        flight.setX(airspace.getListOfEntryPoints().get(randomNumber).getX()); // choose one a get the x and y values
        flight.setY(airspace.getListOfEntryPoints().get(randomNumber).getY());
        return airspace.getListOfEntryPoints().get(randomNumber);
    }

    /**
     * buildRoute: Creates an array of waypoints that the aircraft will be initially given
     * @param airspace airspace object
     * @param entryPoint entry point object
     * @return tempRoute
     */

    public ArrayList<Point> buildRoute(Airspace airspace, EntryPoint entryPoint) {

        // Create the array lists for route and points
        ArrayList<Point> tempRoute = new ArrayList<Point>();

        if (!airspace.getListOfWaypoints().isEmpty() && !airspace.getListOfExitPoints().isEmpty()) {

            // Add an ExitPoint to the plan, ensuring that a plane isn't set to exit on the same edge as it arrived from

            ArrayList<ExitPoint> shuffledExitPointList = new ArrayList<ExitPoint>(airspace.getListOfExitPoints());
            Collections.shuffle(shuffledExitPointList);

            for (ExitPoint exitPoint : shuffledExitPointList) {
                if (entryPoint.getY() == 0 && exitPoint.getY() == 0) {
                    continue;
                }

                if (entryPoint.getX() == 0 && exitPoint.getX() == 0) {
                    continue;
                }

                if (entryPoint.getX() == stateContainer.Game.WIDTH && exitPoint.getX() == stateContainer.Game.WIDTH) {
                    continue;
                }

                if (entryPoint.isRunway() && exitPoint.isRunway()) {
                    continue;
                }

                tempRoute.add(exitPoint);
                this.exitPoint = exitPoint;
                break;
            }


            // Add some intermediate waypoints to the flight plan

            ArrayList<Waypoint> shuffledWaypointList = new ArrayList<Waypoint>(airspace.getListOfWaypoints());
            Collections.shuffle(shuffledWaypointList);

            // Randomly add 2 intermediate waypoints to the route
            for (Waypoint v : shuffledWaypointList) {

                // Exit if we've added the correct number of waypoints
                if (tempRoute.size() == 3) {
                    break;
                }

                for (Waypoint w : shuffledWaypointList) {

                    if (v == w) {
                        continue;
                    }

                    // This humungous if statement tries to ensure that doubling-back between waypoints is avoided
                    if (distanceBetween(v, exitPoint) > distanceBetween(w, exitPoint)
                            && distanceBetween(entryPoint, v) < distanceBetween(entryPoint, w)) {
                        tempRoute.add(0, v);
                        tempRoute.add(1, w);
                        break;
                    }

                }

            }

        }

        return tempRoute;
    }

    /**
     * generateVelocity: Creates a velocity from a range of values
     */

    public int generateVelocity() {
        if (entryPoint.isRunway()) {
            return 0;
        }

        return (rand.nextInt((Flight.MAX_VELOCITY - Flight.MIN_VELOCITY) / 50) * 50) + Flight.MIN_VELOCITY;
    }

    /**
     * update: Handles updating the flight plan when a flight passes through a waypoint
     */

    public void update(ScoreTracking score) {
        int waypointScore = 0;

        if (!currentRoute.isEmpty()) {
            // Check to see if there are still waypoints to visit and then check if the flight is passing through waypoint
            if (flight.checkIfFlightAtWaypoint(currentRoute.get(0))) {
                waypointsAlreadyVisited.add(currentRoute.get(0));
                
                closestDistance = flight.getClosestDistanceFromWaypoint();
                flight.resetClosestDistanceFromWaypoint();
                waypointScore = score.updateWaypointScore(closestDistance); // update the score based on how close to the waypoints
                
                currentRoute.remove(0);
            }

            score.updateScore(waypointScore);
        }
    }

    public void render(Graphics g) throws SlickException {
        if (!currentRoute.isEmpty()) {

            if (flight.getOwner() == "red") {
                g.setColor(RED_COLOR);
            }

            else if (flight.getOwner() == "blue") {
                g.setColor(BLUE_COLOR);
            }

            else {
                g.setColor(Color.lightGray);
            }

            // Draw a line from the flight to it's next waypoint
            g.drawLine(
                (float)flight.getX(),
                (float)flight.getY(),
                (float)currentRoute.get(0).getX(),
                (float)currentRoute.get(0).getY()
            );

            // Draw lines between all waypoints in plan
            for (int i = 1; i < currentRoute.size(); i++) {
                g.drawLine(
                    (float)currentRoute.get(i).getX(),
                    (float)currentRoute.get(i).getY(),
                    (float)currentRoute.get(i - 1).getX(),
                    (float)currentRoute.get(i - 1).getY()
                );
            }            
        }
    }


    // ACCESSORS AND MUTATORS

    public List<Point> getCurrentRoute() {
        return currentRoute;
    }

    public Point getPointByIndex(int i) {
        return currentRoute.get(i);
    }

    public EntryPoint getEntryPoint() {
        return entryPoint;
    }

    public ExitPoint getExitPoint() {
        return exitPoint;
    }


    @Override
    public String toString() {
        String returnString = "";

        if (currentRoute.size() > 0) {
            returnString = currentRoute.get(0).getPointRef();
        }

        for (int i = 1; i < currentRoute.size(); i++) {
            returnString += ", " + currentRoute.get(i).getPointRef();
        }
        
        return returnString;
    }

}
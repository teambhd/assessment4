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

import stateContainer.Game;

public class FlightPlan {

    private static final Color RED_COLOR = new Color(197, 131, 138);
    private static final Color BLUE_COLOR = new Color(141, 191, 244);

    // FIELDS
    private EntryPoint entryPoint;
    private ExitPoint exitPoint;
    private List<Point>
    currentRoute = new ArrayList<Point>(), // Array that stores the current list of waypoints
    waypointsAlreadyVisited; // Array that stores all the waypoints the flight has passed through
    private Flight flight; // The flight object associated with the flight plan
    private int closestDistance;

    // CONSTRUCTOR

    public FlightPlan(Airspace airspace, Flight flight) {
        this.flight = flight;
        this.entryPoint = generateEntryPoint(airspace);
        double v = generateVelocity();
        flight.setVelocity(v);
        flight.setTargetVelocity(v);
        this.currentRoute = buildRoute(airspace, this.entryPoint);
        this.waypointsAlreadyVisited = new ArrayList<Point>();
    }

    // METHODS

    /**
     * generateEntryPoint: Creates the entry point for the flight
     * @param airspace airspace object
     * @return airspace.getListofEntryPoints
     */

    public EntryPoint generateEntryPoint(Airspace airspace) {
        Random rand = new Random();
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
                    if (Point.distanceBetween(v, exitPoint) > Point.distanceBetween(w, exitPoint)
                            && Point.distanceBetween(entryPoint, v) < Point.distanceBetween(entryPoint, w)) {
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
        Random r = new Random();

        if (entryPoint.isRunway()) {
            return 0;
        }

        return (r.nextInt((Flight.MAX_VELOCITY - Flight.MIN_VELOCITY) / 50) * 50) + Flight.MIN_VELOCITY;
    }

    /**
     * update: Handles updating the flight plan when a flight passes through a waypoint
     */

    public void update(ScoreTracking score) {
        int waypointScore = 0;

        if (this.currentRoute.size() > 0) {
            // Check to see if there are still waypoints to visit and then check if the flight is passing through waypoint
            if (this.flight.checkIfFlightAtWaypoint(currentRoute.get(0))) {
                this.waypointsAlreadyVisited.add(this.currentRoute.get(0));
                closestDistance = this.flight.minDistanceFromWaypoint(this.currentRoute.get(0)); // get the closest distance from the waypoint
                flight.resetMinDistanceFromWaypoint();
                waypointScore = score.updateWaypointScore(closestDistance); // update the score based on how close to the waypoints
                this.currentRoute.remove(0);
            }

            score.updateScore(waypointScore);
        }
    }

    public void render(Graphics g) throws SlickException {
        if (this.currentRoute.size() > 0) {

            if (this.flight.getOwner() == "red") {
                g.setColor(RED_COLOR);
            }

            else if (this.flight.getOwner() == "blue") {
                g.setColor(BLUE_COLOR);
            }

            else {
                g.setColor(Color.lightGray);
            }

            // Draw a line from the flight to it's next waypoint
            g.drawLine(
                (float)this.flight.getX(),
                (float)this.flight.getY(),
                (float)this.currentRoute.get(0).getX(),
                (float)this.currentRoute.get(0).getY()
            );

            // Draw lines between all waypoints in plan
            for (int i = 1; i < this.currentRoute.size(); i++) {
                g.drawLine(
                    (float)this.currentRoute.get(i).getX(),
                    (float)this.currentRoute.get(i).getY(),
                    (float)this.currentRoute.get(i - 1).getX(),
                    (float)this.currentRoute.get(i - 1).getY()
                );
            }
        }
    }


    // ACCESSORS AND MUTATORS

    public List<Point> getCurrentRoute() {
        return currentRoute;
    }

    public Point getPointByIndex(int i) {
        return this.currentRoute.get(i);
    }

    public EntryPoint getEntryPoint() {
        return this.entryPoint;
    }

    public ExitPoint getExitPoint() {
        return this.exitPoint;
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
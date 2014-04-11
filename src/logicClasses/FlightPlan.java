package logicClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import stateContainer.Game;

public class FlightPlan {

    // FIELDS
    private EntryPoint entryPoint;
    private ExitPoint exitPoint;
    private List<Point>
    currentRoute = new ArrayList<Point>(), // Array that stores the current list of waypoints
    waypointsAlreadyVisited; // Array that stores all the waypoints the flight has passed through
    private Flight flight; // The flight object associated with the flight plan
    private int closestDistance;
    private static final int    //waypoint ID references
    A = 0, B = 1, C = 2, D = 3, E = 4, F = 5, G = 6, H = 7, I = 8, J = 9;


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
        ArrayList<Point> tempRoute = new ArrayList<Point>();  // Create the array lists for route and points
        ArrayList<Point> tempListOfWaypoints = new ArrayList<Point>();
        ArrayList<ExitPoint> tempListOfExitPoints = new ArrayList<ExitPoint>();
        Boolean exitpointAdded = false;

        if (!airspace.getListOfWaypoints().isEmpty() && !airspace.getListOfExitPoints().isEmpty()) {

            // Initialise a temporary list of all waypoints
            for (int i = 0; i < airspace.getListOfWaypoints().size(); i++) { //loop through all waypoints and add them to tempwaypoints
                tempListOfWaypoints.add(airspace.getListOfWaypoints().get(i));
            }
            
            ArrayList<ExitPoint> shuffledList = new ArrayList<ExitPoint>(airspace.getListOfExitPoints());
            Collections.shuffle(shuffledList);
            // Add an ExitPoint to the plan, ensuring that a plane isn't set to exit on the same edge as it arrived from
            for (ExitPoint exitPoint : shuffledList) {
                if (entryPoint.getY() == 0 && exitPoint.getY() == 0) {
                    continue;
                }

                if (entryPoint.getX() == 150 && exitPoint.getX() == 150) {
                    continue;
                }

                if (entryPoint.getX() == 1200 && exitPoint.getX() == 1200) {
                    continue;
                }

                if (entryPoint.isRunway() && exitPoint.isRunway()) {
                    continue;
                }

                tempRoute.add(exitPoint);
                this.exitPoint = exitPoint;
                break;
            }

            for (int i = 0; i < airspace.getListOfEntryPoints().size(); i ++) {
                if (entryPoint == airspace.getListOfEntryPoints().get(i)) {
                    switch (i) {
                    // entry point on the left
                    case 0:
                        for (int k = 0; k < airspace.getListOfExitPoints().size(); k++) {
                            if (tempRoute.get(tempRoute.size() - 1) == airspace.getListOfExitPoints().get(k)) {
                                int randInt = new Random().nextInt(2);

                                switch (k) {
                                //left to top
                                case 0:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(J));
                                        tempRoute.add(1, tempListOfWaypoints.get(A));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(C));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(B));
                                        tempRoute.add(1, tempListOfWaypoints.get(E));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(G));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(B));
                                        tempRoute.add(1, tempListOfWaypoints.get(E));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(C));
                                        break;
                                    }

                                    break;

                                //left to right
                                case 2:
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(J));
                                        tempRoute.add(1, tempListOfWaypoints.get(F));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(B));
                                        tempRoute.add(1, tempListOfWaypoints.get(E));
                                        tempRoute.add(2, tempListOfWaypoints.get(I));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(J));
                                        tempRoute.add(1, tempListOfWaypoints.get(F));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(I));
                                        break;
                                    }

                                    break;

                                //left to runway
                                case 3:
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(B));
                                        tempRoute.add(1, tempListOfWaypoints.get(E));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(I));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(J));
                                        tempRoute.add(1, tempListOfWaypoints.get(A));
                                        tempRoute.add(2, tempListOfWaypoints.get(C));
                                        tempRoute.add(3, tempListOfWaypoints.get(G));
                                        tempRoute.add(4, tempListOfWaypoints.get(I));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(J));
                                        tempRoute.add(1, tempListOfWaypoints.get(F));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(I));
                                        break;
                                    }

                                    break;
                                }
                            }
                        }

                        break;

                    // entry point on the right
                    case 1:
                        for (int k = 0; k < airspace.getListOfExitPoints().size(); k++) {
                            if (tempRoute.get(tempRoute.size() - 1) == airspace.getListOfExitPoints().get(k)) {
                                int randInt = new Random().nextInt(2);

                                switch (k) {
                                //right to left
                                case 0:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(C));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(G));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(C));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(I));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(C));
                                        break;
                                    }

                                    break;

                                //right to top
                                case 1:
                                    switch (randInt) {
                                    //selects random flight plan
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(J));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(G));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(E));
                                        tempRoute.add(3, tempListOfWaypoints.get(B));
                                        tempRoute.add(4, tempListOfWaypoints.get(J));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(I));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        tempRoute.add(3, tempListOfWaypoints.get(F));
                                        tempRoute.add(4, tempListOfWaypoints.get(A));
                                        tempRoute.add(5, tempListOfWaypoints.get(J));
                                        break;
                                    }

                                    break;

                                //right to runway
                                case 3:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(I));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(E));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(H));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(C));
                                        tempRoute.add(3, tempListOfWaypoints.get(F));
                                        tempRoute.add(4, tempListOfWaypoints.get(B));
                                        break;
                                    }

                                    break;
                                }
                            }
                        }

                        break;

                    //entry point at the top
                    case 2:
                        for (int k = 0; k < airspace.getListOfExitPoints().size(); k++) {
                            if (tempRoute.get(tempRoute.size() - 1) == airspace.getListOfExitPoints().get(k)) {
                                int randInt = new Random().nextInt(2);

                                switch (k) {
                                //top to left
                                case 1:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(A));
                                        tempRoute.add(2, tempListOfWaypoints.get(F));
                                        tempRoute.add(3, tempListOfWaypoints.get(J));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(E));
                                        tempRoute.add(3, tempListOfWaypoints.get(B));
                                        tempRoute.add(4, tempListOfWaypoints.get(J));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(F));
                                        tempRoute.add(2, tempListOfWaypoints.get(B));
                                        tempRoute.add(3, tempListOfWaypoints.get(A));
                                        tempRoute.add(4, tempListOfWaypoints.get(J));
                                        break;
                                    }

                                    break;

                                //top to right
                                case 2:
                                    switch (randInt) {
                                    //selects random flight plan
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(I));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(G));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(H));
                                        break;
                                    }

                                    break;

                                //top to runway
                                case 3:
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(I));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(H));
                                        tempRoute.add(3, tempListOfWaypoints.get(I));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(A));
                                        tempRoute.add(1, tempListOfWaypoints.get(J));
                                        tempRoute.add(2, tempListOfWaypoints.get(B));
                                        break;
                                    }

                                    break;
                                }
                            }
                        }

                        break;

                    //entry point on the runway
                    case 3:
                        for (int k = 0; k < airspace.getListOfExitPoints().size(); k++) {
                            if (tempRoute.get(tempRoute.size() - 1) == airspace.getListOfExitPoints().get(k)) {
                                int randInt = new Random().nextInt(2);

                                switch (k) {
                                //runway to top
                                case 0:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(F));
                                        tempRoute.add(1, tempListOfWaypoints.get(E));
                                        tempRoute.add(2, tempListOfWaypoints.get(D));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(F));
                                        tempRoute.add(1, tempListOfWaypoints.get(A));
                                        tempRoute.add(2, tempListOfWaypoints.get(J));
                                        tempRoute.add(3, tempListOfWaypoints.get(B));
                                        break;
                                    }

                                    break;

                                //runway to left
                                case 1:

                                    //selects random flight plan
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(F));
                                        tempRoute.add(1, tempListOfWaypoints.get(J));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(F));
                                        tempRoute.add(1, tempListOfWaypoints.get(A));
                                        tempRoute.add(2, tempListOfWaypoints.get(J));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(F));
                                        tempRoute.add(1, tempListOfWaypoints.get(B));
                                        tempRoute.add(2, tempListOfWaypoints.get(J));
                                        break;
                                    }

                                    break;

                                //runway to right
                                case 2:
                                    switch (randInt) {
                                    case 0:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(G));
                                        tempRoute.add(2, tempListOfWaypoints.get(H));
                                        break;

                                    case 1:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(I));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;

                                    case 2:
                                        tempRoute.add(0, tempListOfWaypoints.get(C));
                                        tempRoute.add(1, tempListOfWaypoints.get(D));
                                        tempRoute.add(2, tempListOfWaypoints.get(G));
                                        tempRoute.add(3, tempListOfWaypoints.get(H));
                                        break;
                                    }

                                    break;
                                }
                            }
                        }

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
        Random rand = new Random();

        if (entryPoint.isRunway()) {
            return 0;
        }

        int min = flight.getMinVelocity(),
            max = flight.getMaxVelocity();
        return (rand.nextInt(min) + (max - min));
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

    public void render(Graphics g, GameContainer gc) throws SlickException {
        if (this.currentRoute.size() > 0) {
            g.setColor(Color.lightGray);
            
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
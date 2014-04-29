package logicClasses;

import static java.lang.Math.PI;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;

public class Flight {

    // Images
    private static Image whiteFlightImage;
    private static Image redFlightImage;
    private static Image blueFlightImage;
    private static Image shadowImage;

    // Random number generator
    private static Random rand = new Random();

    // Constants
    private static final double GAME_SCALE = 1 / 1000.0;

    public static final int MIN_VELOCITY = 200; // mph
    public static final int MAX_VELOCITY = 400; // mph

    public static final int MIN_ALTITUDE = 1000; // ft
    public static final int MAX_ALTITUDE = 5000; // ft

    private final static int RADIUS = 30;

    private static final double ACCEL_RATE = 20 / 60.0;
    private static final double CLIMB_RATE = 60 / 60.0;
    private static final double TURN_RATE = 30 / 60.0;

    // Fields
    private String flightName;

    private double x = 0;
    private double y = 0;

    private double velocity, targetVelocity;
    private double currentHeading, targetHeading;
    private int currentAltitude, targetAltitude;

    private int timeToLand;

    private boolean turningRight = false;
    private boolean turningLeft = false;

    private Airspace airspace;
    private FlightPlan flightPlan;

    private int closestDistance = Integer.MAX_VALUE; // this is the maximum distance a plane
    // can be away from the waypoint once it has
    // been checked that the plane is inside the waypoint
    private int distanceFromWaypoint;

    // All three variables are used to control the flight state
    private boolean takingOff = false;
    private boolean landing = false;
    private boolean removeMe = false;

    private String owner;

    // Constructor
    public Flight(Airspace airspace) {
        this.airspace = airspace;
        this.flightPlan = new FlightPlan(airspace, this);
        this.currentAltitude = generateAltitude();
        this.targetAltitude = this.currentAltitude;
    }

    // METHODS

    /**
     * init: initialises resources such as images.
     */

    public static void init() throws SlickException {
        LoadingList loading = LoadingList.get();

        if (whiteFlightImage == null) {
            loading.add(new DeferredFile("res/graphics/flight.png") {
                public void loadFile(String filename) throws SlickException {
                    whiteFlightImage = new Image(filename);
                }
            });
        }

        if (shadowImage == null) {
            loading.add(new DeferredFile("res/graphics/flight_shadow.png") {
                public void loadFile(String filename) throws SlickException {
                    shadowImage = new Image(filename);
                }
            });
        }

        if (redFlightImage == null) {
            loading.add(new DeferredFile("res/graphics/flight_red.png") {
                public void loadFile(String filename) throws SlickException {
                    redFlightImage = new Image(filename);
                }
            });
        }

        if (blueFlightImage == null) {
            loading.add(new DeferredFile("res/graphics/flight_blue.png") {
                public void loadFile(String filename) throws SlickException {
                    blueFlightImage = new Image(filename);
                }
            });
        }
    }

    /**
     * generateAltitude: Randomly assigns one of three different altitudes to a flight
     * @return A random altitude between MIN_ALTITUDE and MAX_ALTITUDE
     */

    public int generateAltitude() {
        if (getFlightPlan().getEntryPoint().isRunway()) {
            return 0;
        }

        int check = rand.nextInt(((MAX_ALTITUDE - MIN_ALTITUDE) / 1000) - 2);
        return MIN_ALTITUDE + (check + 1) * 1000;
    }

    /**
     * calculateHeadingToFirstWaypoint: calculates heading between flight's current position and the first waypoint
     * in the flight's plan. The flight's current position will always be it's entrypoint because this method
     * is only called within the newFlight() function in airspace.
     * @param desX - The X coordinate of the waypoint
     * @param dexY - The Y coordinate of the waypoint
     * @return The heading between the flight and first waypoint.
     */

    public double calculateHeadingToFirstWaypoint(double desX, double desY) {
        double angle = Math.toDegrees(Math.atan2(desY - this.y, desX - this.x)) + 90;

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    /**
     * turnFlightLeft: sets the target heading to certain amount to to the left of the flight's current heading.
     * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightLeft. The
     * turningLeft boolean is set in order to tell the updateCurrentHeading() method that the flight should turn left
     * towards it's target heading.
     * @param degreeTurnedBy - The amount of degrees you want to turn left by.
     */

    public void turnFlightLeft(int degreeTurnedBy) {
        this.turningRight = false;
        this.turningLeft = true;
        this.targetHeading = Math.round(this.currentHeading) - degreeTurnedBy;

        if (this.targetHeading < 0) {
            this.targetHeading = 360 + this.targetHeading;
        }
    }

    /**
     * turnFlightRight: sets the target heading to certain amount to to the right of the flight's current heading.
     * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightRight. The
     * turningRight boolean is set in order to tell the updateCurrentHeading() method that the flight should turn right
     * towards it's target heading.
     * @param degreeTurnedBy - The amount of degrees you want to turn right by.
     */

    public void turnFlightRight(int degreeTurnedBy) {
        this.turningLeft = false;
        this.turningRight = true;
        this.targetHeading = Math.round(this.currentHeading) + degreeTurnedBy;

        if (this.targetHeading >= 360) {
            this.targetHeading = this.targetHeading - 360;
        }
    }

    /**
     * giveHeading: Changes the target heading to newHeading. Whenever a command is issued by the user to change the heading,
     * the method is passed the value of that command. The heading is always adjusted to a value between 0 and 359. This is
     * done using newHeading % 360.
     * @param newHeading - The heading the flight has been commmanded to fly at.
     */

    public void giveHeading(int newHeading) {
        this.turningRight = false;
        this.turningLeft = false;
        newHeading = newHeading % 360;
        this.targetHeading = newHeading;
    }


    public int minDistanceFromWaypoint(Point waypoint) {
        return closestDistance;
    }

    public void resetMinDistanceFromWaypoint() {
        closestDistance = Integer.MAX_VALUE;
    }

    public void takeOff() {
        takingOff = true;
        setTargetVelocity((MIN_VELOCITY + MAX_VELOCITY) / 2);
        setTargetAltitude(MIN_ALTITUDE);
    }

    public void land() {
        if (!landing && this.checkHeading()) { //Checks if a flight is meant to land, if it is landing already and its heading.
            for (int i = 0; i < this.airspace.getAirport().size(); i++) { //Checks if the flight is at a runway.
                // (Checking a particular runway (red or blue ariport) is pointless, since we already have the heading.
                if (this.checkIfAtAirport(this.airspace.getAirport().get(i))) {
                    Airport airport = this.airspace.getAirport().get(i);
                    landing = true; //Set state to landing
                    setTargetVelocity(0);
                    setTargetAltitude(0);
                    if (withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading(), 10)) {
                        this.setCurrentHeading(airport.getRunwayHeading());
                        this.setTargetHeading(this.getCurrentHeading());
                    }
                    else if (withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading() + 180, 10)) {
                        this.setCurrentHeading(airport.getRunwayHeading() + 180);
                        this.setTargetHeading(this.getCurrentHeading());
                    }
                    else if (withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading() - 180, 10)) {
                        this.setCurrentHeading(airport.getRunwayHeading() - 180);
                        this.setTargetHeading(this.getCurrentHeading());
                    }


                    this.timeToLand = 600; //Set game update cycles until the flight lands. Roughly 10 seconds.
                }
            }
        }
    }
    
    /**
     * handOver: Transfers the plane to the control of the other player. Inoperative in single-player mode.
     * @param st: The ScoreTracking object associated with the flight's (original) owner, 
     * so we can conveniently apply a score penalty intended to discourage handover-spamming.
     */

    public void handOver(ScoreTracking st) {
        if (owner == "red" && airspace.getHandoverDelay() == false) {
            owner = "blue";
            airspace.resetLoopsSinceLastHandover();
            airspace.setHandoverDelay();
            st.applyFlightLossPenalty();
        }
        
        else if (owner == "blue" && airspace.getHandoverDelay() == false) {
            owner = "red";
            airspace.resetLoopsSinceLastHandover();
            airspace.setHandoverDelay();
            st.applyFlightLossPenalty();
        }
    }

    // UPDATE METHODS

    /**
     * updateXYCoordinates: updates the x and y values of the plane depending on it's velocity
     * and it's current heading. The velocity of the plane is scaled so that it can be used for
     * movement in terms of pixels.
     */

    public void updateXYCoordinates() {
        double vs = velocity * GAME_SCALE;
        this.x += vs * Math.sin(Math.toRadians(currentHeading));
        this.y -= vs * Math.cos(Math.toRadians(currentHeading));
    }

    /**
     * updateAltitude(): If the target altitude is higher than the current altitude, increase current altitude.
     * If target altitude is less than current altitude, decrease current altitude. If current altitude and
     * target altitude are the same, do nothing.
     */

    public void updateAltitude() {
        if (this.currentAltitude > this.targetAltitude && !takingOff) {
            this.currentAltitude -= CLIMB_RATE;
        }

        else if (this.currentAltitude < this.targetAltitude && !takingOff) {
            this.currentAltitude += CLIMB_RATE;
        }
    }

    /**
     * updateCurrentHeading(): Moves the current heading towards the target heading. If a user has issued
     * a heading but not specified what way to turn, this method will determine what way it would be quicker
     * to turn towards it's target heading.
     */

    public void updateCurrentHeading() {
        if (Math.round(this.targetHeading) != Math.round(this.currentHeading)) {
            /*
             * If plane has been given a heading so no turning direction specified,
             * below works out whether it should turn left or right to that heading
             */
            if (!this.turningRight && !this.turningLeft) {
                if (Math.abs(this.targetHeading - this.currentHeading) == 180) {
                    this.turningRight = true;
                }

                else if (this.currentHeading + 180 <= 359) {
                    if (this.targetHeading < this.currentHeading + 180 && this.targetHeading > this.currentHeading) {
                        this.turningRight = true;
                    }

                    else {
                        this.turningLeft = true;
                    }
                }

                else {
                    if (this.targetHeading > this.currentHeading - 180 && this.targetHeading < this.currentHeading) {
                        this.turningLeft = true;
                    }

                    else {
                        this.turningRight = true;
                    }
                }
            }

            // If plane is already turning right or user has told it to turn right
            if (this.turningRight) {
                this.currentHeading += TURN_RATE;

                if (Math.round(this.currentHeading) >= 360 && this.targetHeading != 360) {
                    this.currentHeading = 0;
                }
            }

            // If plane is already turning left or user has told it to turn left
            if (this.turningLeft) {
                this.currentHeading -= TURN_RATE;

                if (Math.round(this.currentHeading) <= 0 && this.targetHeading != 0) {
                    this.currentHeading = 360;
                }
            }
        }
    }

    public void updateVelocity() {
        double dv = 0.01 * (targetVelocity - velocity);

        if (targetVelocity > velocity) {
            dv = Math.min(dv , ACCEL_RATE);
        }

        else {
            dv = Math.max(dv, -ACCEL_RATE);
        }

        velocity += dv;

        if (Math.abs(targetVelocity - velocity) < 0.5) {
            velocity = targetVelocity;
        }

        if (takingOff && (Math.abs(MIN_VELOCITY - velocity) < 0.5)) {
            takingOff = false;
        }
    }


    // UPDATE, RENDER



    /**
     * Update: calls all the update functions.
     */

    public void update(ScoreTracking score) {
        this.updateVelocity();
        this.updateCurrentHeading();
        this.updateXYCoordinates();
        this.updateAltitude();
        this.flightPlan.update(score);

        if (landing) {
            if (this.timeToLand != 0) {
                this.timeToLand -= 1;
            }
            else {
                this.removeMe = true;
            }

        }
    }


    /**
     * render: draw's all elements of the flight and it's information.
     * @param g - Graphics libraries required by Slick2D.
     */

    public void render(Graphics g) throws SlickException {
        float shadowScale = (float)(36 - (this.currentAltitude / 1000)) / 10;  // Scale the shadow in line with the flight's altitude
        shadowImage.setRotation((int) currentHeading);
        shadowImage.draw((int) this.x - 35, (int) this.y, shadowScale);

        if (this.owner == "red") {
            redFlightImage.setRotation((int) currentHeading);
            redFlightImage.draw((int) this.x - 10, (int) this.y - 10);
        }

        else if (this.owner == "blue") {
            blueFlightImage.setRotation((int) currentHeading);
            blueFlightImage.draw((int) this.x - 10, (int) this.y - 10);
        }

        else {
            whiteFlightImage.setRotation((int) currentHeading);
            whiteFlightImage.draw((int) this.x - 10, (int) this.y - 10);
        }

        // Drawing information around flight, first the next waypoint, then the flight's altitude, then the flight's speed
        g.setColor(Color.white);

        if (this.flightPlan.getCurrentRoute().size() > 0) {
            g.drawString("Aim: " + this.flightPlan.getPointByIndex(0).getPointRef(), (int)this.x + 18, (int)this.y - 27);
        }

        g.drawString(currentAltitude + "ft", (int)this.x + 17, (int)this.y - 9);

        g.drawString(Math.round(this.velocity) + "mph", (int)this.x + 17, (int)this.y + 9);
    }

    private Airport matchAirport(double pointX) {
        if (pointX == this.airspace.getAirport().get(0).getX()) {
            return this.airspace.getAirport().get(0);
        }
        else {
            return this.airspace.getAirport().get(1);
        }
    }


    // =================
    // # Utility Methods
    // =================

    public static boolean withinTolerance(double x1, double x2, double tolerance) {
        return Math.abs(x1 - x2) <= tolerance;
    }


    public boolean checkHeading() { //Method to check if the flight is heading parallel to a runway.
        for (int i = 0; i < this.airspace.getAirport().size(); i++) { //Checks if the flight is at a runway.
            Airport airport = this.airspace.getAirport().get(i);
            if (withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading(), 10) || //Do checks for both runway
                    withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading() + 180, 10) || //directions.
                    withinTolerance(this.getCurrentHeading(), airport.getRunwayHeading() - 180, 10)) {
                return true;
            }
        }
        return false;
    }


    /**
     * checkIfFlightAtWaypoint: checks whether a flight is close enough to the next waypoint in it's plan
     * for it to be considered at that waypoint. Update the closestDistance so that it knows how close the plane
     * was from the waypoint when it leaves the waypoint. This is so the score can be updated correctly
     * @param Waypoint - The next waypoint in the flight's plan.
     * @return True if flight is at it's next waypoint and it is moving away from that waypoint.
     */

    public boolean checkIfFlightAtWaypoint(Point waypoint) {
        int distanceX;
        int distanceY;
        distanceX = (int)(Math.abs(Math.round(this.x) - Math.round(waypoint.getX())));
        distanceY = (int)(Math.abs(Math.round(this.y) - Math.round(waypoint.getY())));
        distanceFromWaypoint = (int)Math.sqrt((int)Math.pow(distanceX, 2) + (int)Math.pow(distanceY, 2));

        // The plane is coming towards the waypoint
        if (closestDistance > distanceFromWaypoint) {
            closestDistance = distanceFromWaypoint;
        }

        if ((distanceX <= RADIUS) && (distanceY <= RADIUS)) {
            // The plane is going away from the way point
            if (closestDistance < distanceFromWaypoint) {
                if (waypoint instanceof ExitPoint) {
                    if (((ExitPoint)waypoint).isRunway()) {
                        return currentAltitude == 0;
                    }

                    else {
                        return true;
                    }
                }

                //for any non-exit waypoint
                return true;
            }
        }

        //getting closer OR not close enough
        return false;
    }


    /**
     * checkIfAtAirport: Checks if the flight is in the relative area of the airport. Used for landing.
     * Since an airport is a very specific type of a point, we cannot use the stock checkIfAtWaypoint method.
     * @param airport - the airport that we are checking.
     * @return true if the flight is in the airport area. False otherwise.
     */

    public boolean checkIfAtAirport(Airport airport) {
        if (airport.getName() == "Red airport") {
            if (((Math.abs(Math.round(this.x) - Math.round(airport.getX()))) <= 150)
                    && (Math.abs(Math.round(this.y) - Math.round(airport.getY()))) <= 15) {
                return true;
            }
        }

        if (airport.getName() == "Blue airport") {
            if (((Math.abs(Math.round(this.x) - Math.round(airport.getX()))) <= 15)
                    && (Math.abs(Math.round(this.y) - Math.round(airport.getY()))) <= 150) {
                return true;
            }
        }


        return false;
    }


    // ========================
    // # Mutators and Accessors
    // ========================

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCurrentHeading() {
        return this.currentHeading;
    }

    public void setCurrentHeading(double currentHeading) {
        this.currentHeading = currentHeading;
    }

    public double getTargetHeading() {
        return this.targetHeading;
    }

    public void setTargetHeading(double targetHeading) {
        this.targetHeading = targetHeading;
    }

    public int getTargetAltitude() {
        return this.targetAltitude;
    }

    public void setTargetAltitude(int targetAltitude) {
        this.targetAltitude = targetAltitude;
    }

    public int getAltitude() {
        return this.currentAltitude;
    }

    public void setAltitude(int altitude) {
        this.currentAltitude = altitude;
    }

    public boolean isGrounded() {
        return getAltitude() == 0;
    }

    public boolean isCommandable() {
        return !isGrounded() && !landing;
    }

    public boolean getTurningRight() {
        return this.turningRight;
    }

    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }

    public boolean getTurningLeft() {
        return this.turningLeft;
    }

    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    // toString function to display a flight object so we can read it
    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y + " Flight Name: " + this.flightName;
    }

    public int getCurrentAltitude() {
        return currentAltitude;
    }

    public boolean getRemove() {
        return this.removeMe;
    }

    public void setRemove(boolean b) {
        this.removeMe = b;
    }

    public void setCurrentAltitude(int currentAltitude) {
        this.currentAltitude = currentAltitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setTargetVelocity(double velocity) {
        this.targetVelocity = velocity;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public Airspace getAirspace() {
        return airspace;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}

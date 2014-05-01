package logicClasses;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;

import static logicClasses.Airspace.normalizeAngle;


public class Flight {

    // Images
    private static Image whiteFlightImage;
    private static Image redFlightImage;
    private static Image blueFlightImage;
    private static Image shadowImage;

    // Random number generator
    private static final Random rand = new Random();

    // Constants
    private static final double GAME_SCALE = 1 / 1000.0;

    public static final int MIN_VELOCITY = 200; // mph
    public static final int MAX_VELOCITY = 400; // mph
    private static final int STALL_VELOCITY = 80; // mph

    public static final int MIN_ALTITUDE = 1000; // ft
    public static final int MAX_ALTITUDE = 5000; // ft

    private static final int RADIUS = 30;

    private static final double ACCEL_RATE = 20 / 60.0;
    private static final double CLIMB_RATE = 60 / 60.0;
    private static final double TURN_RATE = 30 / 60.0;

    // Fields
    private double x = 0;
    private double y = 0;

    private double velocity; 
    private double targetVelocity;
    
    private double currentHeading;
    private double targetHeading;
    
    private int currentAltitude;
    private int targetAltitude;

    private boolean turningRight = false;
    private boolean turningLeft = false;

    private Airspace airspace;
    private FlightPlan flightPlan;

    // This is the maximum distance a plane can be away from the waypoint once it has
    // been checked that the plane is inside the waypoint
    private int closestDistance = Integer.MAX_VALUE; 
    
    private int distanceFromWaypoint;

    // These two variables are used to control the flight state
    private boolean takingOff = false;
    private boolean landing = false;

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
    
    
    // ==========================================
    // # Methods dealing with flight construction
    // ==========================================

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
        return normalizeAngle(Math.toDegrees(Math.atan2(desY - y, desX - x)) + 90);
    }

    
    // ====================================================
    // # Methods dealing with changes to a flight's heading
    // ====================================================

    /**
     * turnFlightLeft: sets the target heading to certain amount to to the left of the flight's current heading.
     * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightLeft. The
     * turningLeft boolean is set in order to tell the updateCurrentHeading() method that the flight should turn left
     * towards it's target heading.
     * @param degreeTurnedBy - The amount of degrees you want to turn left by.
     */

    public void turnFlightLeft(int degreeTurnedBy) {
        turningRight = false;
        turningLeft = true;
        targetHeading = normalizeAngle(Math.round(this.currentHeading) - degreeTurnedBy);
    }

    /**
     * turnFlightRight: sets the target heading to certain amount to to the right of the flight's current heading.
     * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightRight. The
     * turningRight boolean is set in order to tell the updateCurrentHeading() method that the flight should turn right
     * towards it's target heading.
     * @param degreeTurnedBy - The amount of degrees you want to turn right by.
     */

    public void turnFlightRight(int degreeTurnedBy) {
        turningLeft = false;
        turningRight = true;
        targetHeading = normalizeAngle(Math.round(currentHeading) + degreeTurnedBy);
    }

    /**
     * giveHeading: Changes the target heading to newHeading, 
     * while also ensuring the flight takes the quickest course to that heading
     * @param newHeading The heading the flight has been commmanded to fly at
     */

    public void giveHeading(int newHeading) {
        // Clearing these variables ensures that the flight does not circle round to its new heading
        // but always takes the quickest route
        turningRight = false;
        turningLeft = false;
        targetHeading = normalizeAngle(newHeading);
    }


    // =============================================
    // # Methods dealing with taking-off and landing
    // =============================================

    public void takeOff() {
        takingOff = true;
        setTargetVelocity((MIN_VELOCITY + MAX_VELOCITY) / 2);
        setTargetAltitude(MIN_ALTITUDE);
    }
    
    public void land() {
        if (!landing && currentAltitude <= MIN_ALTITUDE && velocity <= MIN_VELOCITY) {
            for (Airport a : airspace.getListOfAirports()) {
                if (checkIfAtAirport(a) && checkHeadingCorrectForAirport(a)) {
                    // If we have both the correct heading and location, begin the landing procedure
                    landing = true;
                    setTargetAltitude(0);
                    
                    // Set our target speed to touchdown velocity
                    setTargetVelocity(STALL_VELOCITY);
                    
                    // Adjust our heading to line up with the runway even more precisely                    
                    if (withinTolerance(currentHeading, a.getRunwayHeading(), 10)) {
                        giveHeading(a.getRunwayHeading());
                    }
                    
                    else {
                        giveHeading(a.getInverseRunwayHeading());
                    }
                                        
                    // There's no point checking any other airports, if we're already landing at one
                    break;
                }
            }
        }
    }
    
    /**
     * checkHeadingCorrectForAirport: Returns true if the heading is within 10 degrees of the runway heading or its inverse
     * @param a The airport in question
     */
    
    private boolean checkHeadingCorrectForAirport(Airport a) { 
        if (withinTolerance(currentHeading, a.getRunwayHeading(), 10) 
            || withinTolerance(currentHeading, a.getInverseRunwayHeading(), 10)) {
                return true;
        }
        
        return false;
    }
    
    /**
     * checkIfAtAirport: Returns true if the flight is over the runway
     * @param airport The airport in question
     */

    private boolean checkIfAtAirport(Airport airport) {
        if (airport.getRunwayHeading() == 90) {
            if (((Math.abs(Math.round(this.x) - Math.round(airport.getX()))) <= 150)
                    && (Math.abs(Math.round(this.y) - Math.round(airport.getY()))) <= 15) {
                return true;
            }
        }

        if (airport.getRunwayHeading() == 0) {
            if (((Math.abs(Math.round(this.x) - Math.round(airport.getX()))) <= 15)
                    && (Math.abs(Math.round(this.y) - Math.round(airport.getY()))) <= 150) {
                return true;
            }
        }

        return false;
    }
    
    private static boolean withinTolerance(double a, double b, double tolerance) {        
        // Account for the fact that values may be either side of due North (e.g. a = 5 and b = 355)
        if (Math.abs(a - b) <= tolerance || 
            Math.abs((a - 360) - b) <= tolerance || 
            Math.abs((a + 360) - b) <= tolerance) {
            return true;
        }
        
        return false;
    }
    
    public void abortLanding() {
        if (landing) {
            landing = false;
            
            if ((currentAltitude < MIN_ALTITUDE && targetAltitude < MIN_ALTITUDE) || targetAltitude < MIN_ALTITUDE) {
                targetAltitude = MIN_ALTITUDE;
            }
            
            if ((velocity < MIN_VELOCITY && targetVelocity < MIN_VELOCITY) || targetVelocity < MIN_VELOCITY) {
                targetVelocity = MIN_VELOCITY;
            }
        }
    }
            
    /**
     * handOver: Transfers the plane to the control of the other player. Inoperative in single-player mode.
     * @param st: The ScoreTracking object associated with the flight's (original) owner, 
     * so we can conveniently apply a score penalty intended to discourage handover-spamming.
     */

    public void handOver(ScoreTracking st) {
        if (owner == "red" && airspace.getHandoverDelayRed() == false) {
            owner = "blue";
            airspace.resetLoopsSinceLastHandoverRed();
            airspace.setHandoverDelayRed();
            st.applyFlightLossPenalty();
        }
        
        else if (owner == "blue" && airspace.getHandoverDelayBlue() == false) {
            owner = "red";
            airspace.resetLoopsSinceLastHandoverBlue();
            airspace.setHandoverDelayBlue();
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
        x += velocity * Math.sin(Math.toRadians(currentHeading)) * GAME_SCALE;
        y -= velocity * Math.cos(Math.toRadians(currentHeading)) * GAME_SCALE;
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
            dv = Math.min(dv, ACCEL_RATE);
        }

        else {
            dv = Math.max(dv, -ACCEL_RATE);
        }

        velocity += dv;

        if (Math.abs(targetVelocity - velocity) < 0.5) {
            velocity = targetVelocity;
        }

        // If the flight's reached its minimum airborne velocity, then set takingOff to false, 
        // so the updateAltitude method can lift it up off the runway
        if (takingOff && velocity > STALL_VELOCITY) {
            takingOff = false;
        }
        
        // If the flight's landed successfully, then it can decelerate to a stop,
        // at which point it will then be removed by the airspace
        if (landing && isGrounded() && targetVelocity != 0) {
            targetVelocity = 0;
        } 
    }


    /**
     * Update: calls all the update functions.
     */

    public void update(ScoreTracking score) {
        updateVelocity();
        updateCurrentHeading();
        updateXYCoordinates();
        updateAltitude();
        flightPlan.update(score);
    }


    /**
     * render: draw's all elements of the flight and it's information.
     * @param g - Graphics libraries required by Slick2D.
     */

    public void render(Graphics g) throws SlickException {

        // Draw the shadow image, scaled in line with the flight's altitude
        float shadowScale = (float)(36 - (currentAltitude / 1000)) / 10;
        shadowImage.setRotation((int) currentHeading);
        shadowImage.draw((int)x - 35, (int)y, shadowScale);
        
        // Draw the correct colour-coded image, depending on the owner of the flight
        if (owner == "red") {
            redFlightImage.setRotation((int) currentHeading);
            redFlightImage.draw((int)x - 10, (int)y - 10);
        }

        else if (owner == "blue") {
            blueFlightImage.setRotation((int) currentHeading);
            blueFlightImage.draw((int)x - 10, (int)y - 10);
        }

        else {
            whiteFlightImage.setRotation((int) currentHeading);
            whiteFlightImage.draw((int)x - 10, (int)y - 10);
        }


        // Drawing information around flight, first the next waypoint, then the flight's altitude, then the flight's speed
        g.setColor(Color.white);

        if (!flightPlan.getCurrentRoute().isEmpty()) {
            g.drawString("Aim: " + flightPlan.getPointByIndex(0).getPointRef(), (int)x + 18, (int)y - 27);
        }

        g.drawString(currentAltitude + "ft", (int)x + 17, (int)y - 9);

        g.drawString(Math.round(velocity) + "mph", (int)x + 17, (int)y + 9);
        
    }
    

    // =================
    // # Utility Methods
    // =================

    /**
     * checkIfFlightAtWaypoint: checks whether a flight is close enough to the next waypoint in it's plan
     * for it to be considered at that waypoint. Update the closestDistance so that it knows how close the plane
     * was from the waypoint when it leaves the waypoint. This is so the score can be updated correctly
     * @param Waypoint - The next waypoint in the flight's plan.
     * @return True if flight is at it's next waypoint and it is moving away from that waypoint.
     */

    public boolean checkIfFlightAtWaypoint(Point waypoint) {
        // Calculate the distance between the flight and the waypoint
        distanceFromWaypoint = (int)Math.hypot(x - waypoint.getX(), y - waypoint.getY());

        if (closestDistance > distanceFromWaypoint) {
            // The plane is coming towards the waypoint
            closestDistance = distanceFromWaypoint;
        }

        if (distanceFromWaypoint <= RADIUS) {
            // The plane is going away from the waypoint
            if (closestDistance < distanceFromWaypoint) {
                if (waypoint instanceof ExitPoint && ((ExitPoint)waypoint).isRunway()) {
                    return currentAltitude == 0;
                }

                return true; // for any non-exit waypoint
            }
        }
        
        return false; // getting closer or not close enough
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
    
    public boolean isLanding() {
        return landing;
    }

    public boolean getTurningRight() {
        return this.turningRight;
    }

    public boolean getTurningLeft() {
        return this.turningLeft;
    }
    
    public int getClosestDistanceFromWaypoint() {
        return closestDistance;
    }

    public void resetClosestDistanceFromWaypoint() {
        closestDistance = Integer.MAX_VALUE;
    }

    // toString function to display a flight object so we can read it
    @Override
    public String toString() {
        return "Flight at (" + x + ", " + y + ")";
    }

    public int getCurrentAltitude() {
        return currentAltitude;
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

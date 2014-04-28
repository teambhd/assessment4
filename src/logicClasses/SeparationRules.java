package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public class SeparationRules {

    // Constants
    private static final int LATERAL_WARNING_DISTANCE = 100;
    private static final int VERTICAL_WARNING_DISTANCE = 999;

    // Fields
    private int gameOverLateralSeparation;
    private int gameOverVerticalSeparation;
    
    private boolean gameOverViolation = false;
    
    private Flight violatingFlight1;
    private Flight violatingFlight2;

    // Constructor
    public SeparationRules(int difficultyVal) {
        if (difficultyVal == states.DifficultyState.EASY) { 
            // Easy: Only a Crash will cause a Game Over
            this.gameOverLateralSeparation = 30;
            this.gameOverVerticalSeparation = 200;
        }

        if (difficultyVal == states.DifficultyState.MEDIUM) { 
            // Medium: Can Violate, but not too closely
            this.gameOverLateralSeparation = 60;
            this.gameOverVerticalSeparation = 350;
        }

        if (difficultyVal == states.DifficultyState.HARD) { 
            // Hard: Minimal Warning Violation allowed before end game achieved.
            this.gameOverLateralSeparation = 90;
            this.gameOverVerticalSeparation = 500;
        }
    }

    // Methods

    /**
     * lateralDistanceBetweenFlights: Calculates the lateral distance between two flights.
     * @param f1 - A flight from the airspace.
     * @param f2 - A flight from the airspace.
     * @return A double representing the lateral distance between the two flights passed as parameters.
     */

    public double lateralDistanceBetweenFlights(Flight f1, Flight f2) {
        return Math.hypot(f1.getX() - f2.getX(), f1.getY() - f2.getY());
    }

    /**
     * verticalDistanceBetweenFlights: Calculates the vertical distance between two flights.
     * @param f1 - A flight from the airspace.
     * @param f2 - A flight from the airspace.
     * @return An int representing the vertical distance between the two flights passed as parameters.
     */

    public int verticalDistanceBetweenFlights(Flight f1, Flight f2) {
        return Math.abs(f1.getAltitude() - f2.getAltitude());
    }

    /**
     * checkViolation: Checks whether two flights have crashed and, if so, updates gameOverViolation 
     * and violatingFlight1 and 2 accordingly. Assumes that only one crash is happening exactly simultaneously, 
     * which is not too unreasonable given that the function runs 60 times a second.
     * @param airspace - The airspace object is passed so as to allow access to the list of flights.
     */

    public void checkViolation(Airspace airspace) {
        for (Flight e : airspace.getListOfFlights()) {
            for (Flight f : airspace.getListOfFlights()) {
                
                if (e == f) {
                    continue;
                }
                
                if (lateralDistanceBetweenFlights(e, f) <= gameOverLateralSeparation &&
                    verticalDistanceBetweenFlights(e, f) <= gameOverVerticalSeparation) {
                        gameOverViolation = true;
                        violatingFlight1 = e;
                        violatingFlight2 = f;
                }
                
            }
        }   
    }

    /**
     * render: This calculates whether any flights in the airspace are breaking warning separation rules
     * If two flight are breaking warning separation rules, a line is drawn between them.
     * @param g - Graphics libraries required by Slick2D.
     * @param airspace - The airspace object is passed so as to allow access to the list of flights.
     */

    public void render(Graphics g, Airspace airspace) {
        for (Flight e : airspace.getListOfFlights()) {
            for (Flight f : airspace.getListOfFlights()) {
                
                if (e == f) {
                    continue;
                }
                
                if (lateralDistanceBetweenFlights(e, f) <= LATERAL_WARNING_DISTANCE &&
                    verticalDistanceBetweenFlights(e, f) <= VERTICAL_WARNING_DISTANCE) {
                        g.setColor(Color.orange);
                        g.setLineWidth(2);
                        g.drawLine((float)e.getX(), (float)e.getY(), (float)f.getX(), (float)f.getY());
                        g.setLineWidth(1);
                }
                
            }
        }
    }

    /**
     * update: This calls the checkViolation method to detect whether the game over separation rules
     * have been breached.
     * @param airspace - The airspace object is passed as the checkViolation method requires knowledge of
     * flights in the airspace, which is stored within the airspace.
     */

    public void update(Airspace airspace) {
        this.checkViolation(airspace);
    }


    // Mutators and Accessors
    public void setGameOverViolation(boolean gameOverViolation) {
        this.gameOverViolation = gameOverViolation;
    }

    public boolean getGameOverViolation() {
        return this.gameOverViolation;
    }
    
    public Flight getViolatingFlight1() {
        return this.violatingFlight1;
    }
    
    public Flight getViolatingFlight2() {
        return this.violatingFlight2;
    }

}


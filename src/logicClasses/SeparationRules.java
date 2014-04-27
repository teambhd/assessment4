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

    //CONSTRUCTOR
    public SeparationRules(int difficultyVal) {
        if (difficultyVal == 1) { 
            // Easy: Only a Crash will cause a Game Over
            this.gameOverLateralSeparation = 30;
            this.gameOverVerticalSeparation = 200;
        }

        if (difficultyVal == 2) { 
            // Medium: Can Violate, but not too closely
            this.gameOverLateralSeparation = 60;
            this.gameOverVerticalSeparation = 350;
        }

        if (difficultyVal == 3) { 
            // Hard: Minimal Warning Violation allowed before end game achieved.
            this.gameOverLateralSeparation = 90;
            this.gameOverVerticalSeparation = 500;
        }
    }

    // Methods

    /**
     * lateralDistanceBetweenFlights: Calculates the lateral distance between two flights.
     * @param flight1 - A flight from the airspace.
     * @param flight2 - A flight from the airspace.
     * @return A double representing the lateral distance between the two flights passed as parameters.
     */

    public double lateralDistanceBetweenFlights(Flight flight1, Flight flight2) {
        return Math.sqrt(Math.pow((flight1.getX() - flight2.getX()), 2) + Math.pow((flight1.getY() - flight2.getY()), 2));
    }

    /**
     * verticalDistanceBetweenFlights: Calculates the vertical distance between two flights.
     * @param flight1 - A flight from the airspace.
     * @param flight2 - A flight from the airspace.
     * @return An int representing the vertical distance between the two flights passed as parameters.
     */

    public int verticalDistanceBetweenFlights(Flight flight1, Flight flight2) {
        return Math.abs(flight1.getAltitude() - flight2.getAltitude());
    }

    /**
     * checkViolation: Calculates whether two flights have breached the game over separation rules.
     * @param airspace - The airspace object is passed as the checkViolation() method requires knowledge of
     * flights in the airspace, which is stored within the airspace.
     */

    public void checkViolation(Airspace airspace) {
        for (int i = 0; i < airspace.getListOfFlights().size(); i++) {
            for (int j = i + 1; j < airspace.getListOfFlights().size(); j++) {
                if ((lateralDistanceBetweenFlights(airspace.getListOfFlights().get(i), airspace.getListOfFlights().get(j)) < this.gameOverLateralSeparation)) {
                    if ((verticalDistanceBetweenFlights(airspace.getListOfFlights().get(i), airspace.getListOfFlights().get(j)) < this.gameOverVerticalSeparation)) {
                        this.gameOverViolation = true;
                        this.violatingFlight1 = airspace.getListOfFlights().get(i);
                        this.violatingFlight2 = airspace.getListOfFlights().get(j);
                    }
                }
            }
        }
    }

    /**
     * render: This calculates whether any flights in the airspace are breaking warning separation rules
     * If two flight are breaking warning separation rules, a line is drawn between them.
     * @param g - Graphics libraries required by slick2d.
     * @param airspace - The airspace object is passed as the render method requires knowledge of
     * flights in the airspace, which is stored within the airspace.
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


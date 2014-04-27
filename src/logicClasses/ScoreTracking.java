package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ScoreTracking {
    
    // Constants
    private static final int TIME_BONUS = 100;
    private static final int FLIGHT_LOSS_PENALTY = -50;

    
    // Fields
    private int currentScore = 0;


    // Constructor
    public ScoreTracking() {}

    
    // Methods
    public void render(Graphics g, int x, int y, Color color) throws SlickException {
        g.setColor(color);
        g.drawString("Score: " + String.valueOf(currentScore), x, y);
    }
    
    // Positive scoring
    public int updateWaypointScore(int closestDistance) {  
        int waypointScore = 0;
              
        if (closestDistance >= 0 && closestDistance <= 14) {    //checks to see if the plane is within 10 pixels
            waypointScore = 100;                                //if yes, the score given is 100 points
        }

        else if (closestDistance >= 15 && closestDistance <= 28) {
            waypointScore = 50;
        }

        else if (closestDistance >= 29 && closestDistance <= 42) {
            waypointScore = 20;
        }
        
        return waypointScore;
    }

    public int updateScore(int score) {
        return currentScore += score;
    }

    public int updateTimeScore() {
        return currentScore += TIME_BONUS;
    }

    // Negative Scoring
    public int reduceScoreOnFlightLost() {
        return currentScore += FLIGHT_LOSS_PENALTY;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public int getScore() {
        return currentScore;
    }

}
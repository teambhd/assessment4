package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ScoreTracking {

    private int currentScore = 0;
    private int waypointScore;
    
    private static final int TIMESCORE = 2;
    private static final int FLIGHTLOST = -50;


    // Constructor
    public ScoreTracking() {}

    
    // Methods
    public void render(Graphics g, int x, int y, Color color) throws SlickException {
        g.setColor(color);
        g.drawString("Score" + String.valueOf(currentScore), x, y);
    }
    
    // Positive scoring
    public int updateWaypointScore(int closestDistance) {
        if (closestDistance >= 0 && closestDistance <= 14) {    //checks to see if the plane is within 10 pixels
            waypointScore = 100;                                //if yes, the score given is 100 points
        }

        if (closestDistance >= 15 && closestDistance <= 28) {
            waypointScore = 50;
        }

        if (closestDistance >= 29 && closestDistance <= 42) {
            waypointScore = 20;
        }

        return waypointScore;
    }

    public int updateScore(int score) {
        return currentScore += score;
    }

    public int updateTimeScore() {
        return currentScore += TIMESCORE;
    }

    // Negative Scoring
    public int reduceScoreOnFlightLost() {
        return currentScore += FLIGHTLOST;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public int getScore() {
        return currentScore;
    }

}
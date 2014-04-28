package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ScoreTracking {

    // Constants
    private static final int TIME_BONUS = 100;
    private static final int FLIGHT_LOSS_PENALTY = -50;
    private static final int CRASH_PENALTY = -500;


    // Fields
    private int currentScore = 0;


    // Constructor
    public ScoreTracking() {}


    // Methods
    public void render(Graphics g, int x, int y, Color color) throws SlickException {
        // Draw the indicator, showing which player's score this represents.
        // It's a circle, filled with the player's colour and bordered in either white
        // or, if the player colour is white (in single-player) light grey.
        g.setColor(color);
        g.fillOval((float)x, (float)y + 4, 10, 10);

        if (color == Color.white) {
            g.setColor(Color.lightGray);
        }

        else {
            g.setColor(Color.white);
        }

        g.drawOval((float)x, (float)y + 4, 10, 10);

        // Draw the numeric score itself
        g.setColor(Color.white);
        g.drawString(String.valueOf(currentScore), x + 15, y);
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

    public void applyTimeBonus() {
        currentScore += TIME_BONUS;
    }


    // Negative Scoring
    public void applyFlightLossPenalty() {
        currentScore += FLIGHT_LOSS_PENALTY;
    }

    public void applyCrashPenalty() {
        currentScore += CRASH_PENALTY;
    }


    public int getScore() {
        return currentScore;
    }

    public int updateScore(int score) {
        return currentScore += score;
    }

    public void resetScore() {
        currentScore = 0;
    }

}
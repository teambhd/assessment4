package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Waypoint extends Point {
    
    private static final Color BACKGROUND_COLOR = new Color(138, 145, 145);
    private static final Color BORDER_COLOR = Color.lightGray;
    private static final Color TEXT_COLOR = Color.black;

    // Constructor
    public Waypoint(double xcoord, double ycoord, String name) {
        super(xcoord, ycoord, name);
    }

    /**
     * render: Draw this waypoint to the screen
     * @param g Slick2D graphics object
     * @throws SlickException Slick2d exception handler
     */

    public void render(Graphics g) throws SlickException {        
        // Draw the background colour for the waypoint indicator circle
        g.setColor(BACKGROUND_COLOR);
        g.fillOval((float)x - 9, (float)y - 9, 18, 18);

        // Draw the border around the waypoint indicator circle
        g.setColor(BORDER_COLOR);
        g.drawOval((float)x - 9, (float)y - 9, 18, 18);
        
        // Draw the waypoint letter
        g.setColor(TEXT_COLOR);
        g.drawString(pointRef, (int)x - 4, (int)y - 9);
    }

}

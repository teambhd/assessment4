package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class WindIndicator {

    private static Image windImage;

    public static void init() throws SlickException {
        if (windImage == null) {
            LoadingList.get().add(new DeferredFile("res/graphics/wind_indicator.png") {
                public void loadFile(String filename) throws SlickException {
                    windImage = new Image(filename);
                }
            });
        }
    }

    /**
	 * @uml.property  name="direction"
	 */
    private float direction;

    // Constructor
    public WindIndicator() {}

    public void render(Graphics g, int time) throws SlickException {
        // Update the arrow's rotation by a random amount
        this.direction += ((float)Math.cos(time / 2999.0) + (float)Math.sin(time / 1009.0)) / 3;

        // Draw the rotated image on the left-hand bottom corner of the screen
        windImage.setRotation(this.direction);
        windImage.draw(14, 550);

        // Also display the degree value of the wind direction (normalised between 0 and 360)
        int displayDirection = (int)Airspace.normalizeAngle(Math.round(this.direction));

        g.setColor(Color.white);
        g.drawString("Wind:", 60, 550);
        g.drawString(String.valueOf(displayDirection) + "\u00B0", 60, 568); // \u00B0 is the degree symbol
    }

}
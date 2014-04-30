package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class Airport extends Point {

    private static Image airportImage;
    
    private int runwayHeading;

    // Constructor
    Airport(double x, double y, int runwayHeading, String name) {
        super(x, y, name);
        this.runwayHeading = runwayHeading;
    }

    public static void init() throws SlickException {
        if (airportImage == null) {
            LoadingList.get().add(new DeferredFile("res/graphics/airport.png") {
                public void loadFile(String filename) throws SlickException {
                    airportImage = new Image(filename);
                }
            });
        }
    }

    public void render(Graphics g) throws SlickException {
        // Draw the airport image, centred on its co-ordinates
        airportImage.setRotation((int)runwayHeading);
        airportImage.drawCentered((int)x, (int)y);
    }

    @Override
    public String toString() {
        return "Airport " + this.pointRef + " located at (" + x + ", " + y + ")";
    }

    public float getRunwayHeading() {
        return (float)runwayHeading;
    }

}

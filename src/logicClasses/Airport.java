package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class Airport {

    private static Image airportImage;

    private float x;
    private float y;
    
    private float runwayHeading;

    private String name;


    // Constructor
    Airport(float x, float y, float runwayHeading, String name) {
        this.x = x;
        this.y = y;
        this.runwayHeading = runwayHeading;
        this.name = name;
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
        airportImage.drawCentered(x, y);
    }

    @Override
    public String toString() {
        return "Airport " + name + " located at (" + x + ", " + y + ")";
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRunwayHeading() {
        return runwayHeading;
    }

    public String getName() {
        return name;
    }

}

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

    private int runwayLength = 303; //Purpose?

    private String name;


    //CONSTRUCTOR
    Airport(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        if (name == "Blue airport") {
            this.runwayHeading = 0;
        }
        if (name == "Red airport") {
            this.runwayHeading = 90;
        }
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
        // Airport image centred in middle of airspace
        if (this.name == "Blue airport") {
            airportImage.setRotation(0);
        }
        else {
            airportImage.setRotation(90);
        }
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

    public int getRunwayLength() {
        return runwayLength;
    }

    public String getName() {
        return name;
    }

}

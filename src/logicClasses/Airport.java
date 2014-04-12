package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class Airport {

    private static Image airportImage;

    // TODO: these values need setting in the constructor so as to allow multiple independent airports
    private float x = 675;
    private float y = 300; 
    private float runwayHeading = 320;
    
    private int runwayLength = 303;
    

    //CONSTRUCTOR
    Airport() {}

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
        airportImage.setRotation(runwayHeading);
        airportImage.drawCentered(x, y);
    }

    @Override
    public String toString() {
        return "Airport located at (" + x + ", " + y + ")";
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

}

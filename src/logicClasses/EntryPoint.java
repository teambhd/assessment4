package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class EntryPoint extends Point {

    static Image entryPointTop, entryPointRight, entryPointLeft, entryPointRunway;
            
    public EntryPoint(double x, double y) {
        super(x, y);
    }
    
    /**
     * init: Initialises the variables and resources required for the EntryPoint object render (Sets EntryPoint Images)
     * @param gc Game container required by Slick2d
     * @throws SlickException
     */

    public static void init() throws SlickException {
        LoadingList loading = LoadingList.get();

        if (entryPointTop == null)
            loading.add(new DeferredFile("res/graphics/entrypoint.png") {
            public void loadFile(String filename) throws SlickException {
                entryPointTop = new Image(filename);
                entryPointRight = entryPointTop.copy();
                entryPointRight.setRotation(90);
                entryPointLeft = entryPointTop.copy();
                entryPointLeft.setRotation(270);
                entryPointRunway = entryPointTop.copy();
                entryPointRunway.setRotation(135);
            }
        });
    }
        
    /**
     * render: Render method for the EntryPoint object, position determines orientation of image
     * @param g Graphics required by Slick2d
     * @throws SlickException
     */

    public void render(Graphics g) throws SlickException {
        if (y == 0) {
            entryPointTop.draw((int)x - 20, (int)y);
        }

        else if (x == 0) {
            entryPointLeft.draw((int)x, (int)y - 20);
        }

        else if (x == stateContainer.Game.WIDTH) {
            entryPointRight.draw((int)x - 40, (int)y - 20);
        }

        else {
            entryPointRunway.draw((int)x - 20, (int)y - 20);
        }
    }
    
    /**
     * isRunway: Returns true if the EntryPoint is not on any of the screen edges, and therefore must be a runway
     */
    
    public boolean isRunway() {
        return x != 0 && x != stateContainer.Game.WIDTH && y != 0 && y != stateContainer.Game.HEIGHT;
    }

}

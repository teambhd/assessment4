package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class EntryPoint extends Point {

    private static Image entryPointTop;
    private static Image entryPointRight;
    private static Image entryPointLeft;

    // Constructor
    public EntryPoint(double x, double y) {
        super(x, y);
    }

    /**
     * init: Loads the images required to render the EntryPoint
     * @param gc Game container required by Slick2D
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
            }
        });
    }

    /**
     * render: Render method for the EntryPoint object, position determines orientation of image. 
     * EntryPoints attached to Airports are deliberately not drawn.
     * @param g Graphics required by Slick2D
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
    }

    /**
     * isRunway: Returns true if the EntryPoint is not on any of the screen edges, and therefore must be a runway
     */

    public boolean isRunway() {
        return x != 0 && x != stateContainer.Game.WIDTH && y != 0 && y != stateContainer.Game.HEIGHT;
    }

}

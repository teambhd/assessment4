package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class ExitPoint extends Point {

    private static Image exitPointTop;
    private static Image exitPointRight;
    private static Image exitPointLeft;

    public ExitPoint(double x, double y, String name) {
        super(x, y, name);
    }

    /**
     * init: Initialises the variables and resources required for the ExitPoint object render (Sets ExitPoint Images)
     * @throws SlickException
     */

    public static void init() throws SlickException {
        LoadingList loading = LoadingList.get();

        if (exitPointTop == null) {
            loading.add(new DeferredFile("res/graphics/exitpoint.png") {
                public void loadFile(String filename) throws SlickException {
                    exitPointTop = new Image(filename);
                    exitPointRight = exitPointTop.copy();
                    exitPointRight.setRotation(90);
                    exitPointLeft = exitPointTop.copy();
                    exitPointLeft.setRotation(270);
                }
            });
        }
    }

    /**
     * render: Render method for the ExitPoint object, position determines orientation of image and String of name
     * @param g Graphics required by Slick2d
     * @throws SlickException
     */

    public void render(Graphics g) throws SlickException {
        g.setColor(Color.white);

        if (y == 0) {
            exitPointTop.draw((int)x - 20, (int)y);
            g.drawString(pointRef, (int)x - 15, (int)y);
        }

        else if (x == 0) {
            exitPointLeft.draw((int)x, (int)y - 20);
            g.drawString(pointRef, (int)x, (int)y - 7);
        }

        else if (x == stateContainer.Game.WIDTH) {
            exitPointRight.draw((int)x - 40, (int)y - 20);
            g.drawString(pointRef, (int)x - 35, (int)y - 7);
        }
    }

    /**
     * isRunway: Returns true if the ExitPoint is not on any of the screen edges, and therefore must be a runway
     */

    public boolean isRunway() {
        return x != 0 && x != stateContainer.Game.WIDTH && y != 0 && y != stateContainer.Game.HEIGHT;
    }

}

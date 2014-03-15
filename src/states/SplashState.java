package states;

//import java.awt.Rectangle;
import java.io.IOException;

//import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public final class SplashState extends BasicGameState {

    private static Image
    splash, indicator;
    private static LoadingList loading = LoadingList.get();

    public SplashState(int state) {}

    @Override
    public void init(GameContainer gc, StateBasedGame s)
    throws SlickException {
        splash = new Image("res/graphics/new/startup_bg.jpg");
        indicator = new Image("res/graphics/new/startup_plane.png");
    }

    @Override
    public void render(GameContainer gc, StateBasedGame s, Graphics g)
    throws SlickException {
        g.drawImage(splash, 0, 0);
        indicator.drawCentered(900 - ((600 * loading.getRemainingResources()) / loading.getTotalResources()), 390);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame s, int delta)
    throws SlickException {
        if (loading.getRemainingResources() == 0) { //finished loading
            gc.setShowFPS(false);
            s.enterState(stateContainer.Game.MENUSTATE);
        }

        else {
            DeferredResource next = loading.getNext();

            try {
                next.load();
            }

            catch (IOException e) {
                System.out.println("Failed loading:\t" + next.getDescription());
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getID() {
        return stateContainer.Game.SPLASHSTATE;
    }

}

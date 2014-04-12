package states;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public final class SplashState extends BasicGameState {

    private static Color backgroundColor = new Color(60, 60, 60); // or #3C3C3C as a hex colour
    private static Image loadingImage;
    private static LoadingList loading = LoadingList.get();

    public SplashState(int state) {}

    @Override
    public void init(GameContainer gc, StateBasedGame s) throws SlickException {
        gc.getGraphics().setBackground(backgroundColor);
        loadingImage = new Image("res/text_graphics/loading.png");        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
        loadingImage.drawCentered(stateContainer.Game.WIDTH / 2, stateContainer.Game.HEIGHT / 2);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
        // If there's nothing left to load then proceed to the main menu
        if (loading.getRemainingResources() == 0) {
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

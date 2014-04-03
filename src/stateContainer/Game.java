package stateContainer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.GameOverState;
import states.MenuState;
import states.PauseState;
import states.MultiPlayState;
import states.PlayState;
import states.ControlsState;
import states.SplashState;

public class Game extends StateBasedGame {

    public static final String NAME = "Don't Crash";

    public static final int
    SPLASHSTATE = 0,
    MENUSTATE = 1,
    PLAYSTATE = 2,
    MULTIPLAYSTATE = 3,
    GAMEOVERSTATE = 4,
    PAUSESTATE = 5,
    CONTROLSSTATE = 6;

    public static final int
    MAXIMUMWIDTH = 1200, MAXIMUMHEIGHT = 600;


    /**
     * Adds all states to a container
     * @param NAME The game's title
     */

    public Game(String NAME) {
        super(NAME);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new SplashState(SPLASHSTATE));
        enterState(SPLASHSTATE);
        addState(new MenuState(MENUSTATE));
        addState(new PlayState(PLAYSTATE));
        addState(new MultiPlayState(MULTIPLAYSTATE));
        addState(new GameOverState(GAMEOVERSTATE));
        addState(new PauseState(PAUSESTATE));
        addState(new ControlsState(CONTROLSSTATE));
    }

    public static void main(String[] args) {
        AppGameContainer appgc;

        try {
            appgc = new AppGameContainer(new Game(NAME));
            appgc.setDisplayMode(MAXIMUMWIDTH, MAXIMUMHEIGHT, false);
            appgc.setTargetFrameRate(60);
            appgc.setIcon("res/graphics/icon.png");
            appgc.start();
        }

        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
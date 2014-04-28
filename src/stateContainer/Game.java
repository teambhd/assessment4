package stateContainer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.DifficultyState;
import states.GameOverState;
import states.MenuState;
import states.PauseState;
import states.MultiPlayState;
import states.PlayState;
import states.ControlsState;
import states.SplashState;
import states.VictoryState;

public class Game extends StateBasedGame {

    public static final String NAME = "Don't Crash";

    public static final int SPLASHSTATE = 0;
    public static final int MENUSTATE = 1;
    public static final int PLAYSTATE = 2;
    public static final int MULTIPLAYSTATE = 3;
    public static final int GAMEOVERSTATE = 4;
    public static final int PAUSESTATE = 5;
    public static final int CONTROLSSTATE = 6;
    public static final int DIFFICULTYSTATE = 7;
    public static final int REDVICTORYSTATE = 8;
    public static final int BLUEVICTORYSTATE = 9;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 600;


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
        addState(new DifficultyState(DIFFICULTYSTATE));
        addState(new PlayState(PLAYSTATE));
        addState(new MultiPlayState(MULTIPLAYSTATE));
        addState(new GameOverState(GAMEOVERSTATE));
        addState(new PauseState(PAUSESTATE));
        addState(new ControlsState(CONTROLSSTATE));
        addState(new VictoryState(REDVICTORYSTATE, "red"));
        addState(new VictoryState(BLUEVICTORYSTATE, "blue"));
    }

    public static void main(String[] args) {
        AppGameContainer appgc;

        try {
            appgc = new AppGameContainer(new Game(NAME));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(60);
            appgc.setShowFPS(false);
            appgc.setAlwaysRender(true);
            appgc.setUpdateOnlyWhenVisible(true);
            appgc.setIcon("res/graphics/icon.png");
            appgc.start();
        }

        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
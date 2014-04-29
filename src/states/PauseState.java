package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.lwjgl.input.Mouse;

import util.DeferredFile;
import util.HoverImage;

public class PauseState extends BasicGameState {

    private static final Color BACKGROUND_COLOR = new Color(53, 75, 70);

    private static Image pauseTitle;

    private static Image backButton;
    private static Image backButtonHover;
    private static HoverImage back;

    private static Image menuImage;
    private static Image menuHover;
    private static HoverImage menu;

    private static Image quitButton;
    private static Image quitButtonHover;
    private static HoverImage quit;

    // Initialise the destination state variable with a sensible default
    private static int destinationStateID = stateContainer.Game.PLAYSTATE;

    public PauseState(int state) {}

    @Override
    public void init(GameContainer gc, StateBasedGame sbj) throws SlickException {
        LoadingList loading = LoadingList.get();
        loading.add(new DeferredFile("res/text_graphics/paused.png") {
            public void loadFile(String filename) throws SlickException {
                pauseTitle = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/resume.png") {
            public void loadFile(String filename) throws SlickException {
                backButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/resume_hover.png") {
            public void loadFile(String filename) throws SlickException {
                backButtonHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/menu.png") {
            public void loadFile(String filename) throws SlickException {
                menuImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/menu_hover.png") {
            public void loadFile(String filename) throws SlickException {
                menuHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit.png") {
            public void loadFile(String filename) throws SlickException {
                quitButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit_hover.png") {
            public void loadFile(String filename) throws SlickException {
                quitButtonHover = new Image(filename);
            }
        });
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up PauseState buttons";
            }
            public void load() {
                back = new HoverImage(backButton, backButtonHover, ((stateContainer.Game.WIDTH - backButton.getWidth()) / 2) + 5, 420);
                menu = new HoverImage(menuImage, menuHover, 20, 530);
                quit = new HoverImage(quitButton, quitButtonHover, stateContainer.Game.WIDTH - (quitButton.getWidth() + 15), 530);
            }
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Set the background colour
        g.setBackground(BACKGROUND_COLOR);

        // Draw the page title
        pauseTitle.draw(((stateContainer.Game.WIDTH - pauseTitle.getWidth()) / 2) + 5, 20);

        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        // Draw the buttons
        back.render(posX, posY);
        menu.render(posX, posY);
        quit.render(posX, posY);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (gc.getInput().isKeyPressed(Input.KEY_P)) {
            sbg.enterState(destinationStateID);
        }

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (back.isMouseOver(posX, posY)) {
                sbg.enterState(destinationStateID);
            }

            if (menu.isMouseOver(posX, posY)) {
                // We reset both the Challenge and Versus mode states here,
                // since there's no harm in resetting an already inactive state
                PlayState.restartGame();
                MultiPlayState.restartGame();
                sbg.enterState(stateContainer.Game.MENUSTATE);
            }

            if (quit.isMouseOver(posX, posY)) {
                System.exit(0);
            }
        }
    }

    public static void setDestinationStateID(int id) {
        destinationStateID = id;
    }

    @Override
    public int getID() {
        return stateContainer.Game.PAUSESTATE;
    }

}

package states;

import org.lwjgl.input.Mouse;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.DeferredFile;
import util.HoverImage;


public class DifficultyState extends BasicGameState {

    private static Image difficultyBackground;
    private static Image easyImage, mediumImage, hardImage;
    private static Image easyHover, mediumHover, hardHover;

    private HoverImage easyButton, mediumButton, hardButton;

    public static final int EASY = 1;
    public static final int MEDIUM = 2;
    public static final int HARD = 3;

    // Constructor
    public DifficultyState(int stateID) {}

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        LoadingList loading = LoadingList.get();

        loading.add(new DeferredFile("res/menu_graphics/difficulty.png") {
            public void loadFile(String filename) throws SlickException {
                difficultyBackground = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/easy.png") {
            public void loadFile(String filename) throws SlickException {
                easyImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/easy_hover.png") {
            public void loadFile(String filename) throws SlickException {
                easyHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/medium.png") {
            public void loadFile(String filename) throws SlickException {
                mediumImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/medium_hover.png") {
            public void loadFile(String filename) throws SlickException {
                mediumHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/hard.png") {
            public void loadFile(String filename) throws SlickException {
                hardImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/hard_hover.png") {
            public void loadFile(String filename) throws SlickException {
                hardHover = new Image(filename);
            }
        });
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up difficulty buttons";
            }
            public void load() {
                easyButton = new HoverImage(easyImage, easyHover, 100, 250);
                mediumButton = new HoverImage(mediumImage, mediumHover, 100, 340);
                hardButton = new HoverImage(hardImage, hardHover, 100, 430);
            }
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Get the co-ordinates of the mouse pointer, flipping on the Y axis so as to use the same origin as the graphics object
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        // Draw the page background (including the explanatory text)
        difficultyBackground.draw(0, 0);

        // Draw the difficulty selection buttons
        easyButton.render(posX, posY);
        mediumButton.render(posX, posY);
        hardButton.render(posX, posY);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (easyButton.isMouseOver(posX, posY)) {
                PlayState.setGameDifficulty(EASY);
                sbg.enterState(stateContainer.Game.PLAYSTATE);
            }

            else if (mediumButton.isMouseOver(posX, posY)) {
                PlayState.setGameDifficulty(MEDIUM);
                sbg.enterState(stateContainer.Game.PLAYSTATE);
            }

            else if (hardButton.isMouseOver(posX, posY)) {
                PlayState.setGameDifficulty(HARD);
                sbg.enterState(stateContainer.Game.PLAYSTATE);
            }
        }
    }


    @Override
    public int getID() {
        return stateContainer.Game.DIFFICULTYSTATE;
    }

}
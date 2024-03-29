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


public class GameOverState extends BasicGameState {

    private static Color backgroundColor = new Color(75, 70, 54);

    private static Image gameOverTitle;

    private static Image quitImage, quitHover;
    private static Image menuImage, menuHover;
    private static Image againImage, againHover;

    private static HoverImage againButton, menuButton, quitButton;
    
    private static int finalScore;
    

    // Constructor
    public GameOverState(int state) {}


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        LoadingList loading = LoadingList.get();
        loading.add(new DeferredFile("res/text_graphics/gameover.png") {
            public void loadFile(String filename) throws SlickException {
                gameOverTitle = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/again.png") {
            public void loadFile(String filename) throws SlickException {
                againImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit.png") {
            public void loadFile(String filename) throws SlickException {
                quitImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/menu.png") {
            public void loadFile(String filename) throws SlickException {
                menuImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/again_hover.png") {
            public void loadFile(String filename) throws SlickException {
                againHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit_hover.png") {
            public void loadFile(String filename) throws SlickException {
                quitHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/menu_hover.png") {
            public void loadFile(String filename) throws SlickException {
                menuHover = new Image(filename);
            }
        });
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up GameOverState buttons";
            }
            public void load() {
                againButton = new HoverImage(againImage, againHover, ((stateContainer.Game.WIDTH - againImage.getWidth()) / 2) + 5, 420);
                menuButton = new HoverImage(menuImage, menuHover, 20, 530);
                quitButton = new HoverImage(quitImage, quitHover, stateContainer.Game.WIDTH - (quitImage.getWidth() + 15), 530);
            }
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Set the background colour
        g.setBackground(backgroundColor);

        // Draw the page title
        gameOverTitle.draw(((stateContainer.Game.WIDTH - gameOverTitle.getWidth()) / 2) + 5, 20);
        
        // Draw the final score
        String finalScoreString = "Your final score was " + finalScore;
        int finalScoreX = (stateContainer.Game.WIDTH - util.GameFont.getFont().getWidth(finalScoreString)) / 2;
        util.GameFont.getFont().drawString(finalScoreX, 300, finalScoreString);

        // Get the co-ordinates of the mouse pointer,
        // flipping on the Y axis so as to use the same origin as the graphics object
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        // Render the 3 buttons
        againButton.render(posX, posY);
        menuButton.render(posX, posY);
        quitButton.render(posX, posY);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Get the co-ordinates of the mouse pointer, flipping on the Y axis so as to use the same origin as the graphics object
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (againButton.isMouseOver(posX, posY)) {
                sbg.enterState(stateContainer.Game.DIFFICULTYSTATE);
            }

            if (menuButton.isMouseOver(posX, posY)) {
                sbg.enterState(stateContainer.Game.MENUSTATE);
            }

            if (quitButton.isMouseOver(posX, posY)) {
                System.exit(0);
            }
        }
    }
    
    public static void setFinalScore(int score) {
        finalScore = score;
    }

    @Override
    public int getID() {
        return stateContainer.Game.GAMEOVERSTATE;
    }
}

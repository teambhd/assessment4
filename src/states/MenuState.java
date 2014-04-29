package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.DeferredFile;
import util.HoverImage;


public class MenuState extends BasicGameState {

    private static Image menuBackground;
    private static Image titleImage;
    
    private static Image playButton;
    private static Image playHover;
    private static HoverImage play;
    
    private static Image quitButton;
    private static Image quitHover;
    private static HoverImage quit;
    
    private static Image helpButton;
    private static Image helpHover;
    private static HoverImage help;
    
    private static Image versusButton;
    private static Image versusHover;
    private static HoverImage versus;

    private boolean mouseBeenReleased = false;


    // Constructor
    public MenuState(int state) {}


    public void init(GameContainer gc, StateBasedGame sbg)
    throws SlickException {
        LoadingList loading = LoadingList.get();
        loading.add(new DeferredFile("res/menu_graphics/background.png") {
            public void loadFile(String filename) throws SlickException {
                menuBackground = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/title.png") {
            public void loadFile(String filename) throws SlickException {
                titleImage = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/challenge.png") {
            public void loadFile(String filename) throws SlickException {
                playButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/challenge_hover.png") {
            public void loadFile(String filename) throws SlickException {
                playHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit.png") {
            public void loadFile(String filename) throws SlickException {
                quitButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/quit_hover.png") {
            public void loadFile(String filename) throws SlickException {
                quitHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/help.png") {
            public void loadFile(String filename) throws SlickException {
                helpButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/help_hover.png") {
            public void loadFile(String filename) throws SlickException {
                helpHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/versus.png") {
            public void loadFile(String filename) throws SlickException {
                versusButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/text_graphics/versus_hover.png") {
            public void loadFile(String filename) throws SlickException {
                versusHover = new Image(filename);
            }
        });
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up menuState buttons";
            }
            public void load() {
                play = new HoverImage(playButton, playHover, ((stateContainer.Game.WIDTH - playButton.getWidth()) / 2) + 5, 420);
                versus = new HoverImage(versusButton, versusHover, ((stateContainer.Game.WIDTH - versusButton.getWidth()) / 2) + 5, 500);
                help = new HoverImage(helpButton, helpHover, 20, 530);
                quit = new HoverImage(quitButton, quitHover, stateContainer.Game.WIDTH - (quitButton.getWidth() + 15), 530);
            }
        });
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Map mouse co-ordinates onto graphics co-ordinatess
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        // Draw the page background
        menuBackground.draw(0, 0);

        // Draw the game title
        titleImage.draw(((stateContainer.Game.WIDTH - titleImage.getWidth()) / 2) + 5, 0);

        // Draw the buttons
        play.render(posX, posY);
        versus.render(posX, posY);
        help.render(posX, posY);
        quit.render(posX, posY);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Mapping Mouse coords onto graphics coords
        int posX = Mouse.getX(),
            posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (mouseBeenReleased) { //button first pressed
                mouseBeenReleased = false;

                if (play.isMouseOver(posX, posY)) {
                    sbg.enterState(stateContainer.Game.DIFFICULTYSTATE);
                }

                if (versus.isMouseOver(posX, posY)) {
                    sbg.enterState(stateContainer.Game.MULTIPLAYSTATE);
                }

                if (help.isMouseOver(posX, posY)) {
                    sbg.enterState(stateContainer.Game.CONTROLSSTATE);
                }

                if (quit.isMouseOver(posX, posY)) {
                    System.exit(0);
                }
            }
        }

        else if (!mouseBeenReleased) {  //mouse just released
            mouseBeenReleased = true;
        }
    }

    public int getID() {
        return stateContainer.Game.MENUSTATE;
    }

}

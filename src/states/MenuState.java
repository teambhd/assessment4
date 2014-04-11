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

    private static Image
    menuBackground,
    playButton, quitButton, creditsButton, controlsButton,
    playHover, quitHover, creditsHover, controlsHover;

    private HoverImage
    play, quit, credits, controls;

    private boolean mouseBeenReleased;


    public MenuState(int state) {
        this.mouseBeenReleased = false;
    }


    public void init(GameContainer gc, StateBasedGame sbg)
    throws SlickException {
        LoadingList loading = LoadingList.get();
        loading.add(new DeferredFile("res/menu_graphics/background.png") {
            public void loadFile(String filename) throws SlickException {
                menuBackground = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/play_button.png") {
            public void loadFile(String filename) throws SlickException {
                playButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/play_hover.png") {
            public void loadFile(String filename) throws SlickException {
                playHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/quit_button.png") {
            public void loadFile(String filename) throws SlickException {
                quitButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/quit_hover.png") {
            public void loadFile(String filename) throws SlickException {
                quitHover = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/controls.png") {
            public void loadFile(String filename) throws SlickException {
                controlsButton = new Image(filename);
            }
        });
        loading.add(new DeferredFile("res/menu_graphics/controls_hover.png") {
            public void loadFile(String filename) throws SlickException {
                controlsHover = new Image(filename);
            }
        });
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up menuState buttons";
            }
            public void load() {
                play = new HoverImage(playButton, playHover, 439, 349);
                controls = new HoverImage(controlsButton, controlsHover, 5, 535);
                quit = new HoverImage(quitButton, quitHover, 1078, 540);
            }
        });
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
    throws SlickException {
        int posX = Mouse.getX(),
            posY = stateContainer.Game.HEIGHT - Mouse.getY();
        // Mapping Mouse coords onto graphics coords
        menuBackground.draw(0, 0);
        //draw buttons
        play.render(posX, posY);
        quit.render(posX, posY);
        controls.render(posX, posY);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int posX = Mouse.getX(),
            posY = stateContainer.Game.HEIGHT - Mouse.getY();
        // Mapping Mouse coords onto graphics coords

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (mouseBeenReleased) { //button first pressed
                mouseBeenReleased = false;

                if (play.isMouseOver(posX, posY)) {
                    sbg.enterState(stateContainer.Game.PLAYSTATE);
                }

                if (controls.isMouseOver(posX, posY)) {
                    sbg.enterState(stateContainer.Game.CONTROLSSTATE);
                }

                if (quit.isMouseOver(posX, posY)) {
                    System.exit(0);
                }
            }

            /* else mouse is dragged*/
        }

        else if (!mouseBeenReleased) {  //mouse just released
            mouseBeenReleased = true;
        }
    }

    public int getID() {
        return stateContainer.Game.MENUSTATE;
    }

}

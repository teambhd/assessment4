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


public class VictoryState extends BasicGameState {
    
    private int stateID;
    
    private String winningPlayer;
        
    private static Image redTitle;
    private static Image blueTitle;

    private static Image againImage;
    private static Image againHover;
    private static HoverImage againButton;
    
    private static Image menuImage;
    private static Image menuHover;
    private static HoverImage menuButton;

    private static Image quitImage;
    private static Image quitHover;
    private static HoverImage quitButton;


    // Constructor
    public VictoryState(int stateID, String winningPlayer) {
        this.stateID = stateID;
        this.winningPlayer = winningPlayer;        
    }


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        LoadingList loading = LoadingList.get();
        
        if (redTitle == null) {
            loading.add(new DeferredFile("res/text_graphics/redwins.png") {
                public void loadFile(String filename) throws SlickException {
                    redTitle = new Image(filename);
                }
            });
        }
        
        if (blueTitle == null) {
            loading.add(new DeferredFile("res/text_graphics/bluewins.png") {
                public void loadFile(String filename) throws SlickException {
                    blueTitle = new Image(filename);
                }
            });
        }
        
        if (againImage == null) {
            loading.add(new DeferredFile("res/text_graphics/again.png") {
                public void loadFile(String filename) throws SlickException {
                    againImage = new Image(filename);
                }
            });
        }
        
        if (quitImage == null) {
            loading.add(new DeferredFile("res/text_graphics/quit.png") {
                public void loadFile(String filename) throws SlickException {
                    quitImage = new Image(filename);
                }
            });
        }
        
        if (menuImage == null) {
            loading.add(new DeferredFile("res/text_graphics/menu.png") {
                public void loadFile(String filename) throws SlickException {
                    menuImage = new Image(filename);
                }
            });
        }
        
        if (againHover == null) {
            loading.add(new DeferredFile("res/text_graphics/again_hover.png") {
                public void loadFile(String filename) throws SlickException {
                    againHover = new Image(filename);
                }
            });
        }
        
        if (quitHover == null) {
            loading.add(new DeferredFile("res/text_graphics/quit_hover.png") {
                public void loadFile(String filename) throws SlickException {
                    quitHover = new Image(filename);
                }
            });
        }
        
        if (menuHover == null) {
            loading.add(new DeferredFile("res/text_graphics/menu_hover.png") {
                public void loadFile(String filename) throws SlickException {
                    menuHover = new Image(filename);
                }
            });
        }
        
        loading.add(new DeferredResource() {
            public String getDescription() {
                return "set up VictoryState buttons";
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
        // Draw the background image and the page title
        if (winningPlayer == "red") {
            g.setBackground(MultiPlayState.RED_COLOR);
            redTitle.draw(((stateContainer.Game.WIDTH - redTitle.getWidth()) / 2) + 5, 20);
        }
        
        else { // winningPlayer == "blue"
            g.setBackground(MultiPlayState.BLUE_COLOR);
            blueTitle.draw(((stateContainer.Game.WIDTH - blueTitle.getWidth()) / 2) + 5, 20);
        }
        
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
        // Get the co-ordinates of the mouse pointer, 
        // flipping on the Y axis so as to use the same origin as the graphics object
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (againButton.isMouseOver(posX, posY)) {
                sbg.enterState(stateContainer.Game.MULTIPLAYSTATE);
            }

            if (menuButton.isMouseOver(posX, posY)) {
                sbg.enterState(stateContainer.Game.MENUSTATE);
            }

            if (quitButton.isMouseOver(posX, posY)) {
                System.exit(0);
            }
        }
    }

    @Override
    public int getID() {
        return stateID;
    }
}

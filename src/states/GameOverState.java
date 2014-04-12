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

import logicClasses.Achievements;

import util.DeferredFile;
import util.HoverImage;


public class GameOverState extends BasicGameState {

    private static Image 
    quitImage, menuImage, againImage,
    quitHover, menuHover, againHover,
    gameOverBackground;
    
    private static HoverImage againButton, menuButton, quitButton;

    private Achievements achievement;

    public GameOverState(int state) {
        achievement = new Achievements();
    }


    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
    throws SlickException {
        {
            LoadingList loading = LoadingList.get();
            loading.add(new DeferredFile("res/menu_graphics/gameover_screen.png") {
                public void loadFile(String filename) throws SlickException {
                    gameOverBackground = new Image(filename);
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
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        gameOverBackground.draw(0, 0);
        
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();
        
        againButton.render(posX, posY);
        menuButton.render(posX, posY);
        quitButton.render(posX, posY);

        g.drawString(achievement.crashAchievement(60), 900, 30);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int posX = Mouse.getX();
        int posY = stateContainer.Game.HEIGHT - Mouse.getY();

        if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (againButton.isMouseOver(posX, posY)) {
                sbg.enterState(stateContainer.Game.PLAYSTATE);
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
        return stateContainer.Game.GAMEOVERSTATE;
    }
}

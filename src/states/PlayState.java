package states;

import java.awt.Font;
import java.io.InputStream;

import logicClasses.Achievements;
import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.Flight;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Image;

import util.DeferredFile;
import util.HoverImage;
import util.KeyBindings;


public class PlayState extends BasicGameState {
    private static Image
    easyImage, mediumImage, hardImage,
    easyHover, mediumHover, hardHover,
    backgroundImage, difficultyBackground,
    statusBarImage, clockImage, windImage;
            
    private HoverImage easyButton, mediumButton, hardButton;
    
    private static Sound endOfGameSound;
    private static Music gameplayMusic;
    private static TrueTypeFont font;
    public static float time;

    private Airspace airspace;
    private Controls controls;
    private String stringTime;
    private boolean settingDifficulty, gameEnded;

    private Achievements achievement;
    private String achievementMessage = "";

    public PlayState(int state) {
        achievement = new Achievements();
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gameEnded = false;
        settingDifficulty = true;
        time = 0;
        airspace = new Airspace(false);
        this.stringTime = "";
        gc.setAlwaysRender(true);
        gc.setUpdateOnlyWhenVisible(true);
        // Font
        {
            LoadingList loading = LoadingList.get();
            loading.add(new DeferredFile("res/fonts/fira-sans.ttf") {
                public void loadFile(String filename) {
                    InputStream inputStream = ResourceLoader.getResourceAsStream(filename);

                    try {
                        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                        font = new TrueTypeFont(awtFont.deriveFont(16f), true);
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // Music
            loading.add(new DeferredFile("res/music/new/muzikele.ogg") {
                public void loadFile(String filename) throws SlickException {
                    gameplayMusic = new Music(filename);
                }
            });
            loading.add(new DeferredFile("res/music/new/Big Explosion.ogg") {
                public void loadFile(String filename) throws SlickException {
                    endOfGameSound = new Sound(filename);
                }
            });
            //Images
            loading.add(new DeferredFile("res/graphics/clock.png") {
                public void loadFile(String filename) throws SlickException {
                    clockImage = new Image(filename);
                }
            });
            loading.add(new DeferredFile("res/graphics/wind_indicator.png") {
                public void loadFile(String filename) throws SlickException {
                    windImage = new Image(filename);
                }
            });
            loading.add(new DeferredFile("res/graphics/background.png") {
                public void loadFile(String filename) throws SlickException {
                    backgroundImage = new Image(filename);
                }
            });
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
        //initialise the airspace object;
        //Waypoints
        airspace.newWaypoint(350, 150, "A");
        airspace.newWaypoint(400, 470, "B");
        airspace.newWaypoint(700, 60,  "C");
        airspace.newWaypoint(800, 320, "D");
        airspace.newWaypoint(600, 418, "E");
        airspace.newWaypoint(500, 220, "F");
        airspace.newWaypoint(950, 188, "G");
        airspace.newWaypoint(1050, 272, "H");
        airspace.newWaypoint(900, 420, "I");
        airspace.newWaypoint(240, 250, "J");
        //EntryPoints
        airspace.newEntryPoint(0, 400);
        airspace.newEntryPoint(1200, 200);
        airspace.newEntryPoint(600, 0);
        airspace.newEntryPoint(760, 405);
        // Exit Points
        airspace.newExitPoint(800, 0, "1");
        airspace.newExitPoint(0, 200, "2");
        airspace.newExitPoint(1200, 300, "3");
        airspace.newExitPoint(590, 195, "4");
        airspace.init();
        
        // Initialise the controls
        controls = new Controls(KeyBindings.singlePlayerKeys, "single");
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        // Ensure that anti-aliasing is always enabled
        if (!g.isAntiAlias()) {
            g.setAntiAlias(true);
        }        
        
        // Checks whether the user is still choosing the difficulty
        if (settingDifficulty) {
            int posX = Mouse.getX();
            int posY = stateContainer.Game.HEIGHT - Mouse.getY();

            // Draw the page background (including the explanatory text)
            difficultyBackground.draw(0, 0);
            
            // Draw the difficulty selection buttons
            easyButton.render(posX, posY);
            mediumButton.render(posX, posY);
            hardButton.render(posX, posY);
        }

        else {  //main game
            //set font for the rest of the render
            g.setFont(font);
            // Drawing Side Images
            backgroundImage.draw(0, 0);
            // Drawing Airspace and elements within it
            g.setColor(Color.white);
            airspace.render(g);
            controls.render(g);
            // Drawing Clock and Time
            g.setColor(Color.white);
            clockImage.draw(0, 5);
            g.drawString(stringTime, 25, 10);
            // Drawing Score
            g.drawString(airspace.getScore().toString(), 10, 35);
            //drawing wind direction
            windImage.setRotation(windImage.getRotation() + ((float)Math.cos(time / 2999.0) + (float)Math.sin(time / 1009.0)) / 3);
            //for now, set wind direction pseudo-randomly
            windImage.draw(14, 550);
            g.drawString("Wind:", 60, 550);
            g.drawString(String.valueOf(Math.abs(Math.round(windImage.getRotation()))), 65, 565);
            // Drawing Achievements
            g.drawString(airspace.getScore().scoreAchievement(),
                         stateContainer.Game.WIDTH - font.getWidth(airspace.getScore().scoreAchievement()) - 10, 30);
            g.drawString(achievementMessage,
                         stateContainer.Game.WIDTH - 10 - font.getWidth(achievementMessage), 40);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Checks if the game has been retried and if it has resets the airspace
        if (gameEnded) {
            airspace.resetAirspace();
            time = 0;
            gameEnded = false;
            settingDifficulty = true;
            airspace.getScore().resetScore();
        }

        // Checks whether the user is still choosing the difficulty

        if (settingDifficulty) {
            int posX = Mouse.getX();
            int posY = stateContainer.Game.HEIGHT - Mouse.getY();

            if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                if (easyButton.isMouseOver(posX, posY)) {
                    airspace.setDifficultyValueOfGame(1);
                    airspace.createAndSetSeparationRules();
                    settingDifficulty = false;
                }

                else if (mediumButton.isMouseOver(posX, posY)) {
                    airspace.setDifficultyValueOfGame(2);
                    airspace.createAndSetSeparationRules();
                    settingDifficulty = false;
                }

                else if (hardButton.isMouseOver(posX, posY)) {
                    airspace.setDifficultyValueOfGame(3);
                    airspace.createAndSetSeparationRules();
                    settingDifficulty = false;
                }
            }
        }

        else {  //main game
            // Updating Clock and Time
            time += delta;
            achievement.timeAchievement((int)time);
            float decMins = time / 1000 / 60;
            int mins = (int) decMins;
            float decSecs = decMins - mins;
            int secs = Math.round(decSecs * 60);
            String stringMins = "";
            String stringSecs = "";

            if (secs >= 60) {
                secs -= 60;
                mins += 1;
                // {!} should do +60 score every minute(possibly)
                //     - after 3 minutes adds on 2 less points every time?
                airspace.getScore().updateTimeScore();
            }

            if (mins < 10) {
                stringMins = "0" + mins;
            }

            else {
                stringMins = String.valueOf(mins);
            }

            if (secs < 10) {
                stringSecs = "0" + secs;
            }

            else {
                stringSecs = String.valueOf(secs);
            }

            this.stringTime = stringMins + ":" + stringSecs;
            // Updating Airspace
            airspace.newFlight();
            airspace.update();
            
            controls.update(gc, airspace);

            if (airspace.getSeparationRules().getGameOverViolation() == true) {
                achievementMessage = achievement.crashAchievement((int)time); //pass the game time as of game over into the crashAchievement
                airspace.getSeparationRules().setGameOverViolation(false);
                airspace.resetAirspace();
                gameplayMusic.stop();
                endOfGameSound.play();
                sbg.enterState(stateContainer.Game.GAMEOVERSTATE);
                gameEnded = true;
            }

            Input input = gc.getInput();

            // Checking For Pause Screen requested in game
            if (input.isKeyPressed(Input.KEY_P)) {
                sbg.enterState(stateContainer.Game.PAUSESTATE);
            }

            if (!gameplayMusic.playing()) {
                //Loops gameplay music based on random number created in init
                gameplayMusic.loop(1.0f, 0.5f);
            }
        }
    }

    @Override
    public int getID() {
        return stateContainer.Game.PLAYSTATE;
    }

    public Airspace getAirspace() {
        return airspace;
    }

    public void setAirspace(Airspace airspace) {
        this.airspace = airspace;
    }

}
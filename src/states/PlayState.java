package states;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import logicClasses.Achievements;
import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.Flight;
import logicClasses.Timer;
import logicClasses.WindIndicator;

import util.DeferredFile;
import util.KeyBindings;

public class PlayState extends BasicGameState {

    private static Sound endOfGameSound;
    private static Music gameplayMusic;
    private static TrueTypeFont font;
    
    private float time = 0;

    private Airspace airspace;
    private Controls controls;
    private WindIndicator windIndicator;

    private static boolean gameBegun = false;

    private Achievements achievement;
    private String achievementMessage = "";
    
    private static int difficultyLevel;

    public PlayState(int state) {
        achievement = new Achievements();
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // Initialise all the required classes (mainly this loads required images)  
        logicClasses.Airspace.init();      
        logicClasses.Airport.init();
        logicClasses.Flight.init();
        logicClasses.Waypoint.init();
        logicClasses.ExitPoint.init();
        logicClasses.EntryPoint.init();
        logicClasses.Timer.init();
        logicClasses.WindIndicator.init();                
        
        
        LoadingList loading = LoadingList.get();
        
        // Font
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
        
        // Sound Effects
        loading.add(new DeferredFile("res/music/new/Big Explosion.ogg") {
            public void loadFile(String filename) throws SlickException {
                endOfGameSound = new Sound(filename);
            }
        });
        
        
        // Create the airspace object;
        airspace = new Airspace(false);
        
        // Add Waypoints
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
        
        // Add EntryPoints
        airspace.newEntryPoint(0, 400);
        airspace.newEntryPoint(1200, 200);
        airspace.newEntryPoint(600, 0);
        airspace.newEntryPoint(700, 400);		//Blue Airport
        airspace.newEntryPoint(100, 100);		//Red Airport
        
        // Add Exit Points
        airspace.newExitPoint(800, 0, "Exit 1");
        airspace.newExitPoint(0, 200, "Exit 2");
        airspace.newExitPoint(1200, 300, "Exit 3");
        airspace.newExitPoint(700, 300, "Blue airport");
        airspace.newExitPoint(200, 100, "Red airport");
        
        // Initialise the controls
        controls = new Controls(KeyBindings.singlePlayerKeys, "single");
        
        // Create the wind indicator object
        windIndicator = new WindIndicator();
        
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        if (!gameBegun) {
            // Set the difficulty from the value deposited by the DifficultyState
            System.out.println("Game begun with difficulty " + difficultyLevel);
            airspace.setDifficultyValueOfGame(difficultyLevel);
            airspace.createAndSetSeparationRules();
            
            // Reset the airspace and the time and score (redundant on first launch but may as well be done)
            airspace.resetAirspace();
            time = 0;
            airspace.getScore().resetScore();
            
            gameBegun = true;
        }
    } 
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        // Ensure that anti-aliasing is always enabled
        if (!g.isAntiAlias()) {
            g.setAntiAlias(true);
        }        
        
        //set font for the rest of the render
        g.setFont(font);
                
        // Drawing the airspace (and it's background image) and thereby the elements within it
        airspace.render(g);
        controls.render(g);
        
        // Drawing Clock and Time
        logicClasses.Timer.render(g, this.time);
        
        // Drawing Score
        g.setColor(Color.white);
        g.drawString(airspace.getScore().toString(), 10, 35);
        
        // Draw the WindIndicator
        windIndicator.render(g, this.time);
        
        // Drawing Achievements
        g.drawString(airspace.getScore().scoreAchievement(),
                     stateContainer.Game.WIDTH - font.getWidth(airspace.getScore().scoreAchievement()) - 10, 30);
        g.drawString(achievementMessage,
                     stateContainer.Game.WIDTH - 10 - font.getWidth(achievementMessage), 40);
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        // Updating Clock and Time
        time += delta;
        achievement.timeAchievement((int)time);        
        
        // Updating Airspace
        airspace.newFlight();
        airspace.update();
        
        controls.update(gc, airspace);

        if (airspace.getSeparationRules().getGameOverViolation() == true) {
            achievementMessage = achievement.crashAchievement((int)time); //pass the game time as of game over into the crashAchievement
            airspace.getSeparationRules().setGameOverViolation(false);
            gameplayMusic.stop();
            endOfGameSound.play();
            sbg.enterState(stateContainer.Game.GAMEOVERSTATE);
        }

        // Checking For Pause Screen requested in game
        if (gc.getInput().isKeyPressed(Input.KEY_P)) {
            sbg.enterState(stateContainer.Game.PAUSESTATE);
        }

        if (!gameplayMusic.playing()) {
            //Loops gameplay music based on random number created in init
            gameplayMusic.loop(1.0f, 0.5f);
        }
        
    }
    
    public static void setGameDifficulty(int dL) {
        difficultyLevel = dL;
    }
    
    public static void restartGame() {
        gameBegun = false;
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

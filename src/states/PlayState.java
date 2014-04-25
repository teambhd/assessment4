package states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.Flight;
import logicClasses.TimeIndicator;
import logicClasses.WindIndicator;

import util.KeyBindings;


public class PlayState extends BasicGameState {
    
    private int time = 0;
    
    private Airspace airspace;
    private Controls controls;
    private WindIndicator windIndicator;

    private static int difficultyLevel;
    private static boolean gameBegun;
    
    // Constructor
    public PlayState(int state) {}

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
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
        airspace.newEntryPoint(700, 425);		//Blue Airport
        airspace.newEntryPoint(75, 100);		//Red Airport
        
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
        
        // Set font for the rest of the render
        g.setFont(util.GameFont.getFont());
                
        // Drawing the airspace (and it's background image) and thereby the elements within it
        airspace.render(g);
        controls.render(g);
        
        // Drawing Clock and Time
        logicClasses.TimeIndicator.render(g, this.time);
        
        // Drawing Score
        g.setColor(Color.white);
        g.drawString(airspace.getScore().toString(), 10, 35);
                
        // Draw the WindIndicator
        windIndicator.render(g, this.time);
                
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        
        // Increment the time variable by the number of ms elapsed since this function was last called
        time += delta;
        
        // Update the airspace, and potentially spawn a new flight
        airspace.newFlight();
        airspace.update();
        
        // Update the controls overlay
        controls.update(gc, airspace);

        if (airspace.getSeparationRules().getGameOverViolation()) {
            airspace.getSeparationRules().setGameOverViolation(false);
            util.GameAudio.getMusic().stop();
            util.GameAudio.getEndOfGameSound().play();
            restartGame();
            sbg.enterState(stateContainer.Game.GAMEOVERSTATE);
        }

        // Checking for Pause Screen requested in game
        if (gc.getInput().isKeyPressed(Input.KEY_P)) {
	    PauseState.setDestinationStateID(getID());
            sbg.enterState(stateContainer.Game.PAUSESTATE);
        }

        if (!util.GameAudio.getMusic().playing()) {
            //Loops gameplay music based on random number created in init
            util.GameAudio.getMusic().loop(1.0f, 0.5f);
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

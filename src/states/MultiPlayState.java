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


public class MultiPlayState extends BasicGameState {
    
    private int time = 0;
    
    private Airspace airspace;
    private WindIndicator windIndicator;

    private Controls redControls;
    private Controls blueControls;
    
    public static final Color RED_COLOR = new Color(165, 0, 0); // or #D70C1E as a hex
    public static final Color BLUE_COLOR = new Color(0, 0, 160);

    private static boolean gameBegun;
    
    // Constructor
    public MultiPlayState(int state) {}
        
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
                
        // Create the airspace object;
        airspace = new Airspace(true);

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
        redControls = new Controls(KeyBindings.redPlayerKeys, "red");
	    blueControls = new Controls(KeyBindings.bluePlayerKeys, "blue");
        
        // Create the wind indicator object
        windIndicator = new WindIndicator();
        
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        if (!gameBegun) {
            airspace.setDifficultyValueOfGame(1);
            airspace.createAndSetSeparationRules();
            
            // Reset the airspace and the time (redundant on first launch but may as well be done)
            airspace.resetAirspace();
            time = 0;
            
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

	    // Render the two sets of controls
        redControls.render(g);
	    blueControls.render(g);
        
        // Drawing Clock and Time
        logicClasses.TimeIndicator.render(g, this.time);
        
        // Drawing the score
        airspace.getScore("red").render(g, 10, 10, RED_COLOR);
        airspace.getScore("blue").render(g, 10, 30, BLUE_COLOR);
                
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
        
        // Update the controls overlays
        redControls.update(gc, airspace);
	    blueControls.update(gc, airspace);
        
        // Handle a crash between two planes
        if (airspace.getSeparationRules().getGameOverViolation()) { // not actually a Game Over anymore!
            // Play the crash sound
            util.GameAudio.getEndOfGameSound().play();
            
            // Deduct 500 points from the owners of each of the affected planes
            airspace.getScore(airspace.getSeparationRules().getViolatingFlight1().getOwner()).applyCrashPenalty();
            airspace.getScore(airspace.getSeparationRules().getViolatingFlight2().getOwner()).applyCrashPenalty();
            
            // Destroy the two planes
            airspace.getSeparationRules().getViolatingFlight1().setRemove(true);
            airspace.getSeparationRules().getViolatingFlight2().setRemove(true);
            
            // Reset the separation rules in time for the next crash
            airspace.getSeparationRules().setGameOverViolation(false);
        }

        // Checking for Pause Screen requested in game
        if (gc.getInput().isKeyPressed(Input.KEY_P)) {
	        PauseState.setDestinationStateID(getID());
            sbg.enterState(stateContainer.Game.PAUSESTATE);
        }

        // Loop the gameplay music
        if (!util.GameAudio.getMusic().playing()) {
            util.GameAudio.getMusic().loop(1.0f, 0.5f);
        }

    }
    
    @Override
    public void leave(GameContainer gc, StateBasedGame sbg) {
        // Stop the music when leaving the state
        util.GameAudio.getMusic().stop();
    }

    public static void restartGame() {
        gameBegun = false;
    }

    public Airspace getAirspace() {
        return airspace;
    }

    public void setAirspace(Airspace airspace) {
        this.airspace = airspace;
    }
    
    @Override
    public int getID() {
        return stateContainer.Game.MULTIPLAYSTATE;
    }
    
}

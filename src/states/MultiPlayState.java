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
    private Controls controls;
    private WindIndicator windIndicator;

    private static boolean gameBegun;
    
    // Constructor
    public MultiPlayState(int state) {}
        
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
                
        // Create the airspace object;
        airspace = new Airspace(true);
        
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        // Ensure that anti-aliasing is always enabled
        if (!g.isAntiAlias()) {
            g.setAntiAlias(true);
        }        
        
        // Set font for the rest of the render
        g.setFont(util.GameFont.getFont());
        
    
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    
    }
    
    @Override
    public int getID() {
        return stateContainer.Game.MULTIPLAYSTATE;
    }
    
}
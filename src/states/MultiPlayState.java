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

import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.Flight;
import logicClasses.TimeIndicator;
import logicClasses.WindIndicator;

import util.DeferredFile;
import util.KeyBindings;


public class MultiPlayState extends BasicGameState {
    
    // Constructor
    public MultiPlayState(int state) {}
        
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    
    }
    
    @Override
    public int getID() {
        return stateContainer.Game.MULTIPLAYSTATE;
    }
    
}
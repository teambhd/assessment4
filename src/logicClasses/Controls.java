package logicClasses;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

import util.KeyBindings;


public class Controls {

    private KeyBindings myKeys;
    private String myPlayer;
    
    private Flight selectedFlight;


    // CONSTRUCTOR
    
    public Controls(KeyBindings keys, String player) {
        myPlayer = player; // "single", "red" or "blue"
        myKeys = keys;
        selectedFlight = null;
    }


    // METHODS
    
    private double distance(double x1, double y1, double x2, double y2) {
        // DON'T PANIC, just pythagoras
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void update(GameContainer gc, Airspace airspace) {
        // This function handles keyboard inputs and makes the appropriate changes to flights within the airspace
    }


    // MUTATORS AND ACCESSORS
    
    public void setSelectedFlight(Flight flight1) {
        selectedFlight = flight1;
    }

    public Flight getSelectedFlight() {
        return selectedFlight;
    }

}

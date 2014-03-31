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
    
    // private double distance(double x1, double y1, double x2, double y2) {
    //     // DON'T PANIC, just pythagoras
    //     return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    // }

    public void update(GameContainer gc, Airspace airspace) {
        // This function handles keyboard inputs and makes the appropriate changes to flights within the airspace
        
        // TODO: Ensure that our selected flight still exists within the airspace and is still owned by the correct player
        
        if (selectedFlight == null && airspace.isFlightWithOwner(myPlayer)) {
            // Ensure that there is always a plane selected if there's one in the airspace to select
            selectedFlight = airspace.getListOfFlightsWithOwner(myPlayer).get(0);
        }
        
        if (selectedFlight == null) {
            // If there's still no selected flight then there are none in the airspace for us to control, so we'll just return
            return;
        }
        
        // Now we can get on to actually handling the keyboard input
        Input input = gc.getInput();
        
        if (input.isKeyPressed(myKeys.get("up"))) {
            // Increase altitude by 1000ft if the flight's not already at its ceiling
            if (selectedFlight.getTargetAltitude() <= selectedFlight.getMaxAltitude() - 1000) {
                selectedFlight.setTargetAltitude(selectedFlight.getTargetAltitude() + 1000);
            }
        }
        
        else if (input.isKeyPressed(myKeys.get("down"))) {
            // Decrease altitude by 1000ft if the flight's not already at its minimum height
            if (selectedFlight.getTargetAltitude() >= selectedFlight.getMinAltitude() + 1000) {
                selectedFlight.setTargetAltitude(selectedFlight.getTargetAltitude() - 1000);
            }
        }
        
        if (input.isKeyPressed(myKeys.get("left"))) {
            // Turn left by 15 degrees (we don't need to handle going past 0 degrees here)
            selectedFlight.turnFlightLeft(15);
        }
        
        else if (input.isKeyPressed(myKeys.get("right"))) {
            // Turn right by 15 degrees (we don't need to handle going past 360 degrees here)
            selectedFlight.turnFlightRight(15);
        }
        
        if (input.isKeyPressed(myKeys.get("accelerate"))) {
            // TODO
        }
        
        else if (input.isKeyPressed(myKeys.get("decelerate"))) {
            // TODO
        }
        
        if (input.isKeyPressed(myKeys.get("toggle_forwards"))) {
            // TODO
        }
        
        else if (input.isKeyPressed(myKeys.get("toggle_backwards"))) {
            // TODO
        }
        
        if (input.isKeyPressed(myKeys.get("airport"))) {
            // TODO
        }
    }


    // MUTATORS AND ACCESSORS
    
    public void setSelectedFlight(Flight flight1) {
        selectedFlight = flight1;
    }

    public Flight getSelectedFlight() {
        return selectedFlight;
    }

}

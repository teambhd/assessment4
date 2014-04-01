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
       
    public void render(Graphics g, GameContainer gc) throws SlickException {
        if (selectedFlight != null) {
            // TODO: Draw a circle around our currently selected flight
        }
    }

    public void update(GameContainer gc, Airspace airspace) {
        // This function handles keyboard inputs and makes the appropriate changes to flights within the airspace
        
        // Ensure that our selected flight still exists within the airspace and is still owned by the correct player
        if (!airspace.getListOfFlightsWithOwner(myPlayer).contains(selectedFlight)) {
            selectedFlight = null;
        }
        
        // Ensure that there is always a plane selected if there's one in the airspace to select
        if (selectedFlight == null && airspace.isFlightWithOwner(myPlayer)) {
            selectedFlight = airspace.getListOfFlightsWithOwner(myPlayer).get(0);
        }
        
        // If there's still no selected flight then there are none in the airspace for us to control, 
        // so we can't go as far as handling input keys
        if (selectedFlight == null) {
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
        
        // Note that these two handlers use isKeyDown rather than isKeyPressed so as to allow holding down the
        // turn left and turn right keys for more precise control of direction.
        
        if (input.isKeyDown(myKeys.get("left"))) {
            // Turn left by 2 degrees (we don't need to handle going past 0 degrees here)
            selectedFlight.turnFlightLeft(2);
        }
        
        else if (input.isKeyDown(myKeys.get("right"))) {
            // Turn right by 2 degrees (we don't need to handle going past 360 degrees here)
            selectedFlight.turnFlightRight(2);
        }
        
        if (input.isKeyPressed(myKeys.get("accelerate"))) {
            // Increase velocity by 50 if the flight's not already at its maximum speed
            if (selectedFlight.getTargetVelocity() <= selectedFlight.getMaxVelocity() - 50) {
                selectedFlight.setTargetVelocity(selectedFlight.getTargetVelocity() + 50);
            }
        }
        
        else if (input.isKeyPressed(myKeys.get("decelerate"))) {
            // Decrease velocity by 50 if the flight's not already at its minimum speed
            if (selectedFlight.getTargetVelocity() >= selectedFlight.getMinVelocity() + 50) {
                selectedFlight.setTargetVelocity(selectedFlight.getTargetVelocity() - 50);
            }
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
    
    public void setSelectedFlight(Flight flight) {
        selectedFlight = flight;
    }

    public Flight getSelectedFlight() {
        return selectedFlight;
    }

}

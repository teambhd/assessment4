package logicClasses;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

import util.KeyBindings;


public class Controls {

    private KeyBindings myKeys;
    private String myPlayer;

    private Flight selectedFlight;


    // =============
    // # Constructor
    // =============

    public Controls(KeyBindings keys, String player) {
        myPlayer = player; // "single", "red" or "blue"
        myKeys = keys;
        selectedFlight = null;
    }


    // =============
    // # Methods
    // =============

    public void render(Graphics g) throws SlickException {
        if (selectedFlight != null) {
            // Draw a circle of the appropriate colour around our currently selected flight
            if (myPlayer == "red") {
                g.setColor(states.MultiPlayState.RED_COLOR);
            }

            else if (myPlayer == "blue") {
                g.setColor(states.MultiPlayState.BLUE_COLOR);
            }

            else {
                g.setColor(Color.white);
            }

            g.drawOval((int)selectedFlight.getX() - 14, (int)selectedFlight.getY() - 14, 28, 28);

            // Draw the flight plan for our currently selected flight
            selectedFlight.getFlightPlan().render(g);
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

        if (selectedFlight.isCommandable()) {

            if (input.isKeyPressed(myKeys.get("up"))) {
                // Increase altitude by 1000ft if the flight's not already at its ceiling
                if (selectedFlight.getTargetAltitude() <= Flight.MAX_ALTITUDE - 1000) {
                    selectedFlight.setTargetAltitude(selectedFlight.getTargetAltitude() + 1000);
                }
            }

            else if (input.isKeyPressed(myKeys.get("down"))) {
                // Decrease altitude by 1000ft if the flight's not already at its minimum height
                if (selectedFlight.getTargetAltitude() >= Flight.MIN_ALTITUDE + 1000) {
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
                if (selectedFlight.getTargetVelocity() <= Flight.MAX_VELOCITY - 50) {
                    selectedFlight.setTargetVelocity(selectedFlight.getTargetVelocity() + 50);
                }
            }

            else if (input.isKeyPressed(myKeys.get("decelerate"))) {
                // Decrease velocity by 50 if the flight's not already at its minimum speed
                if (selectedFlight.getTargetVelocity() >= Flight.MIN_VELOCITY + 50) {
                    selectedFlight.setTargetVelocity(selectedFlight.getTargetVelocity() - 50);
                }
            }

        }

        if (input.isKeyPressed(myKeys.get("toggle_forwards"))) {
            // Switch the current flight, moving forwards through the list of flights
            // (and looping back to the first flight if the last flight is already selected)
            List<Flight> listOfFlights = new ArrayList<Flight>();
            listOfFlights = airspace.getListOfFlightsWithOwner(myPlayer);
            int indexOfCurrentFlight = listOfFlights.indexOf(selectedFlight);

            if (indexOfCurrentFlight == listOfFlights.size() - 1) {
                selectedFlight = listOfFlights.get(0);
            }

            else {
                selectedFlight = listOfFlights.get(indexOfCurrentFlight + 1);
            }
        }

        else if (input.isKeyPressed(myKeys.get("toggle_backwards"))) {
            // Switch the current flight, moving backwards through the list of flights
            // (and looping if the first flight is already selected)
            List<Flight> listOfFlights = new ArrayList<Flight>();
            listOfFlights = airspace.getListOfFlightsWithOwner(myPlayer);
            int indexOfCurrentFlight = listOfFlights.indexOf(selectedFlight);

            if (indexOfCurrentFlight == 0) {
                selectedFlight = listOfFlights.get(listOfFlights.size() - 1);
            }

            else {
                selectedFlight = listOfFlights.get(indexOfCurrentFlight - 1);
            }
        }

        if (input.isKeyPressed(myKeys.get("airport"))) {
            // If we're waiting for take-off, then take-off, otherwise attempt to issue the land command
            if (selectedFlight.isGrounded()) {
                selectedFlight.takeOff();
            }

            else {
                selectedFlight.land();
            }
        }
        
        if (myPlayer == "red" || myPlayer == "blue") {
            // If we're in multiplayer mode, then we can check to see if the handover key has been pressed
            if (input.isKeyPressed(myKeys.get("handover"))) {
                selectedFlight.handOver(airspace.getScore(selectedFlight.getOwner()));
            }
        }
    }

}

package unitTests;

import static org.junit.Assert.*;

import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.EntryPoint;
import logicClasses.Flight;
import logicClasses.SeparationRules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stateContainer.Game;

public class Controls_Tests {

    private Controls controlsInstance;

    @Before
    public void Setup() {
        controlsInstance = new Controls();
    }

    @Test
    public void testGetSelectedFlight() {
        // This also tests 'setSelectedFlight()'
        // No need to repeat test!
        Airspace newAirspace = new Airspace();
        newAirspace.addEntryPoint(new EntryPoint(10, 10));
        newAirspace.addEntryPoint(new EntryPoint(20, 20));
        newAirspace.addEntryPoint(new EntryPoint(20, 0));
        newAirspace.addEntryPoint(new EntryPoint(0, 20));
        Flight newFlight = new Flight(newAirspace);
        controlsInstance.setSelectedFlight(newFlight);
        assertEquals(newFlight, controlsInstance.getSelectedFlight());
    }

    @Test
    public void testSetDifficultyValueOfGame() {
        Airspace airspace = new Airspace();
        airspace.setDifficultyValueOfGame(1);
        airspace.getControls().setDifficultyValueOfGame(1);// Set the difficulty of the airspace so can retrieve it
        int actualDifficulty = airspace.getDifficultyValueOfGame();
        assertEquals(1, actualDifficulty);
    }

    @After
    public void tearDown() {
        controlsInstance = null;
    }

}

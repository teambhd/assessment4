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

import util.KeyBindings;

public class Controls_Tests {

    private Controls controlsInstance;

    @Before
    public void Setup() {
        controlsInstance = new Controls(KeyBindings.singlePlayerKeys, "single");
    }

    @After
    public void tearDown() {
        controlsInstance = null;
    }

}

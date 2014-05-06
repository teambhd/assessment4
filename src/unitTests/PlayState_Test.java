package unitTests;

import static org.junit.Assert.*;
import logicClasses.Airspace;

import org.junit.Before;
import org.junit.Test;

import states.PlayState;

public class PlayState_Test {
    /**
	 * @uml.property  name="airspaceInstance"
	 * @uml.associationEnd  
	 */
    private Airspace airspaceInstance;
    /**
	 * @uml.property  name="playStateInstance"
	 * @uml.associationEnd  
	 */
    private PlayState playStateInstance;

    @Before
    public void setup() {
        int playState = 2;
        playStateInstance =  new PlayState(playState);
        airspaceInstance = new Airspace(false);
    }

    @Test
    public void testGetID() {
        int actualID = playStateInstance.getID();
        assertEquals(2, actualID);
    }

}

package unitTests;

import static org.junit.Assert.*;

import java.awt.Font;

import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.EntryPoint;
import logicClasses.Flight;
import logicClasses.SeparationRules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

import stateContainer.Game;

public class Controls_Tests {
	
	private Controls controlsInstance;

	@Before
	public void Setup(){
		controlsInstance = new Controls();
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleAndUpdateAltitudeButtons() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeModeByClickingOnFlight() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckSelected() {
		fail("Not yet implemented");
	}

	@Test
	public void testGiveHeadingWithMouse() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateHeadingTextBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTurnLeftTextBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTurnRightTextBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testRender() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	
	@Test // This also tests 'setSelectedFlight()'
	// 		 No need to repeat test!
	public void testGetSelectedFlight() throws SlickException {			
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
	public void tearDown(){
		controlsInstance = null;
	}

}


package unitTests;

import static org.junit.Assert.*;

import logicClasses.Airspace;
import logicClasses.Flight;
import logicClasses.FlightMenu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import stateContainer.Game;


public class FlightMenu_Tests {
	
	private FlightMenu flightMenuInstance;
	
	@Before
	public void setUp() throws SlickException {
		 flightMenuInstance = new FlightMenu();
	}

	@After
	public void tearDown() {
		flightMenuInstance = null;
	}
	
	// As the TrueTypeFont needs OpenGL context all these test fail
	// In theory they should all pass, below is the idea of how to fix
	// this issue however this is not fully correct with the limited time
	@Test
	public void testIsAcceptingInput() throws SlickException {
		AppGameContainer ap = new AppGameContainer(new BasicGame("Test") {
			 

            @Override
            public void update(GameContainer container, int delta)
                    throws SlickException {
            }

            @Override
            public void init(GameContainer container) throws SlickException {
            	flightMenuInstance = new FlightMenu();
            	// no flight added test
				boolean acceptingInput = true;
				boolean actualAcceptingInput = flightMenuInstance.isAcceptingInput();
				assertEquals(acceptingInput, actualAcceptingInput);
				container.exit();
            }

			@Override
			public void render(GameContainer container, Graphics g)
					throws SlickException {
				
				
			}
        });
		ap.start();
	}
		

	@Test
	public void testSetInput() {
		fail("Not yet implemented");
	}

	@Test
	public void testMouseMoved() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAltSize() {
		int altSize = 100;
		int actualAltSize = flightMenuInstance.getAltSize();
		
		assertEquals(altSize, actualAltSize);
	}

	@Test
	public void testSetAltSize() {
		int altSizeSet = 200;
		flightMenuInstance.setAltSize(altSizeSet);
		int actualAltSizeSet = flightMenuInstance.getAltSize();
		
		assertEquals(altSizeSet, actualAltSizeSet);
	}

	@Test
	public void testGetSpeedSize() {
		int speedSize = 100;
		int actualSpeedSize = flightMenuInstance.getSpeedSize();
		
		assertEquals(speedSize, actualSpeedSize);
	}

	@Test
	public void testSetSpeedSize() {
		int setSpeedSize = 200;
		flightMenuInstance.setSpeedSize(setSpeedSize);
		int actualSetSpeedSize = flightMenuInstance.getSpeedSize();
		
		assertEquals(setSpeedSize, actualSetSpeedSize);
	}

	@Test
	public void testGetBearingSize() {
		int headingSize = 120;
		int actualHeadingSize = flightMenuInstance.getBearingSize();
		
		assertEquals(headingSize, actualHeadingSize);
	}

	@Test
	public void testSetHeadingSize() {
		int setHeadingSize = 200;
		flightMenuInstance.setHeadingSize(setHeadingSize);
		int actualSetHeadingSize = flightMenuInstance.getBearingSize();
		
		assertEquals(setHeadingSize, actualSetHeadingSize);
	}

	@Test
	public void testGetSliderWidth() {
		int sliderWidth = 12;
		int actualSliderWidth = flightMenuInstance.getSliderWidth();
		
		assertEquals(sliderWidth, actualSliderWidth);
	}

	@Test
	public void testSetSliderWidth() {
		int setSliderWidth = 20;
		int actualSetSliderWidth = flightMenuInstance.getSliderWidth();
		
		assertEquals(setSliderWidth, actualSetSliderWidth);
	}

	@Test
	public void testGetIndicatorSize() {
		int indicatorSize = 20;
		int actualIndicatorSize = flightMenuInstance.getIndicatorSize();
		
		assertEquals(indicatorSize, actualIndicatorSize);
	}

	@Test
	public void testSetIndicatorSize() {
		int setIndicatorSize = 30;
		flightMenuInstance.setIndicatorSize(setIndicatorSize);
		int actualSetIndicatorSize = flightMenuInstance.getIndicatorSize();
		
		assertEquals(setIndicatorSize, actualSetIndicatorSize);
	}

	@Test
	public void testGetButtonWidth() {
		int buttonWidth = 55;
		int actualButtonWidth = flightMenuInstance.getButtonWidth();
		
		assertEquals(buttonWidth, actualButtonWidth);
	}

	@Test
	public void testSetButtonWidth() {
		int setButtonWidth = 70;
		flightMenuInstance.setButtonWidth(setButtonWidth);
		int actualSetButtonWidth = flightMenuInstance.getButtonWidth();
		
		assertEquals(setButtonWidth, actualSetButtonWidth);
	}

	@Test
	public void testGetButtonHeight() {
		int buttonHeight = 25;
		int actualButtonHeight = flightMenuInstance.getButtonHeight();
		
		assertEquals(buttonHeight, actualButtonHeight);
	}

	@Test
	public void testSetButtonHeight() {
		int setButtonHeight = 60;
		flightMenuInstance.setButtonHeight(setButtonHeight);
		int actualSetButtonHeight = flightMenuInstance.getButtonHeight();
		
		assertEquals(setButtonHeight, actualSetButtonHeight);
	}

	@Test
	public void testGetSpacingSize() {
		int spacingSize = 6;
		int actualSpacingSize = flightMenuInstance.getSpacingSize();
		
		assertEquals(spacingSize, actualSpacingSize);
	}

	@Test
	public void testSetSpacingSize() {
		int setSpacingSize = 10;
		flightMenuInstance.setSpacingSize(setSpacingSize);
		int actualSetSpacingSize = flightMenuInstance.getSpacingSize();
		
		assertEquals(setSpacingSize, actualSetSpacingSize);
	}

	@Test
	public void testGetLabelColor() {
		Color labelColor = Color.white;
		Color actualLabelColor = flightMenuInstance.getLabelColor();
		
		assertEquals(labelColor, actualLabelColor);
	}

	@Test
	public void testSetLabelColor() {
		Color setLabelColor = Color.red;
		flightMenuInstance.setLabelColor(setLabelColor);
		Color actualSetLabelColor = flightMenuInstance.getLabelColor();
		
		assertEquals(setLabelColor, actualSetLabelColor);
	}

	@Test
	public void testGetButtonColor() {
		Color buttonColor = Color.white;
		Color actualButtonColor = flightMenuInstance.getButtonColor();
		
		assertEquals(buttonColor, actualButtonColor);
	}

	@Test
	public void testSetButtonColor() {
		Color setButtonColor = Color.red;
		flightMenuInstance.setButtonColor(setButtonColor);
		Color actualSetButtonColor = flightMenuInstance.getButtonColor();
		
		assertEquals(setButtonColor, actualSetButtonColor);
	}

	@Test
	public void testSetFlight() {
		Airspace as = new Airspace();
		Flight flight = new Flight(as);
		flightMenuInstance.setFlight(flight);
		
	}

}

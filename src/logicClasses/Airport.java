package logicClasses;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;


public class Airport {
	
	//FIELDS
	private static Image airportImage;
	
	private String airportName = "Nothing"; // {!} needs a name
	
	private float x, y;
	private float runwayHeading = 320;
	private int runwayLength;
	
	
	//CONSTRUCTOR
	
	public int getRunwayLength() {
		return runwayLength - 10;
	}


	Airport() {
		
	}
	
	
	public void init(GameContainer gc) throws SlickException {
		LoadingList.get().add(new DeferredFile("res/graphics/new/airport.png"){
			public void loadFile(String filename) throws SlickException{
                airportImage = new Image(filename);
                x = (stateContainer.Game.MAXIMUMWIDTH-150)/2;
                y = stateContainer.Game.MAXIMUMHEIGHT/2;
                runwayLength = airportImage.getHeight();
            }	
		});
	}
	
	public void render(Graphics g, GameContainer gc) throws SlickException { 
		
		// Airport image centred in middle of airspace
		airportImage.setRotation(runwayHeading);
		airportImage.drawCentered(150+x, y);
	} 
	
	
	@Override
	public String toString(){
		String s = "Airport Name: " + airportName;
		return s;
	}
	
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

	public float getRunwayHeading() {
		return runwayHeading;
	}


}

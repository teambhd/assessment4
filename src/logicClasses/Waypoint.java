package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;

public class Waypoint extends Point {

	static Image nextWaypointImage, waypointImage;


	public Waypoint(double xcoord, double ycoord, String name){
		super(xcoord, ycoord, name);

		//System.out.println("Waypoint " + pointRef + " set:(" + x + "," + y +").");
	}

	// INIT, RENDER

	/**
	 * init: Initialises the variables and resources required for the Waypoint class render (Sets Waypoint Images)
	 * @param gc Slick2d game container
	 * @throws SlickException Slick2d exception handler
	 */

	public void init(GameContainer gc) throws SlickException {
		{
			LoadingList loading = LoadingList.get();
			
			if (waypointImage == null){
				loading.add(new DeferredFile("res/graphics/waypoint.png"){
					public void loadFile(String filename) throws SlickException{
						waypointImage = new Image(filename);
					}
				});
			}
			
			if (nextWaypointImage == null){
				loading.add(new DeferredFile("res/graphics/waypoint_next.png"){
					public void loadFile(String filename) throws SlickException{
						nextWaypointImage = new Image(filename);
					}
				});
			}
			
		}
	}

	/**
	 * render: Render the graphics for the Waypoint class (Draws all Waypoints)
	 * @param g slick2d graphics object
	 * @param airspace object
	 * @throws SlickException Slick2d exception handler
	 */

	public void render(Graphics g, Airspace airspace) throws SlickException {
		Image image;
		if(airspace.getControls().getSelectedFlight() !=null){ // If there is a selected flight use its next waypoint and draw it as next
			if (airspace.getControls().getSelectedFlight().getFlightPlan().getCurrentRoute().indexOf(this)==0){
				image = nextWaypointImage;
			} else {
				image = waypointImage;
			}
		} else {	// If there is no flight then draw the waypoint as not next
			image = waypointImage;
		}
		
		image.draw((int)x-14, (int)y-14,30,30);
		g.setColor(Color.black);
		g.drawString(pointRef, (int)x-3, (int)y-9);

	}



}

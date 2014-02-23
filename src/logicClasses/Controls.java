package logicClasses;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;

public class Controls {

	// FIELDS
	
	private int difficultyValueOfGame; //Sets the difficulty of the control scheme
	public static int
		EASY = 1, NORMAL = 2, HARD = 3;
	
	private FlightMenu menu;
	private boolean mouseHeldDownOnFlight, headingAlreadyChangedByMouse;
	private Flight selectedFlight;
	
	
	// CONSTRUCTOR
	public Controls() {
		//Initializes all boolean values controlling selections to false
		mouseHeldDownOnFlight = false;
		headingAlreadyChangedByMouse = false;
		selectedFlight = null;
	}


	// INIT
	public void init(GameContainer gc) throws SlickException {	
		
		menu = new FlightMenu();
		menu.init();
		menu.setInput(gc.getInput());
	}
	
	

	// METHODS
	
	private double distance (double x1, double y1, double x2, double y2){
		//DONT PANIC, just pythagoras 
		return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
	}
	
	
	/**
	 * changeModeByClickingOnFlight: Handles changing between plan and nav modes by clicking on the selected flight
	 * @param nearestFlight Flight object
	 */
	public void changeModeByClickingOnFlight(){
		selectedFlight.getFlightPlan().setChangingPlan(
				!selectedFlight.getFlightPlan().getChangingPlan());
	}
	
	/**
	 * checkSelected: Handles changing the selected flight and ensures that the flight is a valid selection
	 * Also makes sure that if two flights are intersecting that you only select one, not both
	 * @param pointX
	 * @param pointY
	 * @param airspace
	 */
	public void checkSelected(int pointX, int pointY, Airspace airspace ){

		double minimumDistanceBetweenFlightAndMouseClick;//Distance between where you clicked on the airspace and the closest flight
		Flight nearestFlight;

		// If mouse is being held down don't change selected flight. 
		if (mouseHeldDownOnFlight){
			return;
		}
		else mouseHeldDownOnFlight = true;
	
		//continue only if first click

		// Checking if user is dragging a waypoint they can't change flights
		if (selectedFlight != null){
			if (selectedFlight.getFlightPlan().getDraggingWaypoint()){
				return;
			}
		}

		//continue only if user is not dragging a waypoint
		
		// Working out nearest flight to click
		nearestFlight = null;
		minimumDistanceBetweenFlightAndMouseClick = Integer.MAX_VALUE;
		for (Flight f:  airspace.getListOfFlights()){	
			double d = distance( pointX,pointY, f.getX(),f.getY() );
			if (d < minimumDistanceBetweenFlightAndMouseClick){
				nearestFlight = f;
				minimumDistanceBetweenFlightAndMouseClick = d;
			}
		}

		// Working out whether the nearest flight to click is close enough
		// to be selected.
		if (minimumDistanceBetweenFlightAndMouseClick <= 50){ // If the mouse if further from the flight than 50 then it cannot be selected

			if (nearestFlight == selectedFlight){ //If you are clicking on the currently selected flight then change the airspace mode instead of changing flight
				changeModeByClickingOnFlight();
			}
			//only allow switching flights if not in navigator mode
			else {
				
				if (selectedFlight != null){
					
					//only change selected flight if not in navigator mode
						//OR flight is outside of the circle 
					if (selectedFlight.getFlightPlan().getChangingPlan() 
							|| (distance(selectedFlight.getX(),selectedFlight.getY(),
									nearestFlight.getX(), nearestFlight.getY()) 
								> (menu.getBearingSize()/2 +menu.getSliderWidth()))){
						
						//deselect old flight (if any)
						selectedFlight.setSelected(false);
						selectedFlight.getFlightPlan().setChangingPlan(false);
						
						//select new flight
						nearestFlight.setSelected(true);
						setSelectedFlight(nearestFlight);
					}
				}
				else {
					//set selected flight
					nearestFlight.setSelected(true);
					setSelectedFlight(nearestFlight);
				}
		
			}

		}
	}


	/**
	 * giveHeadingWithMouse: Handles updating the currently selected flights heading by clicking in it's
	 * control circle with the left mouse button
	 * @param pointX X Coordinate of the mouse click
	 * @param pointY Y Coordinate of the mouse click
	 * @param airspace
	 */
	
	public void giveHeadingWithMouse(int pointX, int pointY, Airspace airspace){
		
		double deltaX, deltaY;
		double distanceBetweenMouseAndPlane;
		
		// If mouse is being held down don't change selected flight. 
		if (headingAlreadyChangedByMouse){
			return;
		}
		else{
			headingAlreadyChangedByMouse = true;
		}
		
		//Finding the distance between the mouse click and the plane
		distanceBetweenMouseAndPlane = 
				distance( pointX,pointY, selectedFlight.getX(),selectedFlight.getY() );
		if (distanceBetweenMouseAndPlane < 50) //If the distance between the mouse and the plane is greater than 50 then don't do anything
		{
			deltaY = pointY - selectedFlight.getY();
			deltaX = pointX - selectedFlight.getX();
			double angle = Math.toDegrees(Math.atan2(deltaY, deltaX)); // Find the angle between the current heading and where the mouse was clicked
			angle+=90;
			if (angle < 0) {
				angle += 360;
			}
			selectedFlight.giveHeading((int)Math.round(angle));
		
		}
		
	}
	
	

	// RENDER AND UPDATE



	
	/**
	 * render: Render all of the graphics required by controls
	 * @param g The slick2d graphics object
	 * @param gc The slick2d game container
	 * @throws SlickException
	 */
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if(selectedFlight != null) {
						
			
			if(!selectedFlight.getFlightPlan().getChangingPlan()){
				g.setColor(Color.white);

				menu.render(g, gc);

			}	
			
		}	
	}
	
	public void update(GameContainer gc, Airspace airspace) {
		int posX = Mouse.getX();
		int posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();

		if (selectedFlight != null ){	//if controls are active
			
			//check for mode button presses
			if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				//Plan mode
				if(posX>10&&posX<150&&posY<65&&posY>45){
					selectedFlight.getFlightPlan().setChangingPlan(true);
				}
				
				//navigator mode
				if(posX>10&&posX<150&&posY<95&&posY>75){
					selectedFlight.getFlightPlan().setChangingPlan(false);
				}
			}
				
			// Only allow controls if user isn't changing a plan
			if (!(selectedFlight.getFlightPlan().getChangingPlan()) && selectedFlight.isCommandable()){
				//allow mouse control of flight if not in h
				if(Mouse.isButtonDown(Input.MOUSE_RIGHT_BUTTON) && (difficultyValueOfGame != HARD)){
					giveHeadingWithMouse(posX, posY, airspace);
				}			
			}
			
		}
		
		
		if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
			checkSelected(posX,posY,airspace);

		}
		else{
			mouseHeldDownOnFlight = false;
		}
		
		if (!Mouse.isButtonDown(Input.MOUSE_RIGHT_BUTTON)){
			headingAlreadyChangedByMouse = false;
		}
		
	}
	
	
	//MUTATORS AND ACCESSORS
	public void setSelectedFlight(Flight flight1){
		selectedFlight = flight1;
		menu.setFlight(flight1);
	}
	
	public Flight getSelectedFlight(){
		return selectedFlight;
	}
	
	public void setDifficultyValueOfGame(int value){
		difficultyValueOfGame = value;
		
	}
}




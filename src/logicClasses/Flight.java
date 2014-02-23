package logicClasses;
import static java.lang.Math.PI;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import stateContainer.Game;

public class Flight {

	// FIELDS
	private static Image 
		regularFlightImage, slowFlightImage, fastFlightImage, shadowImage;
	private static double gameScale = 1/1000.0;
	
	private int
		minVelocity = 200, maxVelocity = 400,
		minAltitude = 2000, maxAltitude = 5000;
	private double
		accel = 20/60.0,
		climbRate = 60/60.0,
		turnRate = 30/60.0;	//20 mph per second
	
	private int flightNumber;
	private String flightName;
	private double
		x, y,
		velocity, targetVelocity;
	private double
		currentHeading, targetHeading;
	private int 
		currentAltitude, targetAltitude;
	
	private boolean turningRight, turningLeft;
	
	private Airspace airspace;
	private FlightPlan flightPlan;
	private boolean selected;
	private final static int RADIUS = 30;
	private int closestDistance = Integer.MAX_VALUE; // this is the maximum distance a plane
									  // can be away from the waypoint once it has 
									  // been checked that the plane is inside the waypoint
	private int distanceFromWaypoint;

	private boolean 
		takingOff = false,
		landing = false,
		circling = false,
		partCircling = false,
		finalApproach = false;

	
	// CONSTRUCTOR
	public Flight(Airspace airspace) {
		this.x = 0;
		this.y = 0;
		this.targetAltitude = 0;
		this.targetHeading = 0;
		this.currentHeading = 0;
		this.turningRight = false;
		this.turningLeft = false;
		this.airspace = airspace;
		this.flightPlan = new FlightPlan(airspace, this);
		this.currentAltitude = generateAltitude();
		this.selected = false;
	}

	// METHODS
	
	
	/**
	 * generateAltitude: Randomly assigns one of three different altitudes to a flight
	 * @return A random altitude (either 28000, 29000 or 30000)
	 */

	public int generateAltitude() {	//{!} not converted to using min/max
		if(getFlightPlan().getEntryPoint().isRunway()){
			return 0;
		}else{
		Random rand = new Random();
		int check = rand.nextInt(((maxAltitude-minAltitude)/1000) - 2);
		return minAltitude + (check + 1) * 1000;
		}
	}

/**
 * calculateHeadingToFirstWaypoint: calculates heading between flight's current position and the first waypoint
 * in the flight's plan. The flight's current position will always be it's entrypoint because this method
 * is only called within the newFlight() function in airspace.
 * @param desX - The X coordinate of the waypoint
 * @param dexY - The Y coordinate of the waypoint
 * @return The heading between the flight and first waypoint.
 */
	
	public double calculateHeadingToFirstWaypoint(double desX, double desY) {
		
		double deltaX;
		double deltaY;
		deltaY = desY - this.y;
		deltaX = desX - this.x;
		double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
		angle += 90;
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	/**
	 * turnFlightLeft: sets the target heading to certain amount to to the left of the flight's current heading. 
	 * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightLeft. The
	 * turningLeft boolean is set in order to tell the updateCurrentHeading() method that the flight should turn left 
	 * towards it's target heading.
	 * @param degreeTurnedBy - The amount of degrees you want to turn left by.
	 */
	
	public void turnFlightLeft(int degreeTurnedBy) {

		this.turningRight = false;
		this.turningLeft = true;

		this.targetHeading = Math.round(this.currentHeading) - degreeTurnedBy;
		if(this.targetHeading < 0){
			this.targetHeading = 360 +this.targetHeading;
		}
	}
	
	/**
	 * turnFlightRight: sets the target heading to certain amount to to the right of the flight's current heading. 
	 * When an angle is entered in the textfields found in Controls, the value is passed to turnFlightRight. The
	 * turningRight boolean is set in order to tell the updateCurrentHeading() method that the flight should turn right
	 * towards it's target heading.
	 * @param degreeTurnedBy - The amount of degrees you want to turn right by.
	 */
	
	public void turnFlightRight(int degreeTurnedBy) {

		this.turningLeft = false;
		this.turningRight = true;
		
		this.targetHeading = Math.round(this.currentHeading) + degreeTurnedBy;
		if(this.targetHeading >= 360){
			this.targetHeading = this.targetHeading - 360;
		}


	}
	
	/**
	 * giveHeading: Changes the target heading to newHeading. Whenever a command is issued by the user to change the heading,
	 * the method is passed the value of that command. The heading is always adjusted to a value between 0 and 359. This is 
	 * done using newHeading % 360.
	 * @param newHeading - The heading the flight has been commmanded to fly at.
	 */

	public void giveHeading(int newHeading) {
		this.turningRight = false;
		this.turningLeft = false;
		newHeading = newHeading % 360;
		this.targetHeading = newHeading;
	}
	
	/**
	 * checkIfFlightAtWaypoint: checks whether a flight is close enough to the next waypoint in it's plan
	 * for it to be considered at that waypoint. Update the closestDistance so that it knows how close the plane
	 * was from the waypoint when it leaves the waypoint. This is so the score can be updated correctly
	 * @param Waypoint - The next waypoint in the flight's plan.
	 * @return True if flight is at it's next waypoint and it is moving away from that waypoint.
	 */
	
	public boolean checkIfFlightAtWaypoint(Point waypoint) {
		
		int distanceX;
		int distanceY;
		
		distanceX = (int)(Math.abs(Math.round(this.x) - Math.round(waypoint.getX())));
		distanceY = (int)(Math.abs(Math.round(this.y) - Math.round(waypoint.getY())));

		distanceFromWaypoint = (int)Math.sqrt((int)Math.pow(distanceX,2) + (int)Math.pow(distanceY,2));
		
		// The plane is coming towards the waypoint
		if (closestDistance > distanceFromWaypoint){
				closestDistance = distanceFromWaypoint;
		}	
		
		if ((distanceX <= RADIUS) && (distanceY <= RADIUS)) {
			// The plane is going away from the way point
			if (closestDistance < distanceFromWaypoint){				
				if (waypoint instanceof ExitPoint){
					if (((ExitPoint)waypoint).isRunway()){
						return currentAltitude==0;
					}
					else return true;					
				}
				
				//for any non-exit waypoint
				return true;
			}
		}
		//getting closer OR not close enough
		return false;
	}

	public int minDistanceFromWaypoint(Point waypoint){	
		return closestDistance;
	}
	public void resetMinDistanceFromWaypoint(){
		closestDistance = Integer.MAX_VALUE;
	}
	
	public void takeOff(){
		takingOff = true;
		setTargetVelocity((minVelocity +maxVelocity) /2);
		setTargetAltitude(minAltitude);
	}
	public boolean altToLand(){
		if(currentAltitude <= 720){
			return true;
		}else{
			
			return false;
		}
	}
	public void land(){	
		// if next point is an exit point
		if (!landing){
			if(getFlightPlan().getPointByIndex(0)==getFlightPlan().getExitPoint() && getFlightPlan().getExitPoint().isRunway()){
	
				landing = true;
								
				//point towards exitpoint
				double heading = Math.atan2(flightPlan.getExitPoint().getY() -y, flightPlan.getExitPoint().getX() -x) +PI/2; 
				heading = (heading< 0) ? heading+(2*PI) : heading;
				giveHeading((int)Math.round(Math.toDegrees(heading)));
				setTargetVelocity(velocity);
			}
		}
	}
	
	// DRAWING METHODS
	
	/**
	 * drawFlight: draws the flight at it's current x,y and draws its information around within a circle.
	 * Different images for the flight are used depending on how fast the plane is.
	 * @param g - Graphics libraries required by slick2d.
	 * @param gc - GameContainer required by slick2d.
	 */
	
	public void drawFlight(Graphics g, GameContainer gc ){

		g.setColor(Color.white);
		g.setWorldClip(150, 0, Game.MAXIMUMWIDTH -150, Game.MAXIMUMHEIGHT);


		float shadowScale = (float) (36 - (this.currentAltitude / 1000))/10; // Scale the shadow in accordance to the altitude of the flight
		shadowImage.setRotation((int) currentHeading);
		shadowImage.draw((int) this.x-35, (int) this.y, shadowScale);

		//Depending on a plane's speed, different images for the plane are drawn

		if(velocity <= 275){	//{!} not converted to using min/max

			slowFlightImage.setRotation((int) currentHeading);
			slowFlightImage.draw((int) this.x-10, (int) this.y-10);

		}

		else if(velocity>270 && velocity<340){	//{!} not converted to using min/max

			regularFlightImage.setRotation((int) currentHeading);
			regularFlightImage.draw((int) this.x-10, (int) this.y-10);

		}

		else{
			fastFlightImage.setRotation((int) currentHeading);
			fastFlightImage.draw((int) this.x-10, (int) this.y-10);

		}

		// Drawing Separation Circle

		g.drawOval((int) this.x - 50, (int) this.y - 50, 100, 100);


		// Drawing information around flight
		// If flight is selected then also display current heading

		if (this.selected){
			g.setColor(Color.white);
			g.drawString(this.flightName, (int) this.x-24, (int) this.y-44);
			g.drawString(Math.round(this.currentAltitude) + " ft",(int) this.x-30, (int) this.y + 10);
			g.drawString(Math.round(this.currentHeading) + "°",(int) this.x - 13, (int) this.y + 25);//-15,20

			if (this.flightPlan.getCurrentRoute().size() > 0) {
				g.drawString("Aim: "+this.flightPlan.getPointByIndex(0).getPointRef(),(int) this.x -22, (int)this.y-28);

			}

		}

		// If flight isn't selected then don't display current heading
		else{
			g.setColor(Color.lightGray);
			g.drawString(this.flightName, (int) this.x-24, (int) this.y-44);
			g.drawString(Math.round(this.currentAltitude) + " ft",(int) this.x-30, (int) this.y + 10);

			if (this.flightPlan.getCurrentRoute().size() > 0) {
				g.drawString("Aim: "+this.flightPlan.getPointByIndex(0).getPointRef(),(int) this.x -22, (int)this.y-28);
			}
			g.drawOval((int) this.x - 50, (int) this.y - 50, 100, 100);
		}

		g.setWorldClip(0, 0, Game.MAXIMUMWIDTH, Game.MAXIMUMHEIGHT);

	}
	
	/**
	 * drawSelectedFlightInformation: draws the selected flight's information in the bottom left hand corner.
	 * @param g - Graphics libraries required by slick2d.
	 * @param gc - GameContainer required by slick2d.
	 */
	
	public void drawSelectedFlightInformation(Graphics g, GameContainer gc) {
		
		/*selectedFlightInformationBackgroundImage.draw(0,450);
		g.setColor(Color.white);
		g.drawString(this.flightName,  10, 460);
		g.drawString("Plan: ",  10, 480);
		String plan = "";
		
		for(int i=0; i<this.flightPlan.getCurrentRoute().size(); i++) {
			plan += this.flightPlan.getCurrentRoute().get(i).getPointRef()+", ";
		}
		
		
		g.setColor(Color.white);
		g.drawString(plan, 10, 500);
		g.drawString(Math.round(this.currentAltitude) + " Ft",
			 10, 520);
		g.drawString(Math.round(this.currentHeading) + " DEG",
			10, 540);
		g.drawString(Math.round(velocity) + " MPH",
			10, 560);
			*/
		
	}
	
	// UPDATE METHODS
	
	/**
	 * updateXYCoordinates: updates the x and y values of the plane depending on it's velocity 
	 * and it's current heading. The velocity of the plane is scaled so that it can be used for 
	 * movement in terms of pixels.
	 */

	public void updateXYCoordinates() {
		double vs = velocity *gameScale;
		
		this.x += vs * Math.sin(Math.toRadians(currentHeading));

		this.y -= vs * Math.cos(Math.toRadians(currentHeading));

	}

	/**
	 * updateAltitude(): If the target altitude is higher than the current altitude, increase current altitude.
	 * If target altitude is less than current altitude, decrease current altitude. If current altitude and
	 * target altitude are the same, do nothing.
	 */
	
	public void updateAltitude() {
		if (this.currentAltitude > this.targetAltitude&& !takingOff) {
			this.currentAltitude -= climbRate;
		}

		else if (this.currentAltitude < this.targetAltitude && !takingOff) {
			this.currentAltitude += climbRate;
		}
	}
	
	/**
	 * updateCurrentHeading(): Moves the current heading towards the target heading. If a user has issued
	 * a heading but not specified what way to turn, this method will determine what way it would be quicker 
	 * to turn towards it's target heading.
	 */

	public void updateCurrentHeading() {
	
		if (Math.round(this.targetHeading) != Math.round(this.currentHeading)) {
			

			/*
			 * If plane has been given a heading so no turning direction specified,
			 * below works out whether it should turn left or right to that heading
			 */
			
			if(this.turningRight == false && this.turningLeft == false){

				if (Math.abs(this.targetHeading - this.currentHeading) == 180) {
					this.turningRight = true;
				} 
				
				else if (this.currentHeading + 180 <= 359){
					
					if (this.targetHeading < this.currentHeading + 180 && this.targetHeading > this.currentHeading){
						this.turningRight = true;
					}
					else {
						this.turningLeft = true;
					}
				}
				
				else {
					
					if (this.targetHeading > this.currentHeading - 180 && this.targetHeading < this.currentHeading){
						this.turningLeft = true;
					}
					else {
						this.turningRight = true;
					}
				}

			}
			
			// If plane is already turning right or user has told it to turn right
			
			if (this.turningRight == true) {
				this.currentHeading += turnRate;
				if (Math.round(this.currentHeading) >= 360 && this.targetHeading != 360) {
					this.currentHeading = 0;
				}
			}

			// If plane is already turning left or user has told it to turn left
			
			if (this.turningLeft == true) {
				this.currentHeading -= turnRate;
				if (Math.round(this.currentHeading) <= 0 && this.targetHeading != 0) {
					this.currentHeading = 360;
				}
			}
		}
	}
	
	public void updateVelocity(){
		
		double dv = 0.01*(targetVelocity - velocity);
		if (targetVelocity > velocity) {
			dv = Math.min(dv , accel);
		}else{
			dv = Math.max(dv,-accel);
		}

		velocity += dv;
		
		if (Math.abs(targetVelocity - velocity)< 0.5){
			
			velocity = targetVelocity;
		}
		
		if (takingOff && (Math.abs(minVelocity - velocity)< 0.5)){
			takingOff = false;
		}
	}


	// UPDATE, RENDER, INIT
	
	/**
	 * init: initialises resources such as images.
	 * @param gc - GameContainer required by slick2d.
	 */
	
	public void init(GameContainer gc) throws SlickException {
		if (regularFlightImage == null){
			regularFlightImage = new Image("res/graphics/flight.png");
		}
		if(shadowImage == null){
			shadowImage = new Image("res/graphics/flight_shadow.png");	
		}
		if (slowFlightImage == null){
			slowFlightImage = new Image("res/graphics/flight_slow.png");
		}
		if (fastFlightImage == null){
			fastFlightImage = new Image("res/graphics/flight_fast.png");
		}

	}
	
	
/**
 * Update: calls all the update functions.
 */

	public void update(ScoreTracking score) {
		
		this.updateVelocity();
		this.updateCurrentHeading();
		this.updateXYCoordinates();
		this.updateAltitude();
		this.flightPlan.update(score);
		if (landing){
			if (!circling && !finalApproach)
				if (withinTolerance(currentHeading, targetHeading, turnRate)){
					if (withinTolerance(currentHeading, airspace.getAirport().getRunwayHeading(), 5)){
						//{!} test still lined up and far enough away
						setTargetAltitude(500);
						//System.out.println(this.getFlightName() + " starts circling");
						circling = true;
						partCircling = false;
					}
					else {
						landing = false;
						//System.out.println(this.getFlightName() + " aborts");
					}
				}
			if (circling){
				if (withinTolerance(currentHeading, targetHeading, turnRate)){
					//System.out.println(this.getFlightName() + "finishes a part-circle");
					partCircling = false;
					if (currentAltitude==targetAltitude && withinTolerance(currentHeading,airspace.getAirport().getRunwayHeading(),30)){
						//{!} final  approach starts
						circling = false;
						finalApproach = true;
						setTargetAltitude(0);
						double heading = Math.atan2(flightPlan.getExitPoint().getY() -y, flightPlan.getExitPoint().getX() -x) +PI/2; 
						heading = (heading< 0) ? heading+(2*PI) : heading;
						giveHeading((int)Math.round(Math.toDegrees(heading)));
						//System.out.println(this.getFlightName() + " starts final approach");
					}
				}
				if(circling && !partCircling){
					partCircling = true;
					giveHeading((int)Math.round(currentHeading + 120));
					//System.out.println(this.getFlightName() + " starts new part-circle");
				}
			}
						
			
		}
	}
	
public boolean withinTolerance(double x1, double x2,double tolerance){
	return Math.abs(x1 - x2) <= tolerance;
}
/**
 * render: draw's all elements of the flight and it's information.
 * @param g - Graphics libraries required by slick2d.
 * @param gc - GameContainer required by slick2d.
 */


	public void render(Graphics g, GameContainer gc) throws SlickException {
		
		this.drawFlight(g, gc);
		this.flightPlan.render(g,gc);

		if(this.selected) {
			this.drawSelectedFlightInformation(g, gc);
			

		}
		
	}
	

	// MUTATORS AND ACCESSORS
	

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getCurrentHeading() {
		return this.currentHeading;
	}

	public void setCurrentHeading(double currentHeading) {
		this.currentHeading = currentHeading;
	}

	public double getTargetHeading() {
		return this.targetHeading;
	}

	public void setTargetHeading(double targetHeading) {
		this.targetHeading = targetHeading;
	}

	public int getTargetAltitude() {
		return this.targetAltitude;
	}

	public void setTargetAltitude(int targetAltitude) {
		this.targetAltitude = targetAltitude;
	}

	public int getAltitude() {
		return this.currentAltitude;
	}

	public void setAltitude(int altitude) {
		this.currentAltitude = altitude;
	}
	
	public boolean isGrounded(){
		return (getAltitude() ==0);
	}
	public Boolean isCommandable(){
		return (!isGrounded() && !landing);
	}
	
	public int getMinVelocity() {
		return minVelocity;
	}

	public int getMaxVelocity() {
		return maxVelocity;
	}

	public int getMinAltitude() {
		return minAltitude;
	}

	public int getMaxAltitude() {
		return maxAltitude;
	}

	public boolean getTurningRight() {
		return this.turningRight;
	}

	public void setTurningRight(boolean turningRight) {
		this.turningRight = turningRight;
	}

	public boolean getTurningLeft() {
		return this.turningLeft;
	}

	public void setTurningLeft(boolean turningLeft) {
		this.turningLeft = turningLeft;
	}

	public void setFlightNum(int i) {
		this.flightNumber = i;
	}

	public int getFlightNum() {
		return flightNumber;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	// toString function to display a flight object so we can read it
	@Override
	public String toString() {
		return "X: " + this.x + " Y: " + this.y + " Flight Number: "
				+ this.flightNumber;
	}

	public int getCurrentAltitude() {
		return currentAltitude;
	}
	public void setCurrentAltitude(int currentAltitude) {
		this.currentAltitude = currentAltitude;
	}
	
	public double getVelocity() {
		return velocity;
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public void setTargetVelocity(double velocity){
		this.targetVelocity = velocity;
	}
	
	public double getTargetVelocity(){
		return targetVelocity;
	}
	public FlightPlan getFlightPlan() {
		return flightPlan;
	}
	

	
	public Airspace getAirspace(){
		return airspace;
	}
	
	public boolean getSelected(){
		return this.selected;
	}



}

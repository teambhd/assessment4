package logicClasses;

import java.awt.Font;
import java.awt.geom.Point2D;

import static java.lang.Math.PI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;


public class FlightMenu implements MouseListener{
	private static Image	//base images
		sliderBase, sliderRingBase, sliderIndicator, sliderIndicatorSelect,
		button, buttonSelect;

	private int	//graphics data
		altSize = 100, speedSize = 100, headingSize = 120,	//slider lengths
		sliderWidth = 12, indicatorSize = 20,	//track and indicator sizes
		buttonWidth = 55, buttonHeight = 25,	//buttonSizes
		spacingSize = 6;	//spacing between components

	private Image	//scaled instance copies
		altBase, speedBase, headingBase, aIndicator, aIndicatorSelect,
		aButton, aButtonSelect;
	private TrueTypeFont
		labelFont = new TrueTypeFont(new Font(Font.SANS_SERIF, Font.BOLD, 10), false),
		buttonFont = new TrueTypeFont(new Font(Font.SANS_SERIF, Font.BOLD, 11), false);
	private Color
		labelColor = Color.white,	//slider labels
		buttonColor = Color.white,	//button labels
		markerColor = Color.red;	//current position markers
	
	private Point2D.Float	//cached graphics position data
		altPos, speedPos, headingPos, cmdPos, abortPos,
		altMarkerPos, speedMarkerPos,
		altIndicatorPos, speedIndicatorPos, headingIndicatorPos;
		
	private Input input;
	private Flight flight;	//bound Flight
	
	private int mode = NONE;	//active subcomponent
	private static final int
		NONE = 0, ALT = 1, SPEED = 2, HEADING = 3, CMD = 4, ABORT = 5;
	private double	//normalised indicator positions, range 0-1	|	heading in radians
		altIndicator, speedIndicator, headingIndicator;


	public FlightMenu() {
		//initialise objects, initialise layout
		altPos = new Point2D.Float();
		speedPos = new Point2D.Float();
		headingPos = new Point2D.Float();
		cmdPos = new Point2D.Float();
		abortPos = new Point2D.Float();
		altMarkerPos = new Point2D.Float();
		speedMarkerPos= new Point2D.Float();
		altIndicatorPos = new Point2D.Float();
		speedIndicatorPos = new Point2D.Float();
		headingIndicatorPos = new Point2D.Float();
		position();
	}


	public void init() throws SlickException{
		//load images if needed
		if (sliderBase == null)
			sliderBase = new Image("res/graphics/FlightMenu/rectangle_slider.png");
		if (sliderRingBase == null)
			sliderRingBase = new Image("res/graphics/FlightMenu/circle_slider.png");
		if (sliderIndicator == null)
			sliderIndicator = new Image("res/graphics/FlightMenu/slider_element.png");
		if (button == null)
			button = new Image("res/graphics/FlightMenu/button_bg.png");
		//{!} unimplemented graphics; duplicating existing:
		sliderIndicatorSelect = sliderIndicator;
		buttonSelect = button;

	}

	public void render(Graphics g, GameContainer gc) throws SlickException {
		if (flight != null){
			//create scaled copies of images if invalidated
			if (altBase == null){
				altBase = sliderBase.getScaledCopy(altSize, sliderWidth);
				altBase.setCenterOfRotation(0, 0);
				altBase.setRotation(90);	//{!} will leave image positioned sliderWidth to the left
			}
			if (speedBase == null)
				speedBase = sliderBase.getScaledCopy(speedSize, sliderWidth);
			if (headingBase == null)
				headingBase = sliderRingBase.getScaledCopy(headingSize, headingSize);
			if (aIndicator == null)
				aIndicator = sliderIndicator.getScaledCopy(indicatorSize, indicatorSize);
			if (aIndicatorSelect == null)
				aIndicatorSelect = sliderIndicatorSelect.getScaledCopy(indicatorSize, indicatorSize);
			if (aButton == null)
				aButton = button.getScaledCopy(buttonWidth, buttonHeight);
			if (aButtonSelect == null)
				aButtonSelect = buttonSelect.getScaledCopy(buttonWidth, buttonHeight);
			
			setMarkerPos();	//get most recent flight positions
			g.setColor(markerColor);
			
			//{!} constrain positions
	
			
			if (flight.isCommandable()){
				//draw altitude slider and labels
				drawImage(altBase,  new Point2D.Float(altPos.x +sliderWidth, altPos.y));
					//account for image mispositioning after rotation
				if (altMarkerPos.y == constrain(altMarkerPos.y, altPos.y, altPos.y +altSize))
					drawLine(g, altMarkerPos.x, altMarkerPos.y, altMarkerPos.x +sliderWidth, altMarkerPos.y);
				drawString(String.valueOf(flight.getMinAltitude()),
				           labelFont, labelColor,
				           altPos.x, altPos.y +altSize);	//centred on bottom left edge of slider 
				drawString(String.valueOf(flight.getMaxAltitude()),
				           labelFont, labelColor,
				           altPos.x, altPos.y);	//centred on top left edge of slider
				if (ALT == mode)
					drawImage(aIndicatorSelect, altIndicatorPos);
				else drawImage(aIndicator, altIndicatorPos);
				
				//draw speed slider and labels
				drawImage(speedBase, speedPos);
				if (speedMarkerPos.x == constrain(speedMarkerPos.x, speedPos.x, speedPos.x +speedSize))
					drawLine(g, speedMarkerPos.x, speedMarkerPos.y, speedMarkerPos.x, speedMarkerPos.y +sliderWidth);
				drawString(String.valueOf(flight.getMinVelocity()),
				           labelFont, labelColor,
				           speedPos.x, speedPos.y +sliderWidth);	//centred on bottom left edge of slider 
				drawString(String.valueOf(flight.getMaxVelocity()),
				           labelFont, labelColor,
				           speedPos.x +speedSize, speedPos.y +sliderWidth);	//centred on bottom right edge of slider
				if (SPEED == mode)
					drawImage(aIndicatorSelect, speedIndicatorPos);
				else drawImage(aIndicator, speedIndicatorPos);
	
				//draw heading slider and labels
				drawImage(headingBase, headingPos);
				if (HEADING == mode)
					drawImage(aIndicatorSelect, headingIndicatorPos);
				else drawImage(aIndicator, headingIndicatorPos);
			}
			
			//draw command button and label
			if (CMD == mode)
				drawImage(aButtonSelect, cmdPos);
			else drawImage(aButton, cmdPos);
			{	//draw button text
				String cmdString;
				if (flight.getAltitude() == 0)	//detect context
					cmdString = "Take Off";
				else cmdString = "Land";
				drawString(cmdString, buttonFont, buttonColor, 
				           cmdPos.x +(buttonWidth/2.0f), cmdPos.y +(buttonHeight/2.0f));
			}
			
			if(flight.isCommandable()){
				//draw abort button and label
				if (ABORT == mode)
					drawImage(aButtonSelect, abortPos);
				else drawImage(aButton, abortPos);
				drawString("Abort", buttonFont, buttonColor, 
				           abortPos.x +(buttonWidth/2.0f), abortPos.y +(buttonHeight/2.0f));
			}
			
		}
	}

	private void drawImage(Image image, Point2D pos){
		image.draw((float)( pos.getX() +flight.getX() ), 
		           (float)( pos.getY() +flight.getY() ) );
	}
	
	private void drawString(String str, TrueTypeFont font, Color color, float x, float y){
		font.drawString((float)( x -(font.getWidth(str)/2.0) +flight.getX() ),
		                (float)( y -(font.getHeight()/2.0) +flight.getY() ),
		                str, color);
	}
	
	private void drawLine(Graphics g, float x1, float y1, float x2, float y2){
		float	ox = (float)flight.getX(), 
				oy = (float)flight.getY();
		g.drawLine(x1 +ox, y1 +oy, x2 +ox, y2 +oy);
	}

	private void position(){
		mode = NONE;	//invalidate any current mouse movements

		double r = headingSize /2.0;

		//base position at centre of heading slider
		headingPos.setLocation(-r, -r);

		//position altitude slider to left of heading slider, centred
		altPos.setLocation(-r -spacingSize -sliderWidth, -altSize/2.0);

		//position speed slider to below heading slider, centred
		speedPos.setLocation(-speedSize/2.0, r +spacingSize);

		//position buttons to left of altitude slider, centred
		cmdPos.setLocation(altPos.x -spacingSize -buttonWidth, -spacingSize/2.0 -buttonHeight);
		abortPos.setLocation(altPos.x -spacingSize -buttonWidth, spacingSize/2.0);

		setIndicatorPos();
	}
	
	private void setMarkerPos(){
		if (flight != null){
			altMarkerPos.setLocation(	//rescale from altitude to pixels
					altPos.x, 
					altPos.y +multScale(
							normalScale(flight.getAltitude(), flight.getMinAltitude(), flight.getMaxAltitude()),
							altSize, 0));
			
			speedMarkerPos.setLocation(	//rescale from velocity to pixels
					speedPos.x +multScale(
							normalScale(flight.getVelocity(), flight.getMinVelocity(), flight.getMaxVelocity()), 
							0, speedSize), 
					speedPos.y);
		}
	}

	private void setIndicatorPos(){
		if (flight != null){
			double	//slider half-widths ("radii")
				hr = headingSize/2.0,
				sr = sliderWidth/2.0,
				ir = indicatorSize/2.0;

			altIndicatorPos.setLocation(
					altPos.x +sr -ir,
					altPos.y +multScale(altIndicator, altSize, 0) -ir);
			
			speedIndicatorPos.setLocation(
					speedPos.x +multScale(speedIndicator, 0, speedSize) -ir,	
					speedPos.y +sr -ir);

			headingIndicatorPos.setLocation(
					(hr-sr)*Math.sin(headingIndicator) -ir,
					(hr-sr)*-Math.cos(headingIndicator) -ir);
		}
	}

	private double normalScale(double pos, double min, double max){
		//return the position of pos on the scale min-max, normalised to 0-1
		return (pos-min) / (max-min);
	}
	
	private double multScale(double normPos, double min, double max){
		//return the actual position on the scale min-max, of normalised position normPos
		return min +(normPos * (max - min));
	}
	
	private int constrain(int val, int min, int max){
		//return value no more than min, no more than max, otherwise val
		return (val <= min) ? min : 
				(val >= max) ? max : val;
	}
	
	private double constrain(double val, double min, double max){
		//return value no more than min, no more than max, otherwise val
		return (val <= min) ? min : 
				(val >= max) ? max : val;
	}
	
	private Boolean inIndicator(Point2D pos, int mouseX, int mouseY){
		int	x = (int)Math.round(pos.getX()),
			y = (int)Math.round(pos.getY());
		//normalise to internal coordinates
		mouseX -= flight.getX(); 
		mouseY -= flight.getY();

		return (mouseX>x && mouseX<(x+indicatorSize) && 
				mouseY>y && mouseY<(y+indicatorSize));
	}
	
	private Boolean inButton(Point2D pos, int mouseX, int mouseY){
		int	x = (int)Math.round(pos.getX()),
			y = (int)Math.round(pos.getY());
		//normalise to internal coordinates
		mouseX -= flight.getX();
		mouseY -= flight.getY();

		return (mouseX>x && mouseX<(x+buttonWidth) && 
				mouseY>y && mouseY<(y+buttonHeight));
	}

	private void eventTargetAltitude(double altitude){
		int targetAltitude = (int)Math.round(
				multScale(altitude, flight.getMinAltitude(), flight.getMaxAltitude()));
		System.out.println(String.format("altitude := %1$4d", targetAltitude));
		flight.setTargetAltitude((int)Math.round(targetAltitude));
	}
	
	private void eventTargetSpeed(double speed){
		double targetSpeed = multScale(speed, flight.getMinVelocity(), flight.getMaxVelocity());
		System.out.println(String.format("speed := %1$3f", targetSpeed));
		flight.setTargetVelocity(targetSpeed);
		//{!} nothing available to change at this time
	}
	
	private void eventTargetHeading(double heading){
		int targetHeading = (int)(Math.round(Math.toDegrees(heading)));
		System.out.println(String.format("heading := %1$3d", targetHeading));
		flight.giveHeading(targetHeading);	//NOT setTargetHeading
	}

	private void eventAbort(){
		System.out.println("abort");
		//{!} set flight parameters
	}

	private void eventLand(){
		System.out.println("land");
		//{!} set flight parameters
		flight.land();
	}

	private void eventTakeoff(){
		System.out.println("takeoff");
		flight.takeOff();
		//{!} refresh flight parameters
		flight.getAirspace().getControls().setSelectedFlight(flight);
	}
	

	@Override
	public void inputStarted() {};
	
	@Override
	public void inputEnded() {};

	@Override
	public boolean isAcceptingInput() {
		return (flight != null);
	}

	@Override
	public void setInput(Input input) {
		//cleanly transfer to new input
		if (this.input != null)
			this.input.removeMouseListener(this);
		this.input = input;
		if (input != null)
			input.addMouseListener(this);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (Input.MOUSE_LEFT_BUTTON == button){
			//check for which component mouse is in (if any)
			if (inIndicator(altIndicatorPos, x, y))
				mode = ALT;
			else if (inIndicator(speedIndicatorPos, x, y))
				mode = SPEED;
			else if (inIndicator(headingIndicatorPos, x, y))
				mode = HEADING;
			else if (inButton(cmdPos, x, y))
				mode = CMD;
			else if (inButton(abortPos, x, y))
				mode = ABORT;
			else
				mode = NONE;
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		switch (mode){
		case NONE:	//nothing to check
			break;

		//reposition sliders
		case ALT: {
			//calculate y position relative to top of scale
			int y = (int)Math.round( newy -flight.getY() -altPos.y);
			y = constrain(y, 0, altSize);	//cap y to within bounds of scale
			altIndicator = normalScale(y, altSize, 0);	//invert scale direction
			setIndicatorPos();
			break;
		}
		case SPEED: {
			//calculate x position relative to left of scale
			int x = (int)Math.round( newx -flight.getX() -speedPos.x);
			x = constrain(x, 0, speedSize);	//cap x to within bounds of scale
			speedIndicator = normalScale(x, 0, speedSize);
			setIndicatorPos();
			break;
		}
		case HEADING: {
			double	//positions relative to centre of scale
				x = newx -flight.getX(),
				y = newy -flight.getY();
			headingIndicator = Math.atan2(y, x) +PI/2;	//correct for polar coordinates
			headingIndicator = 
					(headingIndicator < 0) ? headingIndicator+(2*PI) : headingIndicator;
			setIndicatorPos();
			break;
		}

		//check if should invalidate button presses
		case CMD:
			if (!inButton(cmdPos, newx, newy))
				mode = NONE;
			break;
		case ABORT:
			if (!inButton(abortPos, newx, newy))
				mode = NONE;
			break;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		//disable invalid commands
		if ((mode != CMD) && !flight.isCommandable())
			mode = NONE;
		
		if (Input.MOUSE_LEFT_BUTTON == button){
			switch (mode){
			case NONE:	//nothing to check
				break;
				
			//release sliders
			case ALT:
				eventTargetAltitude(altIndicator);
				break;
			case SPEED:
				eventTargetSpeed(speedIndicator);
				break;
			case HEADING:
				eventTargetHeading(headingIndicator);
				break;
				
			//release buttons
			case CMD:
				//interpret context
				if (flight.getAltitude() == 0)
					eventTakeoff();
				else eventLand();
				break;
			case ABORT:
				eventAbort();
				break;				
			}
			mode = NONE;
			setIndicatorPos();
		}
	}

	@Override
	public void mouseWheelMoved(int change) {}
	
	
	public int getAltSize() {
		return altSize;
	}
	public void setAltSize(int altSize) {
		this.altSize = altSize;
		altBase = null;
		position();
	}

	public int getSpeedSize() {
		return speedSize;
	}
	public void setSpeedSize(int speedSize) {
		this.speedSize = speedSize;
		speedBase = null;
		position();
	}

	public int getBearingSize() {
		return headingSize;
	}
	public void setHeadingSize(int headingSize) {
		this.headingSize = headingSize;
		headingBase = null;
		position();
	}

	public int getSliderWidth() {
		return sliderWidth;
	}
	public void setSliderWidth(int sliderWidth) {
		this.sliderWidth = sliderWidth;
		altBase = null;
		speedBase = null;
		position();
	}

	public int getIndicatorSize() {
		return indicatorSize;
	}
	public void setIndicatorSize(int indicatorSize) {
		this.indicatorSize = indicatorSize;
		aIndicator = null;
		aIndicatorSelect = null;
		position();
	}

	public int getButtonWidth() {
		return buttonWidth;
	}
	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
		aButton = null;
		aButtonSelect = null;
		position();
	}

	public int getButtonHeight() {
		return buttonHeight;
	}
	public void setButtonHeight(int buttonHeight) {
		this.buttonHeight = buttonHeight;
		aButton = null;
		aButtonSelect = null;
		position();
	}

	public int getSpacingSize() {
		return spacingSize;
	}
	public void setSpacingSize(int spacingSize) {
		this.spacingSize = spacingSize;
		position();
	}

	public TrueTypeFont getLabelFont() {
		return labelFont;
	}
	public void setLabelFont(TrueTypeFont labelFont) {
		this.labelFont = labelFont;
	}

	public TrueTypeFont getButtonFont() {
		return buttonFont;
	}
	public void setButtonFont(TrueTypeFont buttonFont) {
		this.buttonFont = buttonFont;
	}

	public Color getLabelColor() {
		return labelColor;
	}
	public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
	}

	public Color getButtonColor() {
		return buttonColor;
	}
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}
	
	public Color getMarkerColor() {
		return markerColor;
	}
	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}

	public void setFlight(Flight flight) {
		mode = NONE;
		this.flight = flight;
		if (flight != null){
			altIndicator = normalScale(flight.getTargetAltitude(), flight.getMinAltitude(),
			                           flight.getMaxAltitude());
			speedIndicator = normalScale(flight.getTargetVelocity(), flight.getMinVelocity(),
			                             flight.getMaxVelocity());
			headingIndicator = Math.toRadians(flight.getTargetHeading());
			setIndicatorPos();
		}
	}

}

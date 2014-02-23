package states;

import java.awt.Font;
import java.io.InputStream;

import logicClasses.Achievements;
import logicClasses.Airspace;
import logicClasses.Controls;
import logicClasses.Flight;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Image;

import util.DeferredFile;


public class PlayState extends BasicGameState {
	private static Image 
		easyButton, mediumButton,hardButton,  
		easyHover, mediumHover, hardHover,  
		backgroundImage, difficultyBackground,
		statusBarImage, clockImage, windImage,
		flightIcon,
		cursorImg;
	private static Sound endOfGameSound;
	private static Music gameplayMusic;
	private static TrueTypeFont
		font, panelFont;	
	public static float time;

	private Airspace airspace;
	private String stringTime;
	private boolean settingDifficulty, gameEnded;
	
	private Achievements achievement;
	private String achievementMessage = "";

	public PlayState(int state) {
		achievement = new Achievements();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		gameEnded = false;
		settingDifficulty = true;
		time = 0;
		airspace = new Airspace();
		this.stringTime="";
		
		gc.setAlwaysRender(true);
		gc.setUpdateOnlyWhenVisible(true);
		// Set mouse cursor
		//gc.setMouseCursor("res/graphics/cross.png",12,12);
	
		
		// Font
		
		{
			LoadingList loading = LoadingList.get();
			
			loading.add(new DeferredFile("res/blue_highway_font/bluehigh.ttf"){
				public void loadFile(String filename) {
					InputStream inputStream = ResourceLoader.getResourceAsStream(filename);
					try {
						Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
						font = new TrueTypeFont(awtFont.deriveFont(20f), true);
						panelFont = new TrueTypeFont(awtFont.deriveFont(14f), true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// Music
			loading.add(new DeferredFile("res/music/new/muzikele.ogg"){
				public void loadFile(String filename) throws SlickException{
					gameplayMusic = new Music(filename);
				}
			});
			
			loading.add(new DeferredFile("res/music/new/Big Explosion.ogg"){
				public void loadFile(String filename) throws SlickException{
					endOfGameSound = new Sound(filename);
				}
			});

			//Images
			loading.add(new DeferredFile("res/graphics/new/control_bar_vertical.png"){
				public void loadFile(String filename) throws SlickException{
					statusBarImage = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/graphics/clock.png"){
				public void loadFile(String filename) throws SlickException{
					clockImage = new Image(filename);
				}
			});
			
			loading.add(new DeferredFile("res/graphics/new/wind_indicator.png"){
				public void loadFile(String filename) throws SlickException{
					windImage = new Image(filename);
				}
			});
			
			loading.add(new DeferredFile("res/graphics/new/control_bar_plane.png"){		
				public void loadFile(String filename) throws SlickException{	
					flightIcon = new Image(filename);
				}	
			});		

			loading.add(new DeferredFile("res/graphics/new/background.png"){
				public void loadFile(String filename) throws SlickException{
					backgroundImage = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/difficulty.png"){
				public void loadFile(String filename) throws SlickException{
					difficultyBackground = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/easy.png"){
				public void loadFile(String filename) throws SlickException{
					easyButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/easy_hover.png"){ 
				public void loadFile(String filename) throws SlickException{
					easyHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/medium.png"){
				public void loadFile(String filename) throws SlickException{
					mediumButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/medium_hover.png"){
				public void loadFile(String filename) throws SlickException{
					mediumHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/hard.png"){
				public void loadFile(String filename) throws SlickException{
					hardButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/hard_hover.png"){
				public void loadFile(String filename) throws SlickException{
					hardHover = new Image(filename);
				}
			});
		}
		
		//initialise the airspace object;
		//Waypoints
		airspace.newWaypoint(350, 150, "A");
		airspace.newWaypoint(400, 470, "B");
		airspace.newWaypoint(700, 60,  "C");
		airspace.newWaypoint(800, 320, "D");
		airspace.newWaypoint(600, 418, "E");
		airspace.newWaypoint(500, 220, "F");
		airspace.newWaypoint(950, 188, "G");
		airspace.newWaypoint(1050, 272,"H");
		airspace.newWaypoint(900, 420, "I");
		airspace.newWaypoint(240, 250, "J");
		
	
		//EntryPoints
		airspace.newEntryPoint(150, 400);
		airspace.newEntryPoint(1200, 200);
		airspace.newEntryPoint(600, 0);
		airspace.newEntryPoint(760, 405);
		// Exit Points
		airspace.newExitPoint(800, 0, "1");
		airspace.newExitPoint(150, 200, "2");
		airspace.newExitPoint(1200, 300, "3");
		airspace.newExitPoint(590,195,"4");
				
	    airspace.init(gc);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		// Checks whether the user is still choosing the difficulty
		
		if(settingDifficulty){

			int posX = Mouse.getX();
			int posY= stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();
				//Fixing posY to reflect graphics coords

			difficultyBackground.draw(0,0);

			if (posX>100 && posX<216 && posY>300 && posY<354)
				easyHover.draw(100,300);
			else easyButton.draw(100,300);
			
			if (posX>100 && posX<284 && posY>400 && posY<454)
				mediumHover.draw(100,400);
			else mediumButton.draw(100,400);
			
			if (posX>100 && posX<227 && posY>500 && posY<554)
				hardHover.draw(100,500);
			else hardButton.draw(100,500);
		}
		
		else{	//main game
			//set font for the rest of the render
			g.setFont(font);
			
			// Drawing Side Images
			backgroundImage.draw(150,0);
			statusBarImage.draw(0,0);
			
			// Drawing Airspace and elements within it
			g.setColor(Color.white);
			airspace.render(g, gc);
					
			// Drawing Clock and Time
			g.setColor(Color.white);
			clockImage.draw(0,5);
			g.drawString(stringTime, 25, 10);
			
			// Drawing Score
			g.drawString(airspace.getScore().toString(), 10, 35);
						
			{	//draw flight information panels	
				int baseY = 60;	
				for (Flight f: airspace.getListOfFlights()){	
					renderFlightPanel(f, g, baseY);
					baseY += 50;
				}	
			}		
			
			//drawing wind direction
			windImage.setRotation(windImage.getRotation() +((float)Math.cos(time/2999.0) +(float)Math.sin(time/1009.0))/3);
				//for now, set wind direction pseudo-randomly
			windImage.draw(14, 550);
			g.drawString("Wind:", 60, 550);
			g.drawString(String.valueOf(Math.round(windImage.getRotation())), 65, 565);
		
			
			// Drawing Achievements
			g.drawString(airspace.getScore().scoreAchievement(), 
					stateContainer.Game.MAXIMUMWIDTH -font.getWidth(airspace.getScore().scoreAchievement()) -10, 30);
			g.drawString(achievementMessage, 
					stateContainer.Game.MAXIMUMWIDTH -10 -font.getWidth(achievementMessage), 40);
		}	
	}
	
	private void renderFlightPanel(Flight f, Graphics g, int baseY){						
		//draw border if flight is selected					
		if (f.getSelected()){					
			g.drawRoundRect(1, baseY, 135, 50, 3);				
		}					
							
		int h = panelFont.getHeight();					
							
		//draw icon, rotated to match plane					
		flightIcon.setRotation((float)f.getCurrentHeading());					
		flightIcon.draw(7, 7 +baseY);					
		//draw flight name at bottom of box					
		panelFont.drawString(4, 50 -h +baseY, f.getFlightName());					
							
		String[] data = new String[] {					
				"Plan: " +f.getFlightPlan().toString(),			
				"Speed: " +String.valueOf(Math.round(f.getVelocity())) +" mph",			
				"Altitude: " +String.valueOf(Math.round(f.getCurrentAltitude())) +" ft"			
		};					
							
		baseY = baseY +3;					
		for (String str: data){					
			panelFont.drawString(40, baseY, str);				
			baseY += h;				
		}					
							
	}						
							
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		// Checks if the game has been retried and if it has resets the airspace
		
		if (gameEnded){
	
			airspace.resetAirspace();
	    	time = 0;
	    	gameEnded = false;
	    	settingDifficulty = true;
	    	airspace.getScore().resetScore();
		}
		
		// Checks whether the user is still choosing the difficulty
		
		if(settingDifficulty){
		
			int posX = Mouse.getX();
			int posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();
			
			if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				if((posX>100&&posX<216) && (posY>300&&posY<354)) {
					
					airspace.setDifficultyValueOfGame(1);
					airspace.getControls().setDifficultyValueOfGame(Controls.EASY);
					airspace.createAndSetSeparationRules();
					settingDifficulty = false;			
				}
				
				
				if((posX>100&&posX<284) && (posY>400&&posY<454)) {
					
					airspace.setDifficultyValueOfGame(2);
					airspace.getControls().setDifficultyValueOfGame(Controls.NORMAL);
					airspace.createAndSetSeparationRules();
					settingDifficulty = false;	
				}
				
				
				if((posX>100&&posX<227) && (posY>500&&posY<554)) {
					
					airspace.setDifficultyValueOfGame(3);
					airspace.getControls().setDifficultyValueOfGame(Controls.HARD);
					airspace.createAndSetSeparationRules();
					settingDifficulty = false;
				}	
			}
		}
		
		else{	//main game
			
			// Updating Clock and Time
			
			time += delta;
			achievement.timeAchievement((int)time);
			float decMins=time/1000/60;
			int mins = (int) decMins;
			float decSecs=decMins-mins;
				
			int secs = Math.round(decSecs*60);
				
			String stringMins="";
			String stringSecs="";
			if(secs>=60){
				secs -= 60;
				mins+=1;
				// {!} should do +60 score every minute(possibly) 
				//     - after 3 minutes adds on 2 less points every time?
				airspace.getScore().updateTimeScore();
			}
			if(mins<10) {
				stringMins="0"+mins;
			}
			else {
				stringMins=String.valueOf(mins);
			}
			if(secs<10) {
				stringSecs="0"+secs;
			}
			else {
				stringSecs=String.valueOf(secs);
			}
						
			this.stringTime=stringMins+":"+stringSecs;
						
						
			// Updating Airspace
						
			airspace.newFlight(gc);
			airspace.update(gc);
			if (airspace.getSeparationRules().getGameOverViolation() == true){
				achievementMessage = achievement.crashAchievement((int)time); //pass the game time as of game over into the crashAchievement
				airspace.getSeparationRules().setGameOverViolation(false);
				airspace.resetAirspace();
				gameplayMusic.stop();
				endOfGameSound.play();
				sbg.enterState(stateContainer.Game.GAMEOVERSTATE);
				gameEnded = true;
							
			}					
			
			Input input = gc.getInput();
						
			// Checking For Pause Screen requested in game
						
			if (input.isKeyPressed(Input.KEY_P)) {
				sbg.enterState(stateContainer.Game.PAUSESTATE);
			}			
						
			if (!gameplayMusic.playing()){
				//Loops gameplay music based on random number created in init
							
				gameplayMusic.loop(1.0f, 0.5f);
			}			
		}
	}


	@Override
	public int getID() {
		return stateContainer.Game.PLAYSTATE;
	}

	public Airspace getAirspace() {
		return airspace;
	}

	public void setAirspace(Airspace airspace) {
		this.airspace = airspace;
	}
	


}
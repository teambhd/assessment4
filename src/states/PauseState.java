package states;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.lwjgl.input.Mouse;

import util.DeferredFile;

public class PauseState extends BasicGameState {
	
	private static Image
		nextPageButton, previousPageButton, menuButton, quitButton, backButton,
		nextPageButtonHover, previousPageButtonHover, quitButtonHover, backButtonHover,
		pauseBackgroundPage1, pauseBackgroundPage2;
	//private static TrueTypeFont font;
	
	private int pageNumber;

	
	public PauseState(int state) {
		
	}

	
	@Override
	public void init(GameContainer gc, StateBasedGame sbj) throws SlickException {
		pageNumber = 1;
		
		{
			LoadingList loading = LoadingList.get();

			loading.add(new DeferredFile("res/menu_graphics/new/pause1.png"){
				public void loadFile(String filename) throws SlickException{
					pauseBackgroundPage1 = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/pause2.png"){
				public void loadFile(String filename) throws SlickException{
					pauseBackgroundPage2 = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/next_page.png"){
				public void loadFile(String filename) throws SlickException{
					nextPageButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/back.png"){
				public void loadFile(String filename) throws SlickException{
					backButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/back_hover.png"){
				public void loadFile(String filename) throws SlickException{
					backButtonHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/next_page_hover.png"){
				public void loadFile(String filename) throws SlickException{
					nextPageButtonHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/previous_page.png"){
				public void loadFile(String filename) throws SlickException{
					previousPageButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/previous_page_hover.png"){
				public void loadFile(String filename) throws SlickException{
					previousPageButtonHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/menu_button.png"){
				public void loadFile(String filename) throws SlickException{
					menuButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/quit_button.png"){
				public void loadFile(String filename) throws SlickException{
					quitButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/quit_hover.png"){
				public void loadFile(String filename) throws SlickException{
					quitButtonHover = new Image(filename);
				}
			});
		}
			
		/*InputStream inputStream = ResourceLoader.getResourceAsStream("res/blue_highway_font/bluehigh.ttf");
		Font awtFont= Font.createFont(Font.TRUETYPE_FONT, inputStream);
		awtFont = awtFont.deriveFont(20f);
		font = new TrueTypeFont(awtFont, false);*/
						
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		int	posX = Mouse.getX(),
			posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();
		
		if (pageNumber == 1) {
			
			pauseBackgroundPage1.draw(0,0);
			
			if (posX>1020 && posX<1150 && posY>270 && posY<330)
				nextPageButtonHover.draw(1030,280);
			else nextPageButton.draw(1030,280);
			
		}
		else if (pageNumber == 2){
						
			pauseBackgroundPage2.draw(0,0);

			if (posX>50 && posX<240 && posY>280 && posY<320) 
				previousPageButtonHover.draw(30,280);
			else previousPageButton.draw(30,280);				
		}
		
		if (posX>20 && posX<40 && posY>20 && posY<40) 
			backButtonHover.draw(20,20);
		else backButton.draw(20,20);
		
		if (posX>1150 && posX<1170 && posY>550 && posY<580) 
			quitButtonHover.draw(1150,550);
		else quitButton.draw(1150,550);
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		int	posX = Mouse.getX(),
			posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();
		
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_P)) {
			pageNumber = 1;
			sbg.enterState(stateContainer.Game.PLAYSTATE);
		}
		
		if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			//check if buttons are pressed
			
			if (posX>20 && posX<40 && posY>20 && posY<40) {					
				pageNumber = 1;
				sbg.enterState(stateContainer.Game.PLAYSTATE);
			}		
			
			if (posX>1150 && posX<1170 && posY>550 && posY<580) {
				System.exit(0);
			}

			if (pageNumber == 1){
				if((posX > 1030 && posX < 1193) && (posY > 280 && posY < 315)) 
					pageNumber = 2;			
			}
			else if (pageNumber == 2){
				if (posX>30 && posX<241 && posY>280 && posY<315) 
					pageNumber = 1;		
			}
			
		}
		
	}
	
	@Override
	public int getID(){
		return stateContainer.Game.PAUSESTATE;
	}
}

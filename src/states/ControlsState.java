package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.DeferredFile;


public class ControlsState extends BasicGameState {
		
	private static Image 
		nextPageButton, previousPageButton, backButton, quitButton, 
		nextPageHover, previousPageHover, backHover, quitHover,
		controlsBackgroundPage1, controlsBackgroundPage2;
	
	private int pageNumber;
    
	public ControlsState(int state){
		
	}
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		pageNumber = 1;

		{
			LoadingList loading = LoadingList.get();
			loading.add(new DeferredFile("res/menu_graphics/new/controls1.png"){
				public void loadFile(String filename) throws SlickException{
					controlsBackgroundPage1 = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/controls2.png"){
				public void loadFile(String filename) throws SlickException{
					controlsBackgroundPage2 = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/back.png"){
				public void loadFile(String filename) throws SlickException{
					backButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/back_hover.png"){
				public void loadFile(String filename) throws SlickException{
					backHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/next_page.png"){
				public void loadFile(String filename) throws SlickException{
					nextPageButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/next_page_hover.png"){
				public void loadFile(String filename) throws SlickException{
					nextPageHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/previous_page.png"){
				public void loadFile(String filename) throws SlickException{
					previousPageButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/new/previous_page_hover.png"){
				public void loadFile(String filename) throws SlickException{ 
					previousPageHover = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/quit_button.png"){
				public void loadFile(String filename) throws SlickException{
					quitButton = new Image(filename);
				}
			});

			loading.add(new DeferredFile("res/menu_graphics/quit_hover.png"){
				public void loadFile(String filename) throws SlickException{
					quitHover = new Image(filename);
				}
			});
		}
			
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException{
		
		int	posX = Mouse.getX(),
			posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();
			//Fixing posY to reflect graphics coords
		
		if (pageNumber == 1) {
			
			controlsBackgroundPage1.draw(0,0);
			
			if (posX>1030 && posX<1193 && posY>280 && posY<315)
				nextPageHover.draw(1030,280);
			else nextPageButton.draw(1030,280);
			
			//menuButton.draw(1050, 20);
			
			if (posX>1150 && posX<1170 && posY>550 && posY<580)
				quitHover.draw(1148,556);
			else quitButton.draw(1148,556);
			
		}
		
		else if (pageNumber == 2){
			controlsBackgroundPage2.draw(0,0);
			
			if (posX>30 && posX<241 && posY>280 && posY<315)
				previousPageHover.draw(30,280);
			else previousPageButton.draw(30,280);
			
			//menuButton.draw(1050, 20);
			
			if (posX>1150 && posX<1170 && posY>550 && posY<580)
				quitHover.draw(1148,556);
			else quitButton.draw(1148,556);	
		}
		
		if (posX>20 && posX<40 && posY>20 && posY<40)
			backHover.draw(20,20);
		else backButton.draw(20,20);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {

		int	posX = Mouse.getX(),
			posY = stateContainer.Game.MAXIMUMHEIGHT -Mouse.getY();

		if (Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			
			if (posX>20 && posX<40 && posY>20 && posY<40) {
				pageNumber = 1;
				sbg.enterState(stateContainer.Game.MENUSTATE);
			}
			
			if (posX>1150 && posX<1170 && posY>550 && posY<580) {
				System.exit(0);
			}

			if (pageNumber == 1){
				if (posX>1030 && posX<1193 && posY>280 && posY<315) {
					pageNumber = 2;
				}
			}

			if (pageNumber == 2){
				if (posX>30 && posX<241 && posY>280 && posY<315) {
					pageNumber = 1;
				}
			}
		}
		
	}

	public int getID(){
		return stateContainer.Game.CONTROLSSTATE;
	}
	
}

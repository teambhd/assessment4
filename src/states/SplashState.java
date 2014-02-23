package states;

//import java.awt.Rectangle;
import java.io.IOException;

//import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public final class SplashState extends BasicGameState {

	private static Image 
		splash, indicator;
	/*private static Color
		loadBaseColor = Color.white, loadFillColor = Color.black;*/
	private static LoadingList loading = LoadingList.get();
	
	
	public SplashState(int state){
		
	}

	
	@Override
	public void init(GameContainer gc, StateBasedGame s)
			throws SlickException {
		splash = new Image("res/graphics/new/startup_bg.jpg");
		indicator = new Image("res/graphics/new/startup_plane.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g)
			throws SlickException {
		
		g.drawImage(splash, 0, 0);
		indicator.drawCentered(900 -((600 * loading.getRemainingResources()) 
				/ loading.getTotalResources()), 390);
		
		/*//calculate loading bar sizes
		int	ox = stateContainer.Game.MAXIMUMWIDTH /4,	// offset/sizing values
			oy = stateContainer.Game.MAXIMUMHEIGHT /16;
		
		Rectangle 
			loadBase = new Rectangle(ox, 10*oy, 2*ox, oy),
			loadFill = new Rectangle(loadBase);	
		loadFill.grow(-4, -4);
		loadFill.width = loadFill.width -
					((loadFill.width * loading.getRemainingResources()) 
							/ loading.getTotalResources());
		
		//draw loading bar
		g.setColor(loadBaseColor);
		g.fillRoundRect(loadBase.x, loadBase.y, 
				loadBase.width, loadBase.height, oy/3);
		g.setColor(loadFillColor);
		g.fillRoundRect(loadFill.x, loadFill.y, 
				loadFill.width, loadFill.height, oy/3 -1);*/
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta)
			throws SlickException {
		
		if (loading.getRemainingResources() == 0){	//finished loading
			gc.setShowFPS(false);
			s.enterState(stateContainer.Game.MENUSTATE);
		}
		else {
			DeferredResource next = loading.getNext();
			try {
				next.load();
			} catch (IOException e) {
				System.out.println("Failed loading:\t" +next.getDescription());
				e.printStackTrace();
			}	
		}		
	}

	@Override
	public int getID() {
		return stateContainer.Game.SPLASHSTATE;
	}	
	
}

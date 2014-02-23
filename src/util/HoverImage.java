package util;

import java.awt.geom.Rectangle2D;

import org.newdawn.slick.Image;

public class HoverImage {

	private Image
		normal, hover;
	private Rectangle2D.Float
		dim = new Rectangle2D.Float();	//tracks position and dimensions

	
	public HoverImage(Image normal, Image hover, float x, float y) {
		//ASSUMES normal AND hover TO BE THE SAME SIZE
		this.normal = normal;
		this.hover = hover;
		dim.x = x;
		dim.y = y;
		dim.width = normal.getWidth();
		dim.height = normal.getHeight();
	}
	
	
	public Boolean isMouseOver(int mouseX, int mouseY){
		return dim.contains(mouseX, mouseY);
	}
	
	public void render(int mouseX, int mouseY){
		if (isMouseOver(mouseX, mouseY))
			hover.draw(dim.x, dim.y);
		else normal.draw(dim.x, dim.y); 
	}
	
	
	public float getX() {
		return dim.x;
	}
	public void setX(float x) {
		this.dim.x = x;
	}

	public float getY() {
		return dim.y;
	}
	public void setY(float y) {
		this.dim.y = y;
	}

}

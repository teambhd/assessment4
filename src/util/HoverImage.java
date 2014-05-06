package util;

import java.awt.Rectangle;

import org.newdawn.slick.Image;

public class HoverImage {

    /**
	 * @uml.property  name="normal"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Image normal;
	/**
	 * @uml.property  name="hover"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Image hover;
    /**
	 * @uml.property  name="dim"
	 */
    private Rectangle dim = new Rectangle();  //tracks position and dimensions


    public HoverImage(Image normal, Image hover, int x, int y) {
        //ASSUMES normal AND hover TO BE THE SAME SIZE
        this.normal = normal;
        this.hover = hover;
        dim.x = x;
        dim.y = y;
        dim.width = normal.getWidth();
        dim.height = normal.getHeight();
    }


    public Boolean isMouseOver(int mouseX, int mouseY) {
        return dim.contains(mouseX, mouseY);
    }

    public void render(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            hover.draw(dim.x, dim.y);
        }

        else {
            normal.draw(dim.x, dim.y);
        }
    }


    public float getX() {
        return dim.x;
    }

    public void setX(int x) {
        this.dim.x = x;
    }

    public float getY() {
        return dim.y;
    }

    public void setY(int y) {
        this.dim.y = y;
    }

}

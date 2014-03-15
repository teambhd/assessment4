package unitTests;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;

import util.HoverImage;

public class HoverImage_Test {

    private HoverImage hoverImageInstance;
    private Image playHover;
    private Image playButton;
    private Rectangle dim = new Rectangle();

    @Before
    public void setUp() throws Exception {
        playHover = new Image("res/menu_graphics/new/play_button.png");
        playButton = new Image("res/menu_graphics/new/play_hover.png");
        hoverImageInstance = new HoverImage(playButton, playHover, 439, 349);
    }

    @After
    public void tearDown() throws Exception {
        playHover = null;
        playButton = null;
        hoverImageInstance = null;
    }

    @Test
    public void testGetX() {
        dim.x = 439;
        assertEquals(dim.x, (int)hoverImageInstance.getX());
    }

    @Test
    public void testSetX() {
        hoverImageInstance.setX(240);
        assertEquals(240, (int)hoverImageInstance.getX());
    }

    @Test
    public void testGetY() {
        dim.y = 349;
        assertEquals(dim.y, (int)hoverImageInstance.getY());
    }

    @Test
    public void testSetY() {
        hoverImageInstance.setY(240);
        assertEquals(240, (int)hoverImageInstance.getY());
    }

}

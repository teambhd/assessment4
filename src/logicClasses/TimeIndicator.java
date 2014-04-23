package logicClasses;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;

import util.DeferredFile;

public class TimeIndicator {
    
    private static Image clockImage;
    
    public static void init() throws SlickException {        
        if (clockImage == null) {
            LoadingList.get().add(new DeferredFile("res/graphics/clock.png") {
                public void loadFile(String filename) throws SlickException {
                    clockImage = new Image(filename);
                }
            });
        }
    }
    
    public static void render(Graphics g, float time) throws SlickException {
        // Work out the time to display
        int elapsedSecs = Math.round(time / 1000);
        int displayMins = elapsedSecs / 60;
        int displaySecs = elapsedSecs % 60;
        
        // Draw the clock icon
        clockImage.draw(0, 4);
        
        // Write out the time, with the values padded to 2 digits
        g.setColor(Color.white);
        g.drawString(String.format("%02d",displayMins) + ":" + String.format("%02d",displaySecs), 25, 10);        
    }
    
}
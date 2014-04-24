package util;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.util.ResourceLoader;

public class GameFont {
    
    private static TrueTypeFont gameFont;
    
    public static void init() {
        if (gameFont == null) {      
            LoadingList.get().add(new DeferredFile("res/fonts/fira-sans.ttf") {
                public void loadFile(String filename) {
                    InputStream inputStream = ResourceLoader.getResourceAsStream(filename);

                    try {
                        gameFont = new TrueTypeFont(Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(16f), true);
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    public static TrueTypeFont getFont() {
        return gameFont;
    }
    
}
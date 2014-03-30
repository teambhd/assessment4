package util;

import java.util.HashMap;

import org.newdawn.slick.Input;


public class KeyBindings {
    
    private HashMap<String, Integer> hash = new HashMap<String, Integer>();
    
    public int get(String key) {
        return hash.get(key);
    }
        
    // Set up our default keybindings for single player, and both players in the multi-player mode
    public static KeyBindings singlePlayerKeys = new KeyBindings();
    public static KeyBindings redPlayerKeys = new KeyBindings(); // red sits on the left
    public static KeyBindings bluePlayerKeys = new KeyBindings(); // blue sits on the right
    
    static {        
        singlePlayerKeys.hash.put("up", Input.KEY_W);
        singlePlayerKeys.hash.put("left", Input.KEY_A);
        singlePlayerKeys.hash.put("down", Input.KEY_S);
        singlePlayerKeys.hash.put("right", Input.KEY_D);
    
        singlePlayerKeys.hash.put("accelerate", Input.KEY_RBRACKET); // ]
        singlePlayerKeys.hash.put("decelerate", Input.KEY_LBRACKET); // [
    
        singlePlayerKeys.hash.put("toggle_forwards", Input.KEY_PERIOD);
        singlePlayerKeys.hash.put("toggle_backwards", Input.KEY_COMMA);
    
        singlePlayerKeys.hash.put("airport", Input.KEY_L); // combined take-off and landing button
        
        
        redPlayerKeys.hash.put("up", Input.KEY_W);
        redPlayerKeys.hash.put("left", Input.KEY_A);
        redPlayerKeys.hash.put("down", Input.KEY_S);
        redPlayerKeys.hash.put("right", Input.KEY_D);
        
        redPlayerKeys.hash.put("accelerate", Input.KEY_Q);
        redPlayerKeys.hash.put("decelerate", Input.KEY_E);
    
        redPlayerKeys.hash.put("toggle_forwards", Input.KEY_C);
        redPlayerKeys.hash.put("toggle_backwards", Input.KEY_V);
    
        redPlayerKeys.hash.put("airport", Input.KEY_B); // combined take-off and landing button
        
        
        bluePlayerKeys.hash.put("up", Input.KEY_UP);
        bluePlayerKeys.hash.put("left", Input.KEY_LEFT);
        bluePlayerKeys.hash.put("down", Input.KEY_DOWN);
        bluePlayerKeys.hash.put("right", Input.KEY_RIGHT);
        
        bluePlayerKeys.hash.put("accelerate", Input.KEY_RBRACKET); // ]
        bluePlayerKeys.hash.put("decelerate", Input.KEY_LBRACKET); // [
    
        bluePlayerKeys.hash.put("toggle_forwards", Input.KEY_PERIOD);
        bluePlayerKeys.hash.put("toggle_backwards", Input.KEY_COMMA);
    
        bluePlayerKeys.hash.put("airport", Input.KEY_L); // combined take-off and landing button
    }
        
}
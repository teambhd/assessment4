package util;

import java.util.HashMap;

import org.newdawn.slick.Input;


public class KeyBindings<K, V> extends HashMap<K, V> {
    
    // Set up our default keybindings for single player, and both players in the multi-player mode
    public static KeyBindings<String, Integer> singlePlayerKeys = new KeyBindings<String, Integer>();
    public static KeyBindings<String, Integer> redPlayerKeys = new KeyBindings<String, Integer>(); // red sits on the left
    public static KeyBindings<String, Integer> bluePlayerKeys = new KeyBindings<String, Integer>(); // blue sits on the right
    
    static {        
        singlePlayerKeys.put("up", Input.KEY_W);
        singlePlayerKeys.put("left", Input.KEY_A);
        singlePlayerKeys.put("down", Input.KEY_S);
        singlePlayerKeys.put("right", Input.KEY_D);
    
        singlePlayerKeys.put("accelerate", Input.KEY_RBRACKET); // ]
        singlePlayerKeys.put("decelerate", Input.KEY_LBRACKET); // [
    
        singlePlayerKeys.put("toggle_forwards", Input.KEY_PERIOD);
        singlePlayerKeys.put("toggle_backwards", Input.KEY_COMMA);
    
        singlePlayerKeys.put("airport", Input.KEY_L); // combined take-off and landing button
        
        
        redPlayerKeys.put("up", Input.KEY_W);
        redPlayerKeys.put("left", Input.KEY_A);
        redPlayerKeys.put("down", Input.KEY_S);
        redPlayerKeys.put("right", Input.KEY_D);
        
        redPlayerKeys.put("accelerate", Input.KEY_Q);
        redPlayerKeys.put("decelerate", Input.KEY_E);
    
        redPlayerKeys.put("toggle_forwards", Input.KEY_C);
        redPlayerKeys.put("toggle_backwards", Input.KEY_V);
    
        redPlayerKeys.put("airport", Input.KEY_B); // combined take-off and landing button
        
        
        bluePlayerKeys.put("up", Input.KEY_UP);
        bluePlayerKeys.put("left", Input.KEY_LEFT);
        bluePlayerKeys.put("down", Input.KEY_DOWN);
        bluePlayerKeys.put("right", Input.KEY_RIGHT);
        
        bluePlayerKeys.put("accelerate", Input.KEY_RBRACKET); // ]
        bluePlayerKeys.put("decelerate", Input.KEY_LBRACKET); // [
    
        bluePlayerKeys.put("toggle_forwards", Input.KEY_PERIOD);
        bluePlayerKeys.put("toggle_backwards", Input.KEY_COMMA);
    
        bluePlayerKeys.put("airport", Input.KEY_L); // combined take-off and landing button
    }
        
}
package util;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.util.ResourceLoader;

public class GameAudio {

    private static Music gameplayMusic;
    private static Sound crashSound;

    public static void init() {
        LoadingList loading = LoadingList.get();

        // Music
        if (gameplayMusic == null) {
            loading.add(new DeferredFile("res/audio/muzikele.ogg") {
                public void loadFile(String filename) throws SlickException {
                    gameplayMusic = new Music(filename);
                }
            });
        }

        // Sound Effects
        if (crashSound == null) {
            loading.add(new DeferredFile("res/audio/explosion.ogg") {
                public void loadFile(String filename) throws SlickException {
                    crashSound = new Sound(filename);
                }
            });
        }
    }

    public static Music getMusic() {
        return gameplayMusic;
    }

    public static Sound getCrashSound() {
        return crashSound;
    }

}
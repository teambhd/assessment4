package util;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.util.ResourceLoader;

import util.DeferredFile;

public class GameAudio {

    private static Music gameplayMusic;
    private static Sound endOfGameSound;

    public static void init() {
        LoadingList loading = LoadingList.get();

        // Music
        if (gameplayMusic == null) {
            loading.add(new DeferredFile("res/music/new/muzikele.ogg") {
                public void loadFile(String filename) throws SlickException {
                    gameplayMusic = new Music(filename);
                }
            });
        }

        // Sound Effects
        if (endOfGameSound == null) {
            loading.add(new DeferredFile("res/music/new/Big Explosion.ogg") {
                public void loadFile(String filename) throws SlickException {
                    endOfGameSound = new Sound(filename);
                }
            });
        }
    }

    public static Music getMusic() {
        return gameplayMusic;
    }

    public static Sound getEndOfGameSound() {
        return endOfGameSound;
    }

}
package sounds;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sounds.Sound;
import sounds.SoundMedia;

public class ResourceGetter {
    private final static String DEFAULT_SOUND_FILE_NAME = "sounds/DK64- Cutout fall.mp3";
    private static boolean usedSoundsBefore = false;
    
    private static Map<String,Media> soundCache = new HashMap<>();
    public static Sound getSound(String location){
        return getSound(new File(location));
    }
    public static Sound getSound(File file){
        Sound sound = null;
        if(!usedSoundsBefore){
            JFXPanel fxPanel = new JFXPanel();//crude hack to avoid error: this initializes the toolkit for JavaFxRuntime to let me use media players
            usedSoundsBefore = true;
        }
        
        try {
            Media media = soundCache.get(file.getPath());
            if(media==null){
                if(!file.exists()){
                    throw new IOException();
                }
                media = new Media(file.toURI().toString());
//System.out.println("loaded " + file.getPath());
                soundCache.put(file.getPath(), media);
            }else{
//System.out.println("loaded " + file.getPath() + " from cache");
            }
            sound = new SoundMedia(media);
        } catch (IOException e) {
            System.out.println("can't find sound at " + file.getPath());
            sound = getDefaultSound();
        }
        
        return sound;
        
    }
    
    
    private static Sound getDefaultSound() {
        Media media = soundCache.get(DEFAULT_SOUND_FILE_NAME);
        if(media!=null){
            return new SoundMedia(media);
        }
        
        try {
            File file = new File(DEFAULT_SOUND_FILE_NAME);
            if(!file.exists()){
                throw new IOException();
            }
            media = new Media(file.toURI().toString());
        } catch (IOException e) {
            System.out.println("can't find default sound! (should be at " + DEFAULT_SOUND_FILE_NAME + ")");
        }
        return new SoundMedia(media);
    }
}

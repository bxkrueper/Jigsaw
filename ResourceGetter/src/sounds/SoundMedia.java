package sounds;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundMedia implements Sound{
    /////todo: wav files
    
    
    private MediaPlayer mediaPlayer;//keep reference here to avoid garbage collection stopping it
    ///Create a player for a specific media. This is the only way to associate a Media object with a MediaPlayer: once the player is created it cannot be changed.
    public SoundMedia(Media media){
        mediaPlayer = new MediaPlayer(media);
    }
    
    @Override
    public void play(){
        mediaPlayer.play();
    }
    
    @Override
    public void stopAndReset(){
        mediaPlayer.stop();
    }
    
    @Override
    public void pause(){
        mediaPlayer.pause();
    }
    
    
    
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("destroyed " + this);
        mediaPlayer.dispose();
    }
}

package structure;

import java.awt.Graphics;
import java.io.File;

import javax.swing.JPanel;

import images.Picture;
import images.ResourceGetter;

public class PicturePanel extends JPanel{
    private Picture picture;
    
    ////input Picture instead?
    public PicturePanel(String picLocation){
        this(new File(picLocation));
    }
    public PicturePanel(File picFile){
        this(ResourceGetter.getPicture(picFile));
    }
    public PicturePanel(Picture pic){
        this.picture = pic;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        picture.draw(g, 0, 0, getWidth(), getHeight());
    }
}

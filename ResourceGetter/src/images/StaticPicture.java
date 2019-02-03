package images;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class StaticPicture implements Picture{

    private BufferedImage image;
    
    //this constructor is used by ResourceGetter
    public StaticPicture(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }
    //these constructors refer to ResourceGetter
    public StaticPicture(String fileName) {
        this.image = ResourceGetter.getBufferedImage(fileName);
    }
    public StaticPicture(File file) {
        this.image = ResourceGetter.getBufferedImage(file);
    }
    
    @Override
    public int getWidth() {
        return image.getWidth();
    }
    @Override
    public int getHeight() {
        return image.getHeight();
    }
    
    
    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.drawImage(image,x,y,width,height,null);
    }
    @Override
    public int getRGB(int row, int column) {
        // TODO Auto-generated method stub
        return image.getRGB(row, column);
    }
    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }
    
    //not an animation. does nothing
    @Override
    public void start() {
    }
    @Override
    public void stop() {
    }
    
    

}

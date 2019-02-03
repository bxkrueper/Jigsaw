package images;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public interface Picture {
    int getWidth();
    int getHeight();
    
    void draw(Graphics g, int x, int y, int width, int height);
    int getRGB(int row, int column);
    
    BufferedImage getBufferedImage();
    
    void start();
    void stop();
}

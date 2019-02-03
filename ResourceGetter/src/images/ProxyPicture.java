package images;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class ProxyPicture implements Picture{
    
    private Picture staticPicture;
    private String fileName;

    public ProxyPicture(String fileName){
        this.fileName = fileName;
    }
    
    private void loadPicture(){
        staticPicture = ResourceGetter.getPicture(fileName);
        fileName = null;
    }
    
    @Override
    public int getWidth() {
        if(staticPicture==null){
            loadPicture();
        }
        return staticPicture.getWidth();
    }

    @Override
    public int getHeight() {
        if(staticPicture==null){
            loadPicture();
        }
        return staticPicture.getHeight();
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        if(staticPicture==null){
            loadPicture();
        }
        staticPicture.draw(g, x, y, width, height);
    }

    @Override
    public int getRGB(int row, int column) {
        if(staticPicture==null){
            loadPicture();
        }
        return staticPicture.getRGB(row, column);
    }

    @Override
    public BufferedImage getBufferedImage() {
        if(staticPicture==null){
            loadPicture();
        }
        return staticPicture.getBufferedImage();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
    
}

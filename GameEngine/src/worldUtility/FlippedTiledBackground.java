package worldUtility;

import images.Picture;
import images.ResourceGetter;
import world.WorldPainter;

public class FlippedTiledBackground extends TiledBackground{
    public FlippedTiledBackground(Picture pic,double width, double height, int maxPictures, int step){
        super(pic,width,height, maxPictures, step);
    }
    public FlippedTiledBackground(Picture image, int maxPictures, int step){
        super(image,image.getWidth(),image.getHeight(), maxPictures, step);
    }
    public FlippedTiledBackground(String fileLocation,double width, double height, int maxPictures, int step){
        super(ResourceGetter.getPicture(fileLocation),width,height, maxPictures, step);
    }
    public FlippedTiledBackground(String fileLocation, int maxPictures, int step){
        super(ResourceGetter.getPicture(fileLocation), maxPictures, step);
    }
    
    @Override
    protected void drawImage(WorldPainter wp, double x, double y) {
        boolean flipH = Math.round(Math.abs(x)/WIDTH)%2==1;
        boolean flipV = Math.round(Math.abs(y)/HEIGHT)%2==1;
        
        if(!flipH&&!flipV){
            wp.drawPicture(getImage(), x, y, WIDTH, HEIGHT);
        }else if(flipH&&!flipV){
            wp.drawPicture(getImage(), x+WIDTH, y, -WIDTH, HEIGHT);
        }else if(!flipH&&flipV){
            wp.drawPicture(getImage(), x, y+HEIGHT, WIDTH, -HEIGHT);
        }else{// if(flipH&&flipV)
            wp.drawPicture(getImage(), x+WIDTH, y+HEIGHT, -WIDTH, -HEIGHT);
        }
        
    }
}

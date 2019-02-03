package worldUtility;

import java.awt.Color;
import java.awt.Font;

import images.Picture;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

public class DisplayPicture extends WorldObject implements WorldDrawable{
    private Picture image;
    private double x,y,width,height;
    
    public DisplayPicture(Picture image, double x, double y, double width, double height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public DisplayPicture(Picture image, double x, double y){
        this(image,x,y,image.getWidth(),image.getHeight());
    }
    
    public void setImage(Picture newImage){
        this.image = newImage;
    }
    
    
    @Override
    public void draw(WorldPainter wp) {
        wp.drawPicture(image, x, y, width, height);
    }

    @Override
    public void doOnAdd() {
    }

    @Override
    public void doOnDelete() {
    }

}

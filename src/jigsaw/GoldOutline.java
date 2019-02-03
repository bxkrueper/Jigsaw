package jigsaw;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import coordinate.Coordinate2DDouble;
import images.Picture;
import images.ResourceGetter;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;


public class GoldOutline extends WorldObject implements WorldDrawable{
    private Picture image;
    private Coordinate2DDouble picPosition;//bottomLeft of empty area
    private Coordinate2DDouble position;//bl of frame
    private double width, height;//of empty area
    private Path2D clipArea;
    double boarderWidth;
    double boarderHeight;
    
    public GoldOutline(Coordinate2DDouble p, double w, double h){
        image = ResourceGetter.getPicture("images/Gold Texture.jpg");
        this.picPosition = new Coordinate2DDouble(p);
        this.position = new Coordinate2DDouble(p);
        this.width = w;
        this.height = h;
        
        this.boarderWidth = width/15;
        this.boarderHeight = height/15;
        position.set(position.getX()-boarderWidth,position.getY()-boarderHeight);
        makeClipArea(boarderWidth,boarderHeight);
    }
    
    public Coordinate2DDouble getPicPosition() {
        return picPosition;
    }
    
    private void makeClipArea(double boarderWidth, double boarderHeight) {
        clipArea = new Path2D.Double(new Rectangle2D.Double(position.getX(),position.getY(),width+boarderWidth*2,boarderHeight));
        clipArea.append(new Rectangle2D.Double(position.getX(),position.getY(),boarderWidth,height+boarderHeight*2), false);
        clipArea.append(new Rectangle2D.Double(position.getX(),position.getY()+height+boarderHeight,width+boarderWidth*2,boarderHeight), false);
        clipArea.append(new Rectangle2D.Double(position.getX()+width+boarderWidth,position.getY(),boarderWidth,height+boarderHeight*2), false);
    }
    @Override
    public void draw(WorldPainter wp) {
        wp.clip(clipArea);
        wp.drawPicture(image,position,width+2*boarderWidth,height+2*boarderHeight);
//        wp.restoreWorldClip();
    }

    @Override
    public void doOnAdd() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void doOnDelete() {
        // TODO Auto-generated method stub
        
    }
    
}

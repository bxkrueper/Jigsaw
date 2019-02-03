package worldUtility;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;

import coordinate.Coordinate;
import coordinate.Coordinate2D;
import coordinate.Coordinate2DDouble;
import images.Picture;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseClickedOnReact;
import worldFunctionality.MouseGrabbable;
import worldFunctionality.MouseMovedReact;

public class WorldButton extends WorldObject implements MouseClickedOnReact, WorldDrawable, MouseMovedReact, MouseGrabbable{

    private Shape area;
    private Coordinate2DDouble pictureSource;
    private double pictureWidth,pictureHeight;
    
    private MyListener listener;
    private String text;
    private Picture pic;
    private boolean beingHoveredOn;
    private boolean beingPushedDown;
    private static final Color highlightColor = new Color(127,127,127,50);
    
    public WorldButton(Shape area, Coordinate2D pictureSource,double pictureWidth,double pictureHeight, Picture pic,MyListener l,String text){
        this.area = area;
        this.pictureSource = (Coordinate2DDouble) pictureSource.copy();
        this.pictureWidth = pictureWidth;
        this.pictureHeight = pictureHeight;
        this.listener = l;
        this.text = text;
        this.pic = pic;
        this.beingHoveredOn = false;
        this.beingPushedDown = false;
    }
    @Override
    public boolean acceptTarget(ButtonType bt) {
        return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
    }

    @Override
    public boolean consumeTargetable(ButtonType bt) {
        return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
    }

    //position of picture source
    @Override
    public Coordinate getPosition() {
        // TODO Auto-generated method stub
        return pictureSource;
    }

    @Override
    public boolean occupies(double x, double y) {
        return area.contains(new Point2D.Double(x,y));
    }

    @Override
    public void reactToMouseClickedOn(ButtonType bt, double x, double y) {
        beingPushedDown = false;
        this.repaintWorld();
        activate();
    }
    public void activate() {
        listener.eventHappened(new MyEvent(this,text,null));
    }
    @Override
    public void reactToMouseDownOn(ButtonType bt, double x, double y) {
        beingPushedDown = true;
        this.repaintWorld();
    }

    @Override
    public void doOnAdd() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doOnDelete() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void draw(WorldPainter wp) {
        
        if(beingPushedDown){
            wp.translate(3, -3);
            wp.clip(area);
            wp.translate(-3, 3);
            wp.drawPicture(pic, pictureSource.getX()+3,pictureSource.getY()-3,pictureWidth, pictureHeight);
        }else{
            wp.clip(area);
            wp.drawPicture(pic, pictureSource.getX(),pictureSource.getY(),pictureWidth, pictureHeight);
        }
        
        if(beingHoveredOn){
            wp.setColor(highlightColor);
            wp.fillRectangle(pictureSource.getX(),pictureSource.getY()-3,pictureWidth+3, pictureHeight+3);
        }
    }
    @Override
    public void reactToMouseMoved(double x, double y) {
        boolean newBool = occupies(x,y);
        
        if(beingHoveredOn!=newBool){
            beingHoveredOn = newBool;
            beingPushedDown = false;
            this.repaintWorld();
        }
    }
    @Override
    public void reactToMouseEntered(double x, double y) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void reactToMouseExited(double x, double y) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public boolean acceptBeingGrabbed(ButtonType bt) {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public void unGrabbed() {
        if(beingHoveredOn){
            activate();
        }
        beingPushedDown = false;
        this.repaintWorld();
    }
    @Override
    public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
        // TODO Auto-generated method stub
        
    }
    

}






//
//
//protected Rectangle2D area;////////change to shape, put shape in constructor?
//private MyListener listener;
//private String text;
//private BufferedImage pic;
//private boolean beingHoveredOn;
//private boolean beingPushedDown;
//private Color highlightColor;
//
//public WorldButton(double blx,double bly,double width,double height,BufferedImage pic,MyListener l,String text){
//    this.area = new Rectangle2D.Double(blx,bly,width,height);
//    this.listener = l;
//    this.text = text;
//    this.pic = pic;
//    this.beingHoveredOn = false;
//    this.beingPushedDown = false;
//    this.highlightColor = new Color(127,127,127,50);
//}
//@Override
//public boolean acceptTarget(ButtonType bt) {
//    return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
//}
//
//@Override
//public boolean consumeTargetable(ButtonType bt) {
//    return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
//}
//
//@Override
//public Coordinate getPosition() {
//    // TODO Auto-generated method stub
//    return new Coordinate2DDouble(area.getX(),area.getY());
//}
//
//@Override
//public boolean occupies(double x, double y) {
//    return area.contains(new Point2D.Double(x,y));
//}
//
//@Override
//public void reactToMouseClickedOn(ButtonType bt, double x, double y) {
//    beingPushedDown = false;
//    this.repaintWorld();
//    activate();
//}
//public void activate() {
//    listener.eventHappened(new MyEvent(this,text));
//}
//@Override
//public void reactToMouseDownOn(ButtonType bt, double x, double y) {
//    beingPushedDown = true;
//    this.repaintWorld();
//}
//
//@Override
//public void doOnAdd() {
//    // TODO Auto-generated method stub
//    
//}
//
//@Override
//public void doOnDelete() {
//    // TODO Auto-generated method stub
//    
//}
//@Override
//public void draw(WorldPainter wp) {
//    if(beingPushedDown){
//        wp.drawPicture(pic, area.getX()+3,area.getY()-3,area.getWidth(), area.getHeight());
//    }else{
//        wp.drawPicture(pic, area.getX(),area.getY(),area.getWidth(), area.getHeight());
//    }
//    
//    if(beingHoveredOn){
//        wp.setColor(highlightColor);
//        wp.fillRectangle(area.getX(),area.getY(),area.getWidth(), area.getHeight());
//    }
//}
//@Override
//public void reactToMouseMoved(double x, double y) {
//    boolean newBool = occupies(x,y);
//    
//    if(beingHoveredOn!=newBool){
//        beingHoveredOn = newBool;
//        beingPushedDown = false;
//        this.repaintWorld();
//    }
//}
//@Override
//public void reactToMouseEntered(double x, double y) {
//    // TODO Auto-generated method stub
//    
//}
//@Override
//public void reactToMouseExited(double x, double y) {
//    // TODO Auto-generated method stub
//    
//}
//@Override
//public boolean acceptBeingGrabbed(ButtonType bt) {
//    // TODO Auto-generated method stub
//    return true;
//}
//@Override
//public void unGrabbed() {
//    if(beingHoveredOn){
//        activate();
//    }
//    beingPushedDown = false;
//    this.repaintWorld();
//}
//@Override
//public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
//    // TODO Auto-generated method stub
//    
//}

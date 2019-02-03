package test;

import java.awt.Color;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseGrabbable;

public class Square implements WorldDrawable, MouseGrabbable{
    private double centerX;
    private double centerY;
    private double width;
    private double height;
    
    public Square(double x, double y, double w, double h){
        this.centerX = x;
        this.centerY = y;
        this.width = w;
        this.height = h;
    }

    @Override
    public void draw(WorldPainter wp) {
        wp.setColor(Color.RED);
        wp.fillRectangle((centerX-width/2),(centerY-height/2),width,height);
    }

    @Override
    public boolean occupies(double x, double y) {
        return (x<centerX+width/2 && x>centerX-width/2) && (y<centerY+height/2 && y>centerY-height/2);
    }

    @Override
    public boolean acceptTarget(ButtonType bt) {
        return bt==ButtonType.MIDDLE;
    }

    @Override
    public boolean consumeTargetable(ButtonType bt) {
        return true;
    }

    @Override
    public boolean acceptBeingGrabbed(ButtonType bt) {
        return bt==ButtonType.MIDDLE;
    }

    @Override
    public void unGrabbed() {
    }

    @Override
    public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
        centerX = objectOrigionalLocation.getX()+(grabLocation.getX()-grabSorce.getX());
        centerY = objectOrigionalLocation.getY()+(grabLocation.getY()-grabSorce.getY());
    }

    @Override
    public Coordinate getPosition() {
        return new Coordinate2DDouble(centerX,centerY);
    }

}

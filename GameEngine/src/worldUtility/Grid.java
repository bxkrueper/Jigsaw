package worldUtility;

import java.awt.Color;

import myAlgs.MyMath;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

public class Grid extends WorldObject implements WorldDrawable{

    private static final int MIN_DISTANCE_BETWEEN_LINES = 80;//pixels
    
    @Override
    public void draw(WorldPainter wp) {
        double left = wp.getLeftCoordinate();
        double right = wp.getRightCoordinate();
        double top = wp.getTopCoordinate();
        double bottom = wp.getBottomCoordinate();
        
        double zoom = wp.getZoom();
        
        double spacing = Math.pow(10, (int) (Math.log10(zoom*MIN_DISTANCE_BETWEEN_LINES)+1));
        spacing/=10;
        
        double xStart = Math.ceil(left/spacing)*spacing;
        wp.setColor(Color.GRAY);
        wp.setStroke(0.5, WorldPainter.ReletiveThickness.PIXLE);
        for(double i=xStart;i<right;i+=spacing){
            wp.drawLine(i, top, i, bottom);
        }
        
        double yStart = Math.ceil(bottom/spacing)*spacing;
        for(double i=yStart;i<top;i+=spacing){
            wp.drawLine(left, i, right, i);
        }
        
        //thicker lines
        spacing*=10;
        
        xStart = Math.ceil(left/spacing)*spacing;
        wp.setColor(Color.BLACK);
        wp.setStroke(2, WorldPainter.ReletiveThickness.PIXLE);
        for(double i=xStart;i<right;i+=spacing){
            if(Math.abs(i)<0.0001){//make axis thicker  doesn't work at zoom 1.0E24
                wp.setStroke(4, WorldPainter.ReletiveThickness.PIXLE);
            }else{
                wp.setStroke(2, WorldPainter.ReletiveThickness.PIXLE);
            }
            wp.drawLine(i, top, i, bottom);
            wp.drawText(getTickMarkString(i), i+5*zoom, bottom+5*zoom,12,WorldPainter.ReletiveThickness.PIXLE);
        }
        
        yStart = Math.ceil(bottom/spacing)*spacing;
        for(double i=yStart;i<top;i+=spacing){
            if(Math.abs(i)<0.0001){//make axis thicker
                wp.setStroke(4, WorldPainter.ReletiveThickness.PIXLE);
            }else{
                wp.setStroke(2, WorldPainter.ReletiveThickness.PIXLE);
            }
            wp.drawLine(left, i, right, i);
            wp.drawText(getTickMarkString(i), left+5*zoom, i+5*zoom,12,WorldPainter.ReletiveThickness.PIXLE);
        }
    }
    
    
    private String getTickMarkString(double value){
        if(value==0){
            return "0";
        }
        if (Math.abs(value)<0.001 || Math.abs(value)>=1000000){
            return String.format("%e",value);
        }else{
            return MyMath.toNiceString(value,5);
        }
    }
    
    @Override
    public void drawDebug(WorldPainter wp) {
        
    }

    @Override
    public void doOnAdd() {
    }

    @Override
    public void doOnDelete() {
    }
    
}

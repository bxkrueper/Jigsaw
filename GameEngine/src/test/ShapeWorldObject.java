package test;

import java.awt.Shape;

import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

public class ShapeWorldObject extends WorldObject implements WorldDrawable{

    private Shape shape;
    
    public ShapeWorldObject(Shape shape){
        this.shape = shape;
    }
    
    @Override
    public void draw(WorldPainter wp) {
        wp.drawShape(shape);
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

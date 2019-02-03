package hitbox;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import coordinate.Coordinate;
import world.WorldPainter;
import worldFunctionality.SpatialEntity;

public class ShapeHitBox implements HitBox{
    
    private Shape path;//in relative coordinates
    private static final Color color = new Color(0,255,0,100);
    
    public ShapeHitBox(Shape path){
        this.path = path;
    }
    
    @Override
    public boolean occupies(double x, double y,SpatialEntity owner) {
        Coordinate ownerPosition = owner.getPosition();
        double relX = x-ownerPosition.getX();
        double relY = y-ownerPosition.getY();
        return path.contains(relX,relY);
    }

    @Override
    public void draw(WorldPainter wp,SpatialEntity owner) {
        wp.setColor(color);
        Coordinate ownerPosition = owner.getPosition();
        wp.translate(ownerPosition);
        wp.fillShape(path);
        wp.resetWorldGraphics();//put it back, as this object is not in the drawable list and the worldgraphics will not reset automatically
    }

    @Override
    public boolean occupiesRelative(double x, double y) {
        return path.contains(x,y);
    }

}

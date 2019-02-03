package hitbox;

import java.awt.Color;

import coordinate.Coordinate;
import world.WorldPainter;
import worldFunctionality.SpatialEntity;

public class NullHitBox implements HitBox{
    
    public NullHitBox(){
    }
    
    @Override
    public boolean occupies(double x, double y,SpatialEntity owner) {
        return false;
    }

    @Override
    public void draw(WorldPainter wp,SpatialEntity owner) {
        wp.setColor(Color.BLACK);
        Coordinate ownerPosition = owner.getPosition();
        wp.drawText("null hit box", ownerPosition.getX(), ownerPosition.getY());
    }

    @Override
    public boolean occupiesRelative(double x, double y) {
        return false;
    }
}

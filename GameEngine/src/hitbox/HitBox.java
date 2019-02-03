package hitbox;

import world.WorldPainter;
import worldFunctionality.SpatialEntity;
//immutable
//uses relative coordinates to owner
//does not trigger auto-reset for world painter if it changes the graphics object

////////use coordinate instead of owner???
public interface HitBox{
    boolean occupies(double x, double y,SpatialEntity owner);//x and y are in world coordinates
    boolean occupiesRelative(double x, double y);//x and y are relative to the owner's position like the hitbox is
    public void draw(WorldPainter wp,SpatialEntity owner);
    //todo: scale, rotate. keep cache in mind
}

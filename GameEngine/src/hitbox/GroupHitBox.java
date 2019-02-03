package hitbox;

import java.util.ArrayList;
import java.util.List;

import coordinate.Coordinate;
import world.WorldPainter;
import worldFunctionality.SpatialEntity;

public class GroupHitBox implements HitBox{
    
    private List<HitBox> hitboxList;
    
    public GroupHitBox(List<HitBox> hitboxList){
        this.hitboxList = hitboxList;
    }
    
    public GroupHitBox(HitBox... hitboxes){
        this.hitboxList = new ArrayList<>(hitboxes.length);
        for(HitBox hitbox:hitboxes){
            hitboxList.add(hitbox);
        }
    }
    
    @Override
    public boolean occupies(double x, double y,SpatialEntity owner) {
        for(HitBox hitbox:hitboxList){
            if(hitbox.occupies(x, y,owner)){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void draw(WorldPainter wp,SpatialEntity owner) {
        for(HitBox hitbox:hitboxList){
            hitbox.draw(wp,owner);
        }
    }

    @Override
    public boolean occupiesRelative(double x, double y) {
        for(HitBox hitbox:hitboxList){
            if(hitbox.occupiesRelative(x, y)){
                return true;
            }
        }
        
        return false;
    }
}

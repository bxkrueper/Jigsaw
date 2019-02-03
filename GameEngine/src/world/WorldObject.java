package world;

import structure.InputState.InputStateReader;

public abstract class WorldObject {
    private World world;/////////////may be null on creation  world will set this when the object is added
//    private InputStateReader inputStateReader;/////////////may be null on creation   world will set this when the object is added
    
    /////what if something else besides world sets this?
    public final void setWorld(World world){
        this.world = world;
//        this.inputStateReader = world.getInputStateReader();
    }
    
    public final InputStateReader getInputStateReader(){
        return world.getInputStateReader();
    }
    
    public final void addToWorld(WorldObject worldObject){
        world.add(worldObject);
    }
    
    public final void deleteFromWorld(){
        world.remove(this);
        this.world = null;
    }
    
    //after world reference is set
    public abstract void doOnAdd();
    public abstract void doOnDelete();
    
    public final void sendToFront(){
        world.sendToFront(this);
    }
    
    public final void sendToBack(){
        world.sendToBack(this);
    }
    
    public final void repaintWorld(){
        world.repaint();
    }
    
    /////to add: repaintWorld(clip square)
}

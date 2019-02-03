package test;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Animation;
import images.ResourceGetter;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

public class Explosion extends WorldObject implements WorldDrawable, MyListener{

    private Coordinate position;
    private double size;
    private Animation animation;
    
    public Explosion(Coordinate position,double size){
        this.position = position;
        this.size = size;
        this.animation = (Animation) ResourceGetter.getPicture("images/Explosion/Explosion Animation");
        animation.setListener(this);
        animation.setRepeats(false);
        animation.startFromBeginning();
    }
    @Override
    public void draw(WorldPainter wp) {
        wp.drawPicture(animation,new Coordinate2DDouble(position.getX()-size/2,position.getY()-size/2),size,size);
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
    public void eventHappened(MyEvent e) {
        this.repaintWorld();
        if(e.getCommand().equals("last frame done")){
            this.deleteFromWorld();
        }
    }

}

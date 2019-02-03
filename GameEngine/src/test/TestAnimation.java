package test;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Animation;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import images.ResourceGetter;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;
import worldFunctionality.MouseGrabbable;
import worldFunctionality.MouseButtonReact.ButtonType;

public class TestAnimation extends WorldObject implements WorldDrawable, MouseGrabbable, MyListener{
    private Animation animation;
    private Coordinate2DDouble position;//center
    private double size;
    
    public TestAnimation(Coordinate2DDouble position, double size){
        this.animation = (Animation) ResourceGetter.getPicture("images/numbers");
        animation.setListener(this);
        animation.setRepeats(false);
        animation.startFromBeginning();
        this.position = position;
        this.size = size;
        
    }

    @Override
    public void draw(WorldPainter wp) {
        wp.drawPicture(animation,new Coordinate2DDouble(position.getX()-size/2,position.getY()-size/2),size,size);////////////off
    }
    @Override
    public boolean occupies(double x, double y) {
        return (x<position.getX()+size/2 && x>position.getX()-size/2) && (y<position.getY()+size/2 && y>position.getY()-size/2);
    }

    @Override
    public boolean acceptTarget(ButtonType bt) {
        return bt==ButtonType.LEFT;
    }

    @Override
    public boolean consumeTargetable(ButtonType bt) {
        return true;
    }

    @Override
    public boolean acceptBeingGrabbed(ButtonType bt) {
        return true;
    }

    @Override
    public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce,Coordinate objectOrigionalLocation) {
        position.set(objectOrigionalLocation.getX()+(grabLocation.getX()-grabSorce.getX()),objectOrigionalLocation.getY()+(grabLocation.getY()-grabSorce.getY()));
    }

    @Override
    public void unGrabbed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Coordinate getPosition() {
        return position;
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
            this.addToWorld(new Explosion(new Coordinate2DDouble(0,0),60));
            this.deleteFromWorld();
        }
    }
}

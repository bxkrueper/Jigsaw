package jigsaw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

import javax.swing.Timer;

import coordinate.Coordinate2DDouble;
import images.Picture;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import path.PathSection;
import path.FiniteWalkablePath;
import path.LinePath;
import path.WalkablePath;

public class FinishedPuzzle extends WorldObject implements WorldDrawable, ActionListener{

    private Picture pic;
    private Coordinate2DDouble position;
    private double startRotation;
    private double rotation;
    private double width,height;
    private MyListener animationFinishedListener;
    private WalkablePath walkablePath;
    private double distancePerTick;
    private int tickNumber,maxTick;
    
    private Timer timer;
    
    public FinishedPuzzle(Picture pic,Coordinate2DDouble startPosition,double startRotation, Coordinate2DDouble finishedPosition, double width, double height,MyListener whoToAlertWhenAnimationIsDone){
        this.pic = pic;
        
        this.position = (Coordinate2DDouble) startPosition.copy();
        this.startRotation = startRotation;
        this.rotation = startRotation;
        this.width = width;
        this.height = height;
        this.animationFinishedListener = whoToAlertWhenAnimationIsDone;
        this.tickNumber = 0;
        this.maxTick = 30;
        
        PathSection ps = new LinePath(startPosition,finishedPosition);
        this.walkablePath = new FiniteWalkablePath(ps);
        
        timer = new Timer(20,this);
        this.distancePerTick = walkablePath.getLength()/maxTick;
        pic.start();
    }
    
    public FinishedPuzzle(Picture pic,Coordinate2DDouble startPosition,double startRotation, Coordinate2DDouble finishedPosition, double width, double height){
        this(pic,startPosition,startRotation,finishedPosition,width,height,null);
    }
    
    public Picture getPic(){
        return pic;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        tickNumber++;
        if(walkablePath.isAtEnd()){
            timer.stop();
        }
        walkablePath.advance(distancePerTick);
        position.set(walkablePath.currentCoordinate());
        rotation = startRotation+(0-startRotation)*((double)tickNumber/maxTick);
        this.repaintWorld();
        if(walkablePath.isAtEnd()){
            rotation = 0;//make sure picture isn't crooked
            animationFinishedListener.eventHappened(new MyEvent(this,"Animation done",null));
        }
    }

    @Override
    public void draw(WorldPainter wp) {
        wp.translate(position.getX(), position.getY());
        wp.rotate(rotation);
        wp.drawPicture(pic, Coordinate2DDouble.zero, width, height);
    }

    @Override
    public void doOnAdd() {
        timer.start();
    }

    @Override
    public void doOnDelete() {
        timer.stop();
    }


    public void setListener(MyListener listener) {
        animationFinishedListener = listener;
    }

}



//public class FinishedPuzzle extends WorldObject implements WorldDrawable, ActionListener{
//
//    private Picture pic;
//    private Coordinate2DDouble position;
//    private Coordinate2DDouble startPosition;
//    private double startRotation;
//    private double rotation;
//    private Coordinate2DDouble finishedPosition;
//    private double width,height;
//    private int tickNumber,maxTick;
//    private MyListener animationFinishedListener;
//    
//    private Timer timer;
//    
//    public FinishedPuzzle(Picture pic,Coordinate2DDouble startPosition,double startRotation, Coordinate2DDouble finishedPosition, double width, double height,MyListener whoToAlertWhenAnimationIsDone){
//        this.pic = pic;
//        this.position = (Coordinate2DDouble) startPosition.copy();
//        this.startPosition = (Coordinate2DDouble) startPosition.copy();
//        this.startRotation = startRotation;
//        this.rotation = startRotation;
//        this.finishedPosition = (Coordinate2DDouble) finishedPosition.copy();
//        this.width = width;
//        this.height = height;
//        this.tickNumber = 0;
//        this.maxTick = 30;
//        this.animationFinishedListener = whoToAlertWhenAnimationIsDone;
//        
//        timer = new Timer(20,this);
//    }
//    
//    public FinishedPuzzle(Picture pic,Coordinate2DDouble startPosition,double startRotation, Coordinate2DDouble finishedPosition, double width, double height){
//        this(pic,startPosition,startRotation,finishedPosition,width,height,null);
//    }
//    
//    public Picture getPic(){
//        return pic;
//    }
//    
//    
//    @Override
//    public void actionPerformed(ActionEvent arg0) {
//        tickNumber++;
//        if(tickNumber==maxTick){
//            timer.stop();
//        }
//        position.set(startPosition.getX()+(finishedPosition.getX()-startPosition.getX())*((double)tickNumber/maxTick),startPosition.getY()+(finishedPosition.getY()-startPosition.getY())*((double)tickNumber/maxTick));
//        rotation = startRotation+(0-startRotation)*((double)tickNumber/maxTick);
//        this.repaintWorld();
//        if(tickNumber==maxTick){
//            animationFinishedListener.eventHappened(new MyEvent(this,"Animation done",null));
//        }
//    }
//
//    @Override
//    public void draw(WorldPainter wp) {
//        wp.translate(position.getX(), position.getY());
//        wp.rotate(rotation);
//        wp.drawPicture(pic, Coordinate2DDouble.zero, width, height);
//    }
//
//    @Override
//    public void doOnAdd() {
//        timer.start();
//    }
//
//    @Override
//    public void doOnDelete() {
//        timer.stop();
//    }
//
//
//    public void setListener(MyListener listener) {
//        animationFinishedListener = listener;
//    }
//
//}
package images;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import myAlgs.MyEvent;
import myAlgs.MyListener;

//directory must have a file called info.txt and at least one image starting with 0.(something in POSSIBLE_FORMATS)

//todo: option to have non repeating animation stay on the last frame.  make it default?
public class Animation implements Picture, ActionListener{
    
    private Picture[] frames;
    
    private Timer frameTimer;
    private int frameNumber;
    
    //set these after creation
    private MyListener myListener;
    private boolean repeats;
    
    public Animation(Picture[] frames, int period){
        this.frames = frames;
        
        this.frameTimer = new Timer(period,this);
        frameTimer.setActionCommand("period tick");
        setFrameNumber(0);
        this.repeats = true;
    }
    public Animation(Picture[] frames, int period,boolean repeats){
        this(frames,period);
        this.repeats = repeats;
    }
    
    //returns a new animation that starts from the start
    //but shares the frame array and gets a copy of the period
    //!!!need to set listener
    public Animation freshCopy() {
        return new Animation(frames,getPeriod(),repeats);
    }
    
    //used by subclasses
    protected Picture[] getFrames(){
        return frames;
    }
    
    public int getPeriod(){
        return frameTimer.getDelay();
    }
    
    public int getNumberOfFrames(){
        return frames.length;
    }
    
    public int getFrameNumber(){
        return frameNumber;
    }
    
    //if a frame is an animation, it automatically stops the old animation and starts the new one
    public void setFrameNumber(int num){
        frames[frameNumber].stop();
        frameNumber = num;
        frames[frameNumber].start();
    }
    
    //returns true if it advances with no issue false if can't advance
    public boolean advanceFrame(){
        
        int newframeNumber = frameNumber + 1;
        
        if(repeats){
           if(newframeNumber>=frames.length){
               setFrameNumber(0);
           }else{
               setFrameNumber(newframeNumber);
           }
           return true;
        }else{//it doens't repeat
            if(newframeNumber>=frames.length){
                frameTimer.stop();
                return false;
            }else{
                setFrameNumber(newframeNumber);
                return true;
            }
        }
    }
    
    //returns true if it advances with no issue false if can't advance
    public boolean backTrackFrame(){
        
        int newframeNumber = frameNumber - 1;
        
        if(repeats){
           if(newframeNumber<0){
               setFrameNumber(frames.length-1);
           }else{
               setFrameNumber(newframeNumber);
           }
           return true;
        }else{//it doens't repeat
            if(newframeNumber<0){
                return false;
            }else{
                setFrameNumber(newframeNumber);
                return true;
            }
        }
    }
    
    public void startFromBeginning(){
        setFrameNumber(0);
        if(getPeriod()>0){
            frameTimer.restart();
        }
    }
    
    ////////bug: nested animations seem to only advance one frame per parent frame if the parent frame's period is twice as long
    ////does timer pause midway?
    @Override
    public void start(){
        if(getPeriod()>0){
            frameTimer.start();
        }
    }
    
    @Override
    public void stop(){
        frameTimer.stop();
    }
    
    //if you want it to alert the object when the picture changes
    public void setListener(MyListener myListener){
        this.myListener = myListener;
    }
    
    public void setRepeats(boolean r){
        this.repeats = r;
    }
    public boolean getRepeats(){
        return repeats;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        boolean stopped = !advanceFrame();
        if(myListener!=null){
           if(stopped && myListener!=null){
               myListener.eventHappened(new MyEvent(this,"last frame done",null));
           }else{
               myListener.eventHappened(new MyEvent(this,"frame changed",null));
           } 
        }
        
    }
    
    
    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return frames[frameNumber].getWidth();
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return frames[frameNumber].getHeight();
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
            frames[frameNumber].draw(g,x,y,width,height);
    }
    @Override
    public int getRGB(int row, int column) {
        // TODO Auto-generated method stub
        return frames[frameNumber].getRGB(row, column);
    }
    @Override
    public BufferedImage getBufferedImage() {
        return frames[frameNumber].getBufferedImage();
    }

    
}

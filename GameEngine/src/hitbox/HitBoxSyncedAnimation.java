package hitbox;

import hitbox.HitBox;
import images.Animation;
import images.Picture;


public class HitBoxSyncedAnimation extends Animation{

    private HitBox[] hitboxes;
    
    public HitBoxSyncedAnimation(Picture[] frames, int period,HitBox[] hitboxes) {
        super(frames, period);
        this.hitboxes = hitboxes;
    }
    public HitBoxSyncedAnimation(Picture[] frames, int period,HitBox[] hitboxes,boolean repeats){
        super(frames,period,repeats);
        this.hitboxes = hitboxes;
    }
    
    //returns a new animation that starts from the start
    //but shares the frame array and hitbox array and gets a copy of the period
    //!!!need to set listener
    @Override
    public Animation freshCopy() {
        return new HitBoxSyncedAnimation(getFrames(),getPeriod(),hitboxes,getRepeats());
    }
    
    public HitBox getHitBox(){
        return hitboxes[getFrameNumber()];
    }

}
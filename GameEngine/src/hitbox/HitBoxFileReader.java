package hitbox;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import objectReader.ObjectFileReader;
import worldFunctionality.SpatialEntity;

//does not set the owner. owner is set later
public class HitBoxFileReader extends ObjectFileReader{
    
    private List<HitBox> frameHitBoxList;
    
    public HitBoxFileReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }
    

    @Override
    protected Object nextObject() {
        if(frameHitBoxList==null){
            this.frameHitBoxList = new LinkedList<>();
        }
        
        
        while(true){
            String rowString= nextLine();
            if(rowString==null || rowString.equals("")){
                if(frameHitBoxList.size()==0){
                    return null;//end of file reached
                }else{
                    return retrieveAccumulatedHitBoxes();
                }
                
            }
            
            HitBox hitbox = HitBoxFactory.getHitBox(rowString);
            if(hitbox==null){
                return retrieveAccumulatedHitBoxes();
            }else{
                frameHitBoxList.add(hitbox);
            }
            
        }
    }

    private HitBox retrieveAccumulatedHitBoxes() {
      //send last group of hitboxes up. empties list
        if(frameHitBoxList.size()==0){
            return new NullHitBox();
        }
        if(frameHitBoxList.size()==1){
            HitBox hitboxToReturn = frameHitBoxList.get(0);
            frameHitBoxList.clear();
            return hitboxToReturn;
        }
        //return group hit box for >1
        HitBox[] hitboxes = new HitBox[frameHitBoxList.size()];
        for(int i=0;i<hitboxes.length;i++){
            hitboxes[i] = frameHitBoxList.get(i);
        }
        HitBox groupHitBox = new GroupHitBox(hitboxes);
        frameHitBoxList.clear();
        return groupHitBox;
    }

}

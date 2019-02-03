package hitbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import images.Animation;
import images.Picture;
import objectReader.ObjectFileReader;

public class HitboxReader {
    private static final String HITBOX_INFO_FILE_NAME = "hitboxes.txt";
    private static Map<String,HitBox[]> hitboxArrayCache = new HashMap<>();
    
    public static HitBox[] getHitboxes(File directory) {
        HitBox[] hitboxes = getHitBoxInfo(directory.getPath() + "/" + HITBOX_INFO_FILE_NAME);
        return hitboxes;
//        if(hitboxes==null){//there is no hitbox file
//            
//        }else{
//            if(hitboxes.length!=picArray.length){
//                throw new RuntimeException("hit box and pic array lengths are not the same in " + directory.getPath());
//            }
//            animation = new HitBoxSyncedAnimation(picArray,period,hitboxes);
//            animationCache.put(directory.getPath(), animation);
//            return animation;
//        }
    }
    
  //returns null if file not found
    private static HitBox[] getHitBoxInfo(String location){
        HitBox[] hitboxes = hitboxArrayCache.get(location);
        if(hitboxes!=null){
System.out.println("retrieved hitbox array at " + location + " from cache");
            return hitboxes;
        }
        
        
        List<HitBox> hitboxList = new ArrayList<>();
        try{
           ObjectFileReader obr = new HitBoxFileReader(location);
           for(Object o:obr){
               HitBox hb = (HitBox) o;
               hitboxList.add(hb);
           }
           hitboxes = new HitBox[hitboxList.size()];
           for(int i=0;i<hitboxes.length;i++){
               hitboxes[i] = hitboxList.get(i);
           }
           
           hitboxArrayCache.put(location, hitboxes);
           return hitboxes;
        }catch(FileNotFoundException e){
            System.out.println("problem finding hitbox file at location: " + location);
            return null;
        }
        
    }

}

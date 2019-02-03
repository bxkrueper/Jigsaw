package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import images.Animation;
import images.Picture;
import images.StaticPicture;
import objectReader.ObjectFileReader;
import sounds.Sound;
import sounds.SoundMedia;
import resourceMethods.ResourceMethods;


public class ResourceGetter {
    public static final String[] POSSIBLE_PICTURE_FORMATS = new String[]{"png","jpg","jpeg"};
    private final static String DEFAULT_IMAGE_FILE_NAME = "images/missingImage.jpg";
    private static final String ANIMATION_INFO_FILE_NAME = "info.txt";//for animations. name of text file in animation folder
    //info.txt format: it only reads the first number in each line. Line 1: period in ms
    //animation frame files must be in the same folder and have integers as names.
    //there are as many frames as the highest numbered file + 1. missing frames use the default pic
    
    
    
    private static BufferedImage defaultPicture = null;
    //flyweight pattern
    private static Map<String,BufferedImage> pictureCache = new HashMap<>();
    private static Map<String,Animation> animationCache = new HashMap<>();
    private static Map<String,Picture[]> animationFramesCache = new HashMap<>();
    
    
    //if the filename does not have an extension and is not a directory, it recursively checks if adding the extension work
    public static BufferedImage getBufferedImage(File file){
        BufferedImage image = getBufferedImageFly(file);
        if(image!=null){
            return image;
        }
        
        //can't find picture in cache or from file. if it doesn't have an extension, try adding possible extensions
        if(ResourceMethods.getExtension(file.getPath()).equals("")){//it has no extension but is not a directory. try adding extensions
            for(String ext:POSSIBLE_PICTURE_FORMATS){
                image = getBufferedImageFly(new File(file.getPath()+"."+ext));
                if(image!=null){
                    return image;
                }
            }
            //tried everything. still can't find it
            System.out.println("can't find image at " + file.getPath() + " with any valid extensions");
            return getDefaultBufferedImage();
        }else{//already has an extension that doesn't work. give up
            System.out.println("can't find image at " + file.getPath());
            return getDefaultBufferedImage();
        }
    }
    public static BufferedImage getBufferedImage(String fileName){
        return getBufferedImage(new File(fileName));
    }
    
    

    //tries to get a static picture from a set file (no animation)
    //returns null if not in cache and can't get from file
    private static BufferedImage getBufferedImageFly(File file){
        BufferedImage bufferedImage = pictureCache.get(file.getPath());
        if(bufferedImage==null){//if it is not in the cache
            try{
                bufferedImage = ImageIO.read(file);//try getting it from the file
            }catch(IOException e){
                return null;//can't get it from file
            }
            //got from file. put in cache
            System.out.println("loaded " + file.getPath());
            pictureCache.put(file.getPath(), bufferedImage);
        }else{
            System.out.println("loaded " + file.getPath() + " from cache");
        }
        return bufferedImage;
    }
    
    public static Picture getPicture(String fileLocation){
        return getPicture(new File(fileLocation));
    }
    //get picture or animation
    public static Picture getPicture(File file){
        if(file.isDirectory()){//directories are for animations
            return getAnimation(file);
        }else{
            return new StaticPicture(getBufferedImage(file));
        }
    }
    
    //returns a reference to the default picture
    private static BufferedImage getDefaultBufferedImage(){
        if(defaultPicture!=null){
            return defaultPicture;
        }
        
        try {
            defaultPicture = ImageIO.read(new File(DEFAULT_IMAGE_FILE_NAME));
        } catch (IOException f) {
            System.out.println("can't find default image! (should be at " + DEFAULT_IMAGE_FILE_NAME + ")");
        }
        return defaultPicture;
    }
    
    public static Picture getDefaultPicture(){
        return new StaticPicture(getDefaultBufferedImage());
    }
    
    
    
    
    //file is assumed to be a directory
    public static Animation getAnimation(String directory){
        return getAnimation(new File(directory));
    }
    //file is assumed to be a directory  !!!need to set listener afterwards
    //this is a prototype pattern. animations retrieved from the cache are copies except for their unique
    //instance variables like what frame it is on and the period
    public static Animation getAnimation(File directory) {
        Animation animation = animationCache.get(directory.getPath());
        if(animation!=null){
System.out.println("retrieved animation at " + directory.getPath() + " from cache");
            return animation.freshCopy();
        }
        
        
        //get the period
        int[] animationInfo = getAnimationInfo(directory.getPath() + "/" + ANIMATION_INFO_FILE_NAME);
        int period = animationInfo[0];
        //get the pictures
        Picture[] picArray = getAnimationFrames(directory);
        animation = new Animation(picArray,period);
        animationCache.put(directory.getPath(), animation);
        return animation;
    }
    
    
    
    
    private static Picture[] getAnimationFrames(File directory){
        Picture[] picArray = animationFramesCache.get(directory.getPath());
        if(picArray!=null){
System.out.println("retrieved animation frames at " + directory.getPath() + " from cache");
            return picArray;
        }
        
        
        //get all files that will be used as frames
        File[] frameFiles = resourceMethods.ResourceMethods.getAllFiles(directory,new AnimationFilenameFilter());//should be all acceptable picture types with numbers as names
        //convert frameFiles to list and sort
        List<File> fileList = new ArrayList<>(frameFiles.length);
        for(File file:frameFiles){
            fileList.add(file);
        }
        Collections.sort(fileList, (File f1, File f2) -> getFileNameNumber(f1.getName()) - getFileNameNumber(f2.getName()));

        //find total amount of pictures based on the highest numbered frame
        int numPictures = getFileNameNumber(fileList.get(fileList.size()-1).getName())+1;
        
        //pic array currently all null
        picArray = new Picture[numPictures];
        
        //fill pic array with what is available
        for(File file: fileList){
            int fileNumber = getFileNameNumber(file.getName());
            picArray[fileNumber] = getPicture(file);
        }
        
        //replace all nulls with default picture
        for(int i=0;i<picArray.length;i++){
            if(picArray[i]==null){
                picArray[i] = new StaticPicture(getDefaultBufferedImage());
            }
        }
        
        animationFramesCache.put(directory.getPath(), picArray);
        return picArray;
    }
    
    private static boolean isAnimation(String fileName){
        return isAnimation(new File(fileName));
    }
    
    private static boolean isAnimation(File file){
        if(!file.isDirectory()){
            return false;
        }
        
        File animationInfo = new File(file.getPath()+"/"+ANIMATION_INFO_FILE_NAME);///////////////use method of different seperators?
        if(animationInfo.exists()){
            return true;
        }
        
        return false;
    }
    
    
    

    
    //gets info from ANIMATION_INFO_FILE_NAME which should be in the animation folder
    private static int[] getAnimationInfo(String fileName){////////////should throw exception?
        Scanner scanf;
        int period;
        try {
            scanf = new Scanner(new File(fileName));
            period = scanf.nextInt();
            scanf.close();
        } catch (FileNotFoundException e) {
            System.out.println("can't find info at " + fileName);
            e.printStackTrace();
            period = -1;
        }
        
        return new int[]{period};
    }
    
    
    
    
    
    
    
    private static int getFileNameNumber(String fileName){
        return Integer.parseInt(ResourceMethods.stripExtension(fileName));
    }
    
    
    
    
    //accepts extensions in POSSIBLE_PICTURE_FORMATS
    public static class PictureFilenameFilter implements FilenameFilter{

        @Override
        public boolean accept(File dir, String name) {
            for(String str: POSSIBLE_PICTURE_FORMATS){
                if(name.endsWith("."+str)){
                    return true;
                }
            }
            File file = new File(dir+"/"+name);///////////////use method of different seperators?
            return isAnimation(file);
        }
        
    }
    
    //accepts files that are picture types and have integers for names
    public static class AnimationFilenameFilter extends PictureFilenameFilter{

        @Override
        public boolean accept(File dir, String name) {
            return (super.accept(dir, name) && nameIsInt(name));
        }

        private boolean nameIsInt(String name) {
            try{
                int num = Integer.parseInt(ResourceMethods.stripExtension(name));
            }catch(NumberFormatException e){
                return false;
            }
            return true;
        }
        
    }
    
    
    //gets pictures specifically for the file chooser
    public static class PictureFileFilter extends javax.swing.filechooser.FileFilter{

        @Override
        public boolean accept(File file) {
            if(file.isDirectory()){
                return true;
            }
            for(String str: POSSIBLE_PICTURE_FORMATS){
                if(file.getName().endsWith("."+str)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            // TODO Auto-generated method stub
            return "Just my picture types";
        }
        
    }
}

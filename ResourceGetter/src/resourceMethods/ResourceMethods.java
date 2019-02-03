package resourceMethods;

import java.io.File;
import java.io.FilenameFilter;

public class ResourceMethods {
    //if no extension returns ""
    public static String getExtension(String fileName){
        int lastIndexOfPeriod = fileName.lastIndexOf('.');
        if(lastIndexOfPeriod==-1){
            return "";
        }else{
            return fileName.substring(lastIndexOfPeriod+1,fileName.length());
        }
        
    }

    //if there is no extension, returns the string as is
    public static String stripExtension(String fileName){
        int lastIndexOfPeriod = fileName.lastIndexOf('.');
        if(lastIndexOfPeriod==-1){
            return fileName;
        }else{
            return fileName.substring(0,lastIndexOfPeriod);
        }
    }
    
    public static File getFileIfExists(String location){
        File file = new File(location);
        if(file.exists()){
            return file;
        }else{
            return null;
        }
    }
    
    public static File[] getAllFiles(String directoryLocation, FilenameFilter filter){
        return getAllFiles(new File(directoryLocation),filter);
    }
    
    public static File[] getAllFiles(File directory, FilenameFilter filter){
        if(!directory.exists()){
            System.out.println("directory " + directory.getPath() + " does not exist!");
            return null;
        }
        if(!directory.isDirectory()){
            System.out.println(directory.getPath() + " is not a directory!");
            return null;
        }
        File[] allFileNamesArray = directory.listFiles(filter);
        return allFileNamesArray;
        
    }
}

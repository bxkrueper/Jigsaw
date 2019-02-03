package objectReader;

/**
 * Programmer: Benjamin Krueper
 * Class: CS431
 * Project #: 1: Schedulers
 * 
 * Purpose: this abstract class provides the framework for reading in objects stored in text files.
 * the subclasses only need to know how the text file is formatted and how to build objects with
 * that information
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public abstract class ObjectFileReader implements Iterable<Object>, Iterator<Object>{
    private Object nextObject;
    private BufferedReader reader;
    
    public ObjectFileReader(String fileName) throws FileNotFoundException{
        reader = new BufferedReader(new FileReader(fileName));
        nextObject = nextObject();
    }
    
    @Override
    public boolean hasNext() {
        return nextObject!=null;
    }

    @Override
    public Iterator<Object> iterator() {
        return this;
    }

    @Override
    public Object next() {
        Object objectToReturn = nextObject;
        nextObject = nextObject();
        return objectToReturn;
    }
    
    protected abstract Object nextObject();
    
    //may return null
    protected String nextLine(){
        try {
            String nextLine = reader.readLine();
            if(nextLine==null){
                closeScanner();
            }
            return nextLine;
        } catch (IOException e) {
            closeScanner();
            return null;
        }
    }
    
    private void closeScanner(){
        try {
            reader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

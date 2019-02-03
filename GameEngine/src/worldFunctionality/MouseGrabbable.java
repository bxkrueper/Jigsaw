package worldFunctionality;

import coordinate.Coordinate;
import worldFunctionality.MouseButtonReact.ButtonType;

//grabbed it
public interface MouseGrabbable extends MouseTargetable{
    boolean acceptBeingGrabbed(ButtonType bt);//return true if the object wants to be grabbed
    void unGrabbed();//what to do when grabbing stops
    
    //grab location is where the mouse is now
    void actOnGrab(Coordinate grabLocation, Coordinate grabSorce,Coordinate objectOrigionalLocation);
}

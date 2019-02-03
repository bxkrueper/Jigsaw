//handles click and drag for targets that are not being blocked by targets in front of them that want to hog the mouse
//todo: make dragging the camera when clicking on the background work
package world;

import java.util.LinkedList;
import java.util.List;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import worldFunctionality.MouseButtonReact;
import worldFunctionality.MouseClickedOnReact;
import worldFunctionality.MouseGrabbable;
import worldFunctionality.MouseScrolledOnReact;
import worldFunctionality.MouseTargetable;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;

public class MouseSelectManager {
    
    private List<MouseTargetable> possibleTargets;//every possible target in the world. objects at the front of the list have first dibs on taking the mouse
    private List<MouseTargetable> selectedList;//objects that may need to do something from the last down click. This is cleared when it turns out to be a grab and grabables are transfered to the grabbed list, otherwise everything in it is later told to click
    private List<GrabPair> grabbed;//objects currently in grab mode
    private Coordinate mouseLastPressed;//remembers where the mouse originally clicked when grabbing
    private boolean actionIsAClick;
    private ButtonType activeButton;
    private boolean buttonDown;
    
    
    public MouseSelectManager(){
        possibleTargets = new LinkedList<MouseTargetable>();
        selectedList  = new LinkedList<MouseTargetable>();
        grabbed = new LinkedList<GrabPair>();
        mouseLastPressed = null;
        actionIsAClick = false;
        buttonDown = false;
    }

    //adds to the beginning so it gets priority for clicks and sincs with drawables
    public void addToTargetable(MouseTargetable mt) {
        possibleTargets.add(0,mt);//add to front to sinc with drawable    is drawable order sync fixed? make sure to remember that with other features using these lists
    }

    //removes from deeper lists too
    public void removeFromTargetable(MouseTargetable mt) {
        possibleTargets.remove(mt);
        selectedList.remove(mt);
        if(mt instanceof MouseGrabbable){
            grabbed.remove((MouseGrabbable) mt);
        }
    }
    
    //if 
    public void notifyMouseMoved(Coordinate c) {
        if(actionIsAClick){//not a click anymore!  add to grabbed, remove others from selectedList
            actionIsAClick = false;
            for(MouseTargetable mt: selectedList){
                if(mt instanceof MouseGrabbable){
                    MouseGrabbable mg = (MouseGrabbable) mt;
                    if(mg.acceptBeingGrabbed(activeButton)){
                        grabbed.add(new GrabPair(mg, mg.getPosition()));
                    }
                }
            }
            actOnGrab(c);
            selectedList.clear();
        }else if(buttonDown){
            actOnGrab(c);
        }
    }
    
    //only reacts to one mouse button at a time
    //scroll immediately counts as a click
    public void notifyMouseButtonPressed(ButtonType bt, ButtonPressType bpt, double mouseWorldX, double mouseWorldY) {
        //grabbed should be empty
        
        if(bt==ButtonType.SCROLL){//scroll can't drag. immediately clicks
            if(buttonDown){
                return;
            }else{
                updateSelectedList(bt,mouseWorldX,mouseWorldY);
                for(MouseTargetable mt: selectedList){
                    if(mt instanceof MouseScrolledOnReact){
                        ((MouseScrolledOnReact) mt).reactToMouseScrolledOn(bpt,mouseWorldX,mouseWorldY);
                    }
                }
                selectedList.clear();
                return;
            }
            
        }
        
        if(bpt==ButtonPressType.DOWN){
            if(buttonDown){
                return;//only drags with one button at a time
            }
            else{
                activeButton = bt;
                buttonDown = true;
                actionIsAClick = true;
                mouseLastPressed = new Coordinate2DDouble(mouseWorldX,mouseWorldY);
                updateSelectedList(bt,mouseWorldX,mouseWorldY);
                for(MouseTargetable mt: selectedList){
                    if(mt instanceof MouseClickedOnReact){
                        ((MouseClickedOnReact) mt).reactToMouseDownOn(activeButton, mouseWorldX, mouseWorldY);
                    }
                }
            }
        }else if(bt==activeButton && bpt== ButtonPressType.UP){
            mouseLastPressed = null;
            buttonDown = false;
            if(actionIsAClick){//apply click
                for(MouseTargetable mt: selectedList){
                    if(mt instanceof MouseClickedOnReact){
                        ((MouseClickedOnReact) mt).reactToMouseClickedOn(activeButton, mouseWorldX, mouseWorldY);
                    }
                }
                selectedList.clear();
            }else{//let go of grab
                //empty grabbed list
                for(GrabPair gp: grabbed){
                    gp.mouseGrabObject.unGrabbed();
                }
                grabbed.clear();
            }
            
            
        }
    }
    
    public void removeFromGrabbed(MouseGrabbable mg){
        grabbed.remove(mg);
    }
    
    public boolean isNothingSelected(){
        return selectedList.isEmpty() && grabbed.isEmpty();
    }

    public void notifyMouseEntered(Coordinate c) {
        notifyMouseMoved(c);
    }

    public void notifyMouseExited(double mouseWorldX, double mouseWorldY) {
        
    }
    
    
    //activates everything in the grabbed list to do what they do when grabbed
    private void actOnGrab(Coordinate grabLocation){//grab location is where the mouse is now
        for(GrabPair gp: grabbed){
            gp.mouseGrabObject.actOnGrab(grabLocation, mouseLastPressed,gp.originalPosition);
        }
    }


    //select all targets under mouse until stopped
    private void updateSelectedList(ButtonType bt,double mouseX, double mouseY){
        
        for(MouseTargetable pt:possibleTargets){
            if(pt.occupies(mouseX, mouseY)){
                if(pt.acceptTarget(bt)){
                    selectedList.add(pt);
                }
                if(pt.consumeTargetable(bt)){
                    break;
                } 
            }
        }
    }
    
    //don't need to reorder other lists?
    //called by sendToFront in world class. Assumed it is already sent to the front (end) of the draw list
    public void sendToFront(MouseTargetable mt) {
        possibleTargets.remove(mt);
        addToTargetable(mt);
    }
    
    //don't need to reorder other lists?
    //called by sendToBack in world class. Assumed it is already sent to the back (start) of the draw list
    public void sendToBack(MouseTargetable mt) {
        possibleTargets.remove(mt);
        possibleTargets.add(mt);
    }


    private class GrabPair{
        public MouseGrabbable mouseGrabObject;
        public Coordinate originalPosition;
        
        public GrabPair(MouseGrabbable mouseGrabObject,Coordinate originalPosition){
            this.mouseGrabObject = mouseGrabObject;
            this.originalPosition = originalPosition.copy();
        }
    }
    
}

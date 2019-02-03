package worldUtility;

import camera.Camera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import coordinate.Coordinate2DInteger;
import world.WorldObject;
import worldFunctionality.MouseGrabbable;
import worldFunctionality.MouseButtonReact.ButtonType;

//when added to the world, this class moves the camera it is given when the panel is dragged by the given button type
public class CameraDragManager extends WorldObject implements MouseGrabbable{
    
    private Camera camera;
    private ButtonType grabbingButton;
    
    private Coordinate2DInteger panelGrabSource;
    private Coordinate worldOriginalPosition;
    
    
    public CameraDragManager(Camera camera,ButtonType grabbingButton){
        this.camera = camera;
        this.grabbingButton = grabbingButton;
    }
    @Override
    public void doOnAdd() {
    }

    @Override
    public void doOnDelete() {
    }
    
    @Override
    public boolean acceptTarget(ButtonType bt) {
        return bt==grabbingButton;
    }
    @Override
    public boolean consumeTargetable(ButtonType bt) {
        return false;
    }
    @Override
    public Coordinate getPosition() {
        return Coordinate2DDouble.zero;//////////////////////////////unused;
    }
    @Override
    public boolean occupies(double x, double y) {
        return true;
    }
    
    
    
    
    
    @Override
    public boolean acceptBeingGrabbed(ButtonType bt) {
        if(bt==grabbingButton){
            this.panelGrabSource = new Coordinate2DInteger(getInputStateReader().getMousePanelX(),getInputStateReader().getMousePanelY());
            this.worldOriginalPosition = camera.getPosition().copy();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void unGrabbed() {
    }

    @Override
    public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
        Coordinate2DInteger currentMousePanelLocation = getInputStateReader().getMousePanelPosition();
        camera.setX(worldOriginalPosition.getX()+((panelGrabSource.getX()-currentMousePanelLocation.getX())*camera.getZoom()));
        camera.setY(worldOriginalPosition.getY()+((currentMousePanelLocation.getY()-panelGrabSource.getY())*camera.getZoom()));//swapped because y is inverted for panel coordinates
    }
    
}
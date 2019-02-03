package worldUtility;

import camera.ZoomingCamera;
import coordinate.Coordinate;
import world.WorldObject;
import worldFunctionality.MouseScrolledOnReact;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;

//when added to the world, this makes the camera scroll when the mouse wheel is scrolled
public abstract class CameraZoomScrollManager extends WorldObject implements MouseScrolledOnReact{
    
    private ZoomingCamera camera;
    
    
    public CameraZoomScrollManager(ZoomingCamera camera){
        this.camera = camera;
        
    }
    
    public ZoomingCamera getCamera() {
        return camera;
    }
    @Override
    public boolean acceptTarget(ButtonType bt) {
        return true;
    }

    @Override
    public boolean consumeTargetable(ButtonType bt) {
        return false;
    }

    @Override
    public Coordinate getPosition() {
        return null;
    }

    @Override
    public boolean occupies(double x, double y) {
        return true;
    }
    
    @Override
    public void doOnAdd() {
    }
    @Override
    public void doOnDelete() {
    }
}

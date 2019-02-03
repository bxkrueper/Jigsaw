package camera;

import worldFunctionality.MouseButtonReact.ButtonType;
import worldUtility.CameraDragManager;
import worldUtility.CameraZoomScrollManager;
import worldUtility.FixedMultiplierZoomManager;

//this camera automatically is able to zoom and get dragged around
//this is kind of my default camera as it allows freedom of movement and zooming
public class ZoomDragCamera extends ZoomingCamera{
    
    CameraDragManager cdm;
    CameraZoomScrollManager czsm;
    ButtonType grabbingButton;

    public ZoomDragCamera(double gameX, double gameY, double zoom, double zoomIncrement,ButtonType grabbingButton) {
        super(gameX, gameY, zoom);
        this.grabbingButton = grabbingButton;
        this.cdm = new CameraDragManager(this,grabbingButton);
        this.czsm = new FixedMultiplierZoomManager(this,zoomIncrement);
    }
    
    @Override
    public void doOnAdd() {
        addToWorld(cdm);
        addToWorld(czsm);
    }
    @Override
    public void doOnDelete() {
        cdm.deleteFromWorld();
        czsm.deleteFromWorld();
    }

}

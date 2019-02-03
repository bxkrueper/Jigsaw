package worldUtility;

import camera.ZoomingCamera;
import worldFunctionality.MouseButtonReact.ButtonPressType;

public class FixedMultiplierZoomManager extends CameraZoomScrollManager{

    private double zoomScrollIncrement;
    
    public FixedMultiplierZoomManager(ZoomingCamera camera, double zoomScrollIncrement) {
        super(camera);
        this.zoomScrollIncrement = zoomScrollIncrement;
    }
    
    @Override
    public void reactToMouseScrolledOn(ButtonPressType bpt, double x, double y) {
        if(bpt==ButtonPressType.UP){
            getCamera().setZoom(getCamera().getZoom()/zoomScrollIncrement);
        }else{
            getCamera().setZoom(getCamera().getZoom()*zoomScrollIncrement);
        }
    }

    

}

package test;

import camera.ZoomingCamera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseScrolledOnReact;
import worldFunctionality.MainTickReact;

public class SquareWorldCamera extends ZoomingCamera implements MainTickReact{
//drag manually here. use isNothingSelected  use class for it?
    
    /////use coordinate2D.Integer
    private Coordinate2DDouble panelGrabSource;
    private Coordinate worldOriginalPosition;
    
    
    public SquareWorldCamera(double gameX, double gameY, double zoom) {
        super(gameX, gameY, zoom);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doOnMainTick() {
        if(getInputStateReader().isKeyDown(37)){
            setX(getX()-getZoom()*10);
        }
        if(getInputStateReader().isKeyDown(38)){
            setY(getY()+getZoom()*10);
        }
        if(getInputStateReader().isKeyDown(39)){
            setX(getX()+getZoom()*10);
        }
        if(getInputStateReader().isKeyDown(40)){
            setY(getY()-getZoom()*10);
        }
        
    }
    
    //mouse position panel
    public void acceptedGrab(Coordinate2DDouble panelGrabSource) {
        this.panelGrabSource = new Coordinate2DDouble(panelGrabSource);
        this.worldOriginalPosition = getPosition().copy();
    }
    
    //panelGrabSource: int      currentMousePanelLocation: lossy calculation from world to panel
    public void actOnGrab(Coordinate2DDouble currentMousePanelLocation) {
//        System.out.println("panelGrabSource: " + panelGrabSource + "   currentMousePanelLocation: " + currentMousePanelLocation);
        setX(worldOriginalPosition.getX()+((panelGrabSource.getX()-currentMousePanelLocation.getX())*getZoom()));
        setY(worldOriginalPosition.getY()+((currentMousePanelLocation.getY()-panelGrabSource.getY())*getZoom()));//swapped because y is inverted for panel coordinates
    }

    

}

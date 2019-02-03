package jigsaw;

import camera.ZoomingCamera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseScrolledOnReact;

public class JigSawCamera extends ZoomingCamera implements MouseScrolledOnReact, MyListener{
//drag manually here. use isNothingSelected  use class for it?
    
    /////use coordinate2D.Integer
    //used for dragging
    private Coordinate2DDouble panelGrabSource;
    private Coordinate worldOriginalPosition;
    
    public double minZoom;
    public double maxZoom;
    public double leftBound;
    public double lowerBound;
    public double rightBound;
    public double upperBound;
    
    //initiated and used after puzzle is finished.  for the moving to center
    private LimitedTickTimer ltTimer;
    private double xStep,yStep,zoomStep;
    
    private boolean isInAnimation;
    
    
    public JigSawCamera(double gameX, double gameY, double zoom, double minZoom, double maxZoom, double leftBound, double lowerBound, double rightBound, double upperBound) {
        super(gameX, gameY, zoom);
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.isInAnimation = false;
    }

//    @Override
//    public void doOnTick() {
//        if(getInputStateReader().isKeyDown(37)){
//            setGameX(getGameX()-getZoom()*10);
//        }
//        if(getInputStateReader().isKeyDown(38)){
//            setGameY(getGameY()+getZoom()*10);
//        }
//        if(getInputStateReader().isKeyDown(39)){
//            setGameX(getGameX()+getZoom()*10);
//        }
//        if(getInputStateReader().isKeyDown(40)){
//            setGameY(getGameY()-getZoom()*10);
//        }
//        
//    }
    
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
        conformToBounds();
    }

    @Override
    public boolean occupies(double x, double y) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean acceptTarget(ButtonType bt) {
        // TODO Auto-generated method stub
        return !isInAnimation;
    }

    @Override
    public boolean consumeTargetable(ButtonType bt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reactToMouseScrolledOn(ButtonPressType bpt, double x, double y) {
        if(isInAnimation){
            return;
        }
        if(bpt==ButtonPressType.UP){
            double newZoom = getZoom()/1.125;
            if(newZoom<minZoom){
                setZoom(minZoom);
            }else{
                setZoom(newZoom);
            }
        }else{
            double newZoom = getZoom()*1.125;
            if(newZoom>maxZoom){
                setZoom(maxZoom);
            }else{
                setZoom(newZoom);
            }
            conformToBounds();
        }
    }

    private void conformToBounds() {
        if(getX()<leftBound){
            setX(leftBound);
        }
        if(getX()>rightBound){
            setX(rightBound);
        }
        if(getY()<lowerBound){
            setY(lowerBound);
        }
        if(getY()>upperBound){
            setY(upperBound);
        }
    }

    public void setMaxZoom(double maxZoom) {
        this.maxZoom = maxZoom;
    }

    public void setBounds(double leftBound, double rightBound, double lowerBound, double upperBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    //width and height are of picture
    public void focusOnFinishedPuzzle(int width, int height,int panelWidth,int panelHeight) {
        isInAnimation = true;
        int tickPeriod = 25;
        int numberOfTicks = 40;
        this.ltTimer = new LimitedTickTimer(tickPeriod,numberOfTicks,this,"animation");
        double goalX=0,goalY=(-height/15-width/2-10)/2;
        this.xStep = (goalX-getX())/numberOfTicks;//to (0,0)
        this.yStep = (goalY-getY())/numberOfTicks;
        double goalZoom = width/(double)panelWidth*2;//////////height?   *2 Temporary
        this.zoomStep = (goalZoom-getZoom())/numberOfTicks;
        
//        System.out.println("goalY: " + goalY + " yStep: " + yStep + " goal Zoom: " + goalZoom);
        ltTimer.start();
    }

    @Override
    public void eventHappened(MyEvent e) {
        if(e.getCommand().equals("animation")){
            this.setPosition(this.getX()+xStep,this.getY()+yStep);
            this.setZoom(this.getZoom()+zoomStep);
            this.repaintWorld();
        }else if(e.getCommand().equals("done")){
            isInAnimation = false;
        }
    }

    

}

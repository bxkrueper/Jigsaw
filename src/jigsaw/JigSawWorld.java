package jigsaw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import camera.Camera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Animation;
import images.Picture;
import images.ResourceGetter;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import world.World;
import world.WorldObject;
import worldFunctionality.KeyReact;
import worldFunctionality.KeyReact.PressType;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseGrabbable;
import worldUtility.DebugGrid;
import worldUtility.FlippedTiledBackground;
import worldUtility.WorldButton;

public class JigSawWorld extends World implements MyListener{
    
    private JigSawCamera camera;
    private JigSawWorldLogic swl;
    private GoldOutline goldOutline;
    private JigSawPuzzle jigSawPuzzle;
    private FinishedPuzzle finishedPuzzle;
    private MyListener listener;
    
    private int pictureWidth;
    private int pictureHeight;
    
    public JigSawWorld(MyListener l) {
        this.listener = l;
        this.swl = new JigSawWorldLogic();
        add(swl);//order may matter here
        
        this.camera = new JigSawCamera(0.0,0.0,2,0.25,10,-200,-200,200,200);//order may matter here
        add(camera);
        
        add(new DebugGrid());
        
        add(new FlippedTiledBackground("images/Green Felt 2.jpg",100,25));
    }
    
    public void newPuzzle(Picture puzzlePic, int rows, int columns, boolean rotate){
        if(goldOutline!=null){
            remove(goldOutline);
        }
        if(jigSawPuzzle!=null){
            remove(jigSawPuzzle);
        }
        if(finishedPuzzle!=null){
            remove(finishedPuzzle);
        }
        
        this.pictureWidth = puzzlePic.getWidth();
        this.pictureHeight = puzzlePic.getHeight();
        
        double leftBound = -pictureWidth*10;
        double rightBound = pictureWidth*10;
        double lowerBound = -pictureHeight*10;
        double upperBound = pictureHeight*10;
        
        camera.setMaxZoom(getMaxZoom(pictureWidth,pictureHeight));
        camera.setBounds(leftBound,rightBound,lowerBound,upperBound);
        
        this.goldOutline = new GoldOutline(new Coordinate2DDouble(-pictureWidth/2,-pictureHeight/2),pictureWidth,pictureHeight);
        this.jigSawPuzzle = new JigSawPuzzle(puzzlePic,rows,columns,rotate,this);
        
        //so it redraws every frame if the picture is an animation
        if(puzzlePic instanceof Animation){
            Animation a = (Animation) puzzlePic;
            a.setListener(this);
        }
        
        add(goldOutline);
        add(jigSawPuzzle);
        
    }
    
    private double getMaxZoom(int pictureWidth,int pictureHeight) {
        if(pictureWidth>pictureHeight){
            return pictureWidth/20;
        }else{
            return pictureHeight/20;
        }
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public int getInitialFramePeriod() {
        return 0;
    }
    
//    @Override
//    public void drawDeBug(Graphics2D g) {
//        g.setColor(Color.BLACK);
//        g.drawString("Mouse X panel: " + Double.toString(getInputStateReader().getMousePanelX()), 0, 80);
//        g.drawString("Mouse Y panel: " + Double.toString(getInputStateReader().getMousePanelY()), 0, 110);
//        g.drawString("Mouse X world: " + Double.toString(getMouseWorldX()), 0, 140);
//        g.drawString("Mouse Y world: " + Double.toString(getMouseWorldY()), 0, 170);
//        g.drawString("Panel Width: " + Double.toString(getWorldPanel().getWidth()), 0, 200);
//        g.drawString("Panel Height: " + Double.toString(getWorldPanel().getHeight()), 0, 230);
//        g.drawString("Camera center X: " + Double.toString(getCamera().getX()), 0, 260);
//        g.drawString("Camera center Y: " + Double.toString(getCamera().getY()), 0, 290);
//        g.drawString("Camera zoom: " + Double.toString(camera.getZoom()), 0, 320);
//        g.drawString("Camera top: " + Double.toString(getCamera().getTopCoordinate(getWorldPanel().getHeight())), 0, 350);
//        g.drawString("Camera bottom: " + Double.toString(getCamera().getBottomCoordinate(getWorldPanel().getHeight())), 0, 380);
//        g.drawString("Camera left: " + Double.toString(getCamera().getLeftCoordinate(getWorldPanel().getWidth())), 0, 410);
//        g.drawString("Camera right: " + Double.toString(getCamera().getRightCoordinate(getWorldPanel().getWidth())), 0, 440);
////        wp.drawStringPanel("Mouse target: " + currentMouseTarget(), 0, 470,Color.BLACK,font);
//        getInputStateReader().draw(g, 50, 510);
//  }
    
    
    public final void notifyMouseMoved(int x, int y){
        super.notifyMouseMoved(x,y);
        if(getInputStateReader().isAnyMouseButtonDown()){
            repaint();
        }
        
    }

    public final void notifyMouseButton(ButtonType bt,ButtonPressType bpt, int x, int y){
        super.notifyMouseButton(bt, bpt, x, y);
        repaint();
    }
    
//    public final void notifyKeyPressed(int keyCode){
//        super.notifyKeyPressed(keyCode);
//        if(keyCode ==KeyEvent.VK_F3){
//            repaint();
//        }
//    }

    public final void notifyKeyReleased(int keyCode){
        super.notifyKeyReleased(keyCode);
        if(keyCode ==KeyEvent.VK_F3){
            repaint();
        }
    }
    
//    public final void notifyPanelVisible(){
//        super.notifyPanelVisible();
//        repaint();
//    }

    
    
    
    
    /////////////todo: change camera too, this class controls motion?
    @Override
    public void eventHappened(MyEvent e) {
        if(e.getCommand().equals("frame changed")){
            this.repaint();
        }else if(e.getCommand().equals("Puzzle Finished")){
            this.finishedPuzzle = jigSawPuzzle.getFinishedPicture();
            finishedPuzzle.setListener(this);
            remove(jigSawPuzzle);
            add(finishedPuzzle);
            
            Picture pic = finishedPuzzle.getPic();
            camera.focusOnFinishedPuzzle(pic.getWidth(),pic.getHeight(),this.getWorldPanel().getWidth(),this.getWorldPanel().getHeight());
            
            listener.eventHappened(new MyEvent(this,"Puzzle Finished",null));
        }else if(e.getCommand().equals("Animation done")){
            add(new WorldButton(new Rectangle2D.Double(-pictureWidth/4,-pictureHeight/2-pictureHeight/15-pictureWidth/2-10,pictureWidth/2,pictureWidth/2),new Coordinate2DDouble(-pictureWidth/4,-pictureHeight/2-pictureHeight/15-pictureWidth/2-10),pictureWidth/2,pictureWidth/2,ResourceGetter.getPicture("images/JigSawGame/Menu Button.jpg"),listener,"Puzzle Finished Button Pressed"));
        }
    }
    
    
    
    
    
    private class JigSawWorldLogic extends WorldObject implements KeyReact, MouseGrabbable{
        @Override
        public void reactToKey(int keyCode, PressType t) {
            if(keyCode==KeyEvent.VK_F3 && t==KeyReact.PressType.RELEASED){
                setDeBugMode(!isDeBugMode());
            }
        }

        @Override
        public boolean occupies(double x, double y) {
            return true;
        }

        @Override
        public boolean acceptTarget(ButtonType bt) {
            return true;
        }

        @Override
        public boolean consumeTargetable(ButtonType bt) {
            return true;////
        }

        @Override
        public boolean acceptBeingGrabbed(ButtonType bt) {
            if(bt==ButtonType.MIDDLE){
                camera.acceptedGrab(new Coordinate2DDouble(getInputStateReader().getMousePanelX(),getInputStateReader().getMousePanelY()));
                return true;
            }else{
                return false;
            }
        }

        @Override
        public void unGrabbed() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
            camera.actOnGrab(new Coordinate2DDouble(getInputStateReader().getMousePanelX(),getInputStateReader().getMousePanelY()));
//            camera.actOnGrab(camera.worldPositionToPanelPosition(grabLocation,getWorldPanel().getWidth(), getWorldPanel().getHeight()));
        }

        @Override
        public Coordinate getPosition() {
            return Coordinate2DDouble.zero;//////////////////////////////unused
//            return new Coordinate2D(inputState.getMousePanelX(),inputState.getMousePanelY());
        }

        @Override
        public void doOnAdd() {
            
        }

        @Override
        public void doOnDelete() {
            
        }
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        System.out.println("destroyed " + this);
//    }

    

}

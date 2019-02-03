package test;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.imageio.ImageIO;

import camera.Camera;
import camera.ZoomingCamera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Picture;
import images.StaticPicture;
import sounds.Sound;
import sounds.SoundMedia;
import structure.InputState;
import structure.WorldPanel;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import images.ResourceGetter;
import world.TickListenerManager;
import world.World;
import world.WorldObject;
import worldFunctionality.KeyReact;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldUtility.CameraDragManager;
import worldUtility.CameraZoomScrollManager;
import worldUtility.DebugInputDisplay;
import worldUtility.FixedMultiplierZoomManager;
import worldUtility.Grid;
import worldUtility.WorldButton;
import worldFunctionality.MouseScrolledOnReact;
import worldFunctionality.TickReactAutoAdder;
import worldFunctionality.MainTickReact;

public class TestWorld extends World implements MyListener{
    ////todo: don't scroll when other thing is selected
    
    private ZoomingCamera camera;
    private SquareWorldLogic swl;
    private Grid grid;
    
//    private MediaPlayer mediaPlayer;//keep reference here to avoid garbage collection stopping it
    ///Create a player for a specific media. This is the only way to associate a Media object with a MediaPlayer: once the player is created it cannot be changed.
//    private Sound sound;
//    private Sound sound2;
    public TestWorld() {
        this.swl = new SquareWorldLogic();
        add(swl);//order may matter here
        this.camera = new ZoomingCamera(0.0,0.0,1);
        add(camera);
        
        this.grid = new Grid();
        add(grid);
        add(new DebugInputDisplay(this));
        add(new CameraDragManager(camera,ButtonType.MIDDLE));
        add(new FixedMultiplierZoomManager(camera,1.125));
        
        TickListenerManager<MainTickReact> tlm = new TickListenerManager<MainTickReact>(MainTickReact.class,60){
            @Override
            public void doUniqueAction(TickReactAutoAdder traa){
                MainTickReact ta = (MainTickReact) traa;
                ta.doOnMainTick();
            }
        };
        add(tlm);
        
        
        
//        add(new PathTest());
        
//        add(new ShapeWorldObject(new Ellipse2D.Double(-10, -10, 20, 20)));
//        add(new BlueSnifit(new Coordinate2DDouble(0,0),60));
        
//        add(new WorldButton(new Rectangle2D.Double(-20,70,60,30),new Coordinate2DDouble(-20,70),60,30, ResourceGetter.getPicture("images/JigSawGame/Check.png"),this,"1"));
//        add(new WorldButton(new Rectangle2D.Double(-20,40,60,30),new Coordinate2DDouble(-20,40),60,30, ResourceGetter.getPicture("images/JigSawGame/Check"),this,"2"));
//        add(new WorldButton(new Rectangle2D.Double(-20,10,60,30),new Coordinate2DDouble(-20,10),60,30, ResourceGetter.getPicture("images/JigSawGame/Up Button.jpg"),this,"3"));
//        
//        add(new WorldButton(new Rectangle2D.Double(40,70,60,30),new Coordinate2DDouble(40,70),60,30, ResourceGetter.getPicture("images/JigSawGame/Up Button.jpg"),this,"4"));
//        add(new WorldButton(new Rectangle2D.Double(40,40,60,30),new Coordinate2DDouble(40,40),60,30, ResourceGetter.getPicture("images/JigSawGame/Up Button.jpg"),this,"5"));
//        add(new WorldButton(new Rectangle2D.Double(40,10,60,30),new Coordinate2DDouble(40,10),60,30, ResourceGetter.getPicture("images/JigSawGame/Up Button.jpg"),this,"6"));
//        this.sound = ResourceGetter.getSound("sounds/EBF3.mp3");
//        this.sound2 = ResourceGetter.getSound("sounds/EBF3z.mp3");
        
    }
    
    @Override
    public void eventHappened(MyEvent e) {
//        if(e.getCommand().equals("1")){
//            sound.play();
//        }else if(e.getCommand().equals("2")){
//            sound.pause();
//        }else if(e.getCommand().equals("3")){
//            sound.stopAndReset();
//        }else if(e.getCommand().equals("4")){
//            sound2.play();
//        }else if(e.getCommand().equals("5")){
//            sound2.pause();
//        }else if(e.getCommand().equals("6")){
//            sound2.stopAndReset();
//        }
    }
    
    
    
    
    
    @Override
    public void initialize(WorldPanel wp){
        super.initialize(wp);
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public int getInitialFramePeriod() {
        return 30;
    }

    
    
    private class SquareWorldLogic extends WorldObject implements KeyReact{
        @Override
        public void reactToKey(int keyCode, PressType t) {
            if(keyCode==KeyEvent.VK_F3 && t==KeyReact.PressType.RELEASED){
                setDeBugMode(!isDeBugMode());
                repaint();
            }
//            if(keyCode==KeyEvent.VK_0 && t==KeyReact.PressType.RELEASED){
//                setFramePeriod(0);
//            }
            if(keyCode==KeyEvent.VK_1 && t==KeyReact.PressType.RELEASED){
                setFramePeriod(16);
            }
            if(keyCode==KeyEvent.VK_2 && t==KeyReact.PressType.RELEASED){
                setFramePeriod(1000);
            }
        }

        @Override
        public void doOnAdd() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void doOnDelete() {
            // TODO Auto-generated method stub
            
        }
    }


    

    

}

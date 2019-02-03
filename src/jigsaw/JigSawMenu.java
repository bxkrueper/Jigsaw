package jigsaw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import camera.Camera;
import camera.SimpleCamera;
import coordinate.Coordinate2D;
import coordinate.Coordinate2DDouble;
import images.Animation;
import images.Picture;
import images.ResourceGetter;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import world.World;
import world.WorldObject;
import worldFunctionality.KeyReact;
import worldUtility.DebugGrid;
import worldUtility.DisplayPicture;
import worldUtility.DisplayText;
import worldUtility.FlippedTiledBackground;
import worldUtility.Grid;
import worldUtility.WorldButton;

import javax.swing.JOptionPane;


public class JigSawMenu extends World implements MyListener{
    
    private JigSawFrame jigSawFrame;//the frame
    private SimpleCamera camera;///////////convert to static camera
    
    private DisplayText numberOfPiecesDisplayText;
    private DisplayPicture rotatePicture;
    private DisplayPicture nextPuzzlePicture;
    
    private int numberOfPieces;
    private boolean canRotate;
    private Picture usersImage;//that user chooses
    
    private Picture check;
    private Picture crossOut;
    private Picture defaultNextPictureImage;
    
    private Coordinate2D tlOfSamplePicture;//top left corner position
    private double samplePictureMaxWidth;
    private double samplePictureMaxHeight;
    
    public JigSawMenu(JigSawFrame frame,int numberOfPieces,boolean canRotate,String manualPath) {
        this.jigSawFrame = frame;
        this.numberOfPieces = numberOfPieces;
        this.canRotate = canRotate;
        this.check = ResourceGetter.getPicture("images/JigSawGame/Check.png");
        this.crossOut = ResourceGetter.getPicture("images/JigSawGame/X out.png");
        this.defaultNextPictureImage = ResourceGetter.getPicture("images/JigSawGame/default next image.jpg");
        this.usersImage = defaultNextPictureImage;
        
        this.camera = new SimpleCamera(0.0,0.0);
        add(camera);
        
        add(new FlippedTiledBackground("images/Green Felt 2.jpg",100,25));
        
        
        add(new JigSawDebug());
        
        //number of pieces options
        add(new WorldButton(new Rectangle2D.Double(-20,70,60,30),new Coordinate2DDouble(-20,70),60,30, ResourceGetter.getPicture("images/JigSawGame/Up Button.jpg"),this,"Number Up"));
        add(new WorldButton(new Rectangle2D.Double(-20,40,60,30),new Coordinate2DDouble(-20,40),60,30, ResourceGetter.getPicture("images/JigSawGame/Down Button.jpg"),this,"Number Down"));
        this.numberOfPiecesDisplayText = new DisplayText(Integer.toString(numberOfPieces),new Font("Dialog.plain",Font.BOLD,80),Color.BLACK,70,40);
        add(numberOfPiecesDisplayText);
        add(new DisplayText("Number of",new Font("Dialog.plain",Font.BOLD,40),Color.BLACK,-250,65));
        add(new DisplayText("Pieces",new Font("Dialog.plain",Font.BOLD,40),Color.BLACK,-220,35));
        
        //rotate option
        add(new WorldButton(new Rectangle2D.Double(-70,-25,150,30),new Coordinate2DDouble(-70,-25),150,30, ResourceGetter.getPicture("images/JigSawGame/Rotate Button.jpg"),this,"Rotate Toggle"));
        this.rotatePicture = new DisplayPicture(crossOut,100,-30,50,50);
        add(rotatePicture);
        
        //choose custom picture option
        this.tlOfSamplePicture = new Coordinate2DDouble(90,-55);
        this.samplePictureMaxWidth = 200;
        this.samplePictureMaxHeight = 200;
        add(new WorldButton(new Rectangle2D.Double(-70,-120,150,75),new Coordinate2DDouble(-70,-120),150,75, ResourceGetter.getPicture("images/JigSawGame/Choose Button.jpg"),this,"Choose File"));
        this.nextPuzzlePicture = new DisplayPicture(defaultNextPictureImage,tlOfSamplePicture.getX(),tlOfSamplePicture.getY()-samplePictureMaxHeight,samplePictureMaxWidth,samplePictureMaxHeight);
        add(nextPuzzlePicture);
        
        //start button
        add(new WorldButton(new Rectangle2D.Double(-120,-270,200,110),new Coordinate2DDouble(-120,-270),200,110,ResourceGetter.getPicture("images/JigSawGame/Start Button.jpg"),this,"Start Button"));
    }
    
    public int getNumberOfPieces(){
        return numberOfPieces;
    }
    public boolean getCanRotate(){
        return canRotate;
    }
    public Picture getUsersImage(){
        return usersImage;
    }
    
    //if null, uses default image
    public void setUsersImage(Picture image){
        if(image==null){
            usersImage = defaultNextPictureImage;
        }else{
            usersImage.stop();
            if(usersImage instanceof Animation){
                Animation a = (Animation) usersImage;
                a.setListener(null);
            }
            usersImage = image;
        }
        
        if(usersImage instanceof Animation){
            Animation a = (Animation) usersImage;
            a.setListener(this);
        }
        nextPuzzlePicture.setImage(usersImage);
        usersImage.start();
        adjustSamplePicturePosition();
    }

    @Override
    public void eventHappened(MyEvent e) {
        if(e.getCommand().equals("frame changed")){
            this.repaint();
        }else if(e.getCommand().equals("Start Button")){
            Picture imageToUse = getUsersImage();
            if(imageToUse==defaultNextPictureImage){
                imageToUse=null;
            }
            jigSawFrame.nextPuzzle(getNumberOfPieces(),getCanRotate(),imageToUse);
        }else if(e.getCommand().equals("Number Up")){
            numberOfPieces = nextValidNumberUp(numberOfPieces);
            numberOfPiecesDisplayText.setText(Integer.toString(numberOfPieces));
            this.repaint();
        } else if(e.getCommand().equals("Number Down")){
            numberOfPieces = nextValidNumberDown(numberOfPieces);
            numberOfPiecesDisplayText.setText(Integer.toString(numberOfPieces));
            this.repaint();
        }else if(e.getCommand().equals("Rotate Toggle")){
            canRotate = !canRotate;
            if(canRotate){
                rotatePicture.setImage(check);
            }else{
                rotatePicture.setImage(crossOut);
            }
            this.repaint();
        }else if(e.getCommand().equals("Choose File")){
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("."));
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setFileFilter(new ResourceGetter.PictureFileFilter());
            
            int returnVal = fc.showOpenDialog(jigSawFrame);
            if(returnVal==JFileChooser.APPROVE_OPTION){
                File usersFile = fc.getSelectedFile();
                doWithUsersSelectedFile(usersFile);
            }
        }
    }

    private void doWithUsersSelectedFile(File usersFile) {
        Picture defaultPic = ResourceGetter.getDefaultPicture();
        setUsersImage(ResourceGetter.getPicture(usersFile));
        if(usersImage==defaultPic){
            JOptionPane.showMessageDialog(jigSawFrame,
                    "Could not use this picture format",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE);
            setUsersImage(defaultNextPictureImage);
        }else{
            setUsersImage(usersImage);
        }
    }

    private void adjustSamplePicturePosition(){
        ///////to do
    }
    
    
    
    private int nextValidNumberUp(int oldNumber) {
        return oldNumber + 1;
    }
    
    private int nextValidNumberDown(int oldNumber) {
        if(oldNumber<=2){
            return 2;
        }else{
            return oldNumber-1;
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
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("destroyed " + this);
    }
    
    
    private class JigSawDebug extends WorldObject implements KeyReact{
        @Override
        public void reactToKey(int keyCode, PressType t) {
            if(keyCode==KeyEvent.VK_F3 && t==KeyReact.PressType.RELEASED){
                setDeBugMode(!isDeBugMode());
                repaint();
            }
        }

        @Override
        public void doOnAdd() {
            Grid grid = new DebugGrid();
            add(grid);
            grid.sendToFront();
        }

        @Override
        public void doOnDelete() {
            // TODO Auto-generated method stub
            
        }
    }
}

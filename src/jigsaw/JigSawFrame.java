package jigsaw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import images.Picture;
import images.ResourceGetter;
import myAlgs.MyAlgs;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import myAlgs.MyMath;
import resourceMethods.ResourceMethods;
import structure.WorldPanel;


public class JigSawFrame extends JFrame implements ActionListener, MyListener{

private static JigSawFrame instance;

    private WorldPanel worldPanel;
    
    private List<File> pictureFiles;
    private int pictureIndex;
    private Picture image;
    
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem restartItem;
    private JMenuItem toMenuItem;
    private JMenuItem showPicItem;
    
    private JigSawMenu menuWorld;
    
    private JigSawFrame(){
        setTitle("Jig Saw Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650,650));
        setIconImage(new ImageIcon("images/puzzle piece.png").getImage());
        
        this.pictureIndex = 0;
        this.pictureFiles = new ArrayList<>();
        File[] pictureArray = ResourceMethods.getAllFiles("images/JigSawGame/JigSawPool",new ResourceGetter.PictureFilenameFilter());
        MyAlgs.shuffleArray(pictureArray);
        for(File f:pictureArray){
            pictureFiles.add(f);
        }
        
        this.menuWorld = new JigSawMenu(this,9,false,null);
        this.worldPanel = new WorldPanel(menuWorld);
        
        add(worldPanel);
        
        addMenu();
        
        pack();
        setLocationRelativeTo(null);  
        setVisible(true);
    }
    

    private void addMenu() {
        
        this.menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        
        this.menu = new JMenu("Options");
        menuBar.add(menu);
        this.toMenuItem = new JMenuItem("Menu");
        toMenuItem.setActionCommand("Menu");
        toMenuItem.addActionListener(this);
        
        this.restartItem = new JMenuItem("Restart");
        restartItem.setActionCommand("Restart");
        restartItem.addActionListener(this);
        this.showPicItem = new JMenuItem("Show Picture");
        showPicItem.addActionListener(this);
        
        this.setJMenuBar(menuBar);
    }

    public static JigSawFrame getInstance(){
        if(instance==null){
            instance = new JigSawFrame();
        }
        
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals("Menu")){
            toMenu();
        }else if(ae.getActionCommand().equals("Restart")){
            restart();
        }else if(ae.getActionCommand().equals("Next") || ae.getActionCommand().equals("Skip")){
            nextPuzzle(menuWorld.getNumberOfPieces(),menuWorld.getCanRotate(),menuWorld.getUsersImage());
        }else if(ae.getActionCommand().equals("Show Picture")){
            showPicture();
        }
    }
    
    


    @Override
    public void eventHappened(MyEvent e) {
        if(e.getCommand().equals("Puzzle Finished")){
            
        }else if(e.getCommand().equals("Puzzle Finished Button Pressed")){
            toMenu();
        }
    }

    private void showPicture() {
        if(image==null){
            return;
        }
        JFrame newFrame = new JFrame();
        newFrame.setTitle("Jig Saw Puzzle");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        double[] widthAndHeight = getConstrainedWidthandHeight(image.getWidth(),image.getHeight(),500);
        newFrame.setPreferredSize(new Dimension((int)widthAndHeight[0],(int)widthAndHeight[1]));
//        newFrame.setIconImage(new ImageIcon("images/puzzle piece.png").getImage());
        JPanel panel = new ImagePanel(image);
        newFrame.add(panel);
        
        newFrame.pack();
        newFrame.setLocationRelativeTo(null);  
        newFrame.setVisible(true);
    }
    
    private double[] getConstrainedWidthandHeight(double width, double height, double max){
        if(width<=max&&height<max){
            return new double[]{width,height};
        }
        
        double widthRatio = width/max;
        double heightRatio = height/max;
        
        if(widthRatio>heightRatio){
            double div = width/max;
            return new double[]{max,height/div};
        }else{
            double div = height/max;
            return new double[]{width/div,max};
        }
    }


    private void toMenu() {
        worldPanel.setWorld(menuWorld);
        menuWorld.setUsersImage(null);
        menu.remove(restartItem);
        menu.remove(toMenuItem);
        menu.remove(showPicItem);
        repaint();
    }
    
    public void nextPuzzle(int numberOfPieces,boolean canRotate, Picture usersImage) {
        menu.add(restartItem);
        menu.add(toMenuItem);
        menu.add(showPicItem);
        
        if(usersImage==null){
            pictureIndex = (pictureIndex+1)%pictureFiles.size();
            image = ResourceGetter.getPicture(pictureFiles.get(pictureIndex));
        }else{
            image = usersImage;
        }
        
        int[] rowsAndColumns = getRowsAndColumns(numberOfPieces,image.getWidth(),image.getHeight());
        int rows = rowsAndColumns[0];
        int columns = rowsAndColumns[1];
        
        JigSawWorld world = new JigSawWorld(this);
        world.newPuzzle(image,rows,columns,canRotate);
        worldPanel.setWorld(world);
        
        
        
        repaint();
    }
    
    //answer[0] is rows, answer[1] is columns
    public int[] getRowsAndColumns(int numberOfPieces,int widthOfPicture,int heightOfPicture){
        int[] answer = new int[2];
        
        
        int biggestPic = Math.max(widthOfPicture, heightOfPicture);
        int smallestPic = Math.min(widthOfPicture, heightOfPicture);
        double picRatio = biggestPic/(double)smallestPic;
        double closestRatio = Double.POSITIVE_INFINITY;
        long bestLowNumber = 0;
        List<Long> factorList = MyMath.findAllFactors(numberOfPieces);
        int sqrtInt = (int) Math.sqrt(numberOfPieces);
        
        for(long l:factorList){
            if(l>sqrtInt){
                break;//only need first part of list
            }
            double ratio = (numberOfPieces/(double)l)/l;
            if(Math.abs(ratio-picRatio)<Math.abs(closestRatio-picRatio)){
                closestRatio = ratio;
                bestLowNumber = l;
            }
        }
        
        int rows,columns;
        if(widthOfPicture<heightOfPicture){
            rows = (int) (numberOfPieces/bestLowNumber);
            columns = (int) bestLowNumber;
        }else{
            rows = (int) bestLowNumber;
            columns = (int) (numberOfPieces/bestLowNumber);
        }
        
        
        
        
        
        answer[0] = rows;
        answer[1] = columns;
        return answer;
    }


    private void restart() {
        image = ResourceGetter.getPicture(pictureFiles.get(pictureIndex));
        
        JigSawWorld world = new JigSawWorld(this);
        int[] rowsAndColumns = getRowsAndColumns(menuWorld.getNumberOfPieces(),image.getWidth(),image.getHeight());
        int rows = rowsAndColumns[0];
        int columns = rowsAndColumns[1];
        world.newPuzzle(image,rows,columns,menuWorld.getCanRotate());
        worldPanel.setWorld(world);
        
        repaint();
    }
    
    
    
    
    
    private class ImagePanel extends JPanel{
        
        private Picture thisImage;
        
        public ImagePanel(Picture thisImage){
            this.thisImage = thisImage;
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            boolean sizeToWidth = (((double)getHeight()/getWidth())>((double)thisImage.getHeight()/thisImage.getWidth()));
            if(sizeToWidth){
                thisImage.draw(g, 0, 0, getWidth(), (int) (thisImage.getHeight()*(getWidth()/(double)thisImage.getWidth())));
            }else{
                thisImage.draw(g, 0, 0, (int) (thisImage.getWidth()*(getHeight()/(double)thisImage.getHeight())), getHeight());
            }
            
        }
        @Override 
        public Dimension getPreferredSize(){
            return new Dimension(thisImage.getWidth(),thisImage.getHeight());
        }
    }





    
}

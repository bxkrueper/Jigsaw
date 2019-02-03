package test;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import structure.WorldPanel;
import myAlgs.MyAlgs;
import myAlgs.MyEvent;
import images.ResourceGetter;
import world.World;

public class TestFrame extends JFrame{
    private WorldPanel worldPanel;
    private static TestFrame instance;
    
    private TestFrame(){
        setTitle("Test Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650,650));
//        setIconImage(new ImageIcon("images/puzzle piece.png").getImage());
        
        
        
        World testWorld = new TestWorld();
        this.worldPanel = new WorldPanel(testWorld);
        
        add(worldPanel);
        
        
        pack();
        setLocationRelativeTo(null);  
        setVisible(true);
    }
    
    public static TestFrame getInstance(){
        if(instance==null){
            instance = new TestFrame();
        }
        
        return instance;
    }
}

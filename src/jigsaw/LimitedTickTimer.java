package jigsaw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import myAlgs.MyEvent;
import myAlgs.MyListener;

//turns itself off after the given amount of actions
//sends one last event with string "done" after the last action
public class LimitedTickTimer implements ActionListener{
    private Timer timer;
    private int tickNumber;
    private int maxTicks;
    private MyListener myListener;
    private String myCommand;
    
    public LimitedTickTimer(int tickPeriod, int numberOfTicks,MyListener ml, String actionCommand){
        this.tickNumber = 0;
        this.maxTicks = numberOfTicks;
        this.myListener = ml;
        this.myCommand = actionCommand;
        this.timer = new Timer(tickPeriod,this);
        
    }
    
    public void start(){
        if(tickNumber<maxTicks){
            timer.start();
        }
    }
    
    public void startOver(){
        tickNumber = 0;
        timer.start();
    }
    
    public void pause(){
        timer.stop();
    }
    
    
    public void restart(){
        tickNumber = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tickNumber++;
        myListener.eventHappened(new MyEvent(this,myCommand,null));
        if(tickNumber>=maxTicks){
            timer.stop();
            myListener.eventHappened(new MyEvent(this,"done",null));
        }
    }
}

package world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import worldFunctionality.TickReactAutoAdder;

public abstract class TickListenerManager<T extends TickReactAutoAdder> implements ActionListener {

    private Class<T> classType;
    private Timer tickTimer;
    private List<T> tickList;
    
    public TickListenerManager(Class<T> classType, int period){
        this.classType = classType;
        tickList = new ArrayList<>();
        
        tickTimer = new Timer(period,this);
    }
    
    
    public String getTickName(){
        return classType.getName();
    }
    
    //adds the object if it is the right class
    public void tryAdd(TickReactAutoAdder tickReactAutoAdder) {
        if(classType.isAssignableFrom(tickReactAutoAdder.getClass())){
            tickList.add((T) tickReactAutoAdder);
        }
        
    }
    
    public void remove(TickReactAutoAdder tickReactAutoAdder) {
        if(classType.isAssignableFrom(tickReactAutoAdder.getClass())){
            tickList.remove(tickReactAutoAdder);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        for(TickReactAutoAdder t:tickList){
            doUniqueAction(t);
        }
    }
    
    public void startTimer(){
        tickTimer.start();
    }
    
    public void stopTimer(){
        tickTimer.stop();
    }

    public int size() {
        return tickList.size();
    }
    
    public abstract void doUniqueAction(TickReactAutoAdder traa);
}

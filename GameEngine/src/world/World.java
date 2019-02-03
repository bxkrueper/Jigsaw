//recieves inputs from a world panel and grants access to it
//hooks up the input
package world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Timer;

import camera.Camera;
import coordinate.Coordinate2DDouble;
import structure.InputState;
import structure.WorldPanel;
import structure.InputState.InputStateReader;
import worldFunctionality.WorldDrawable;
import worldFunctionality.KeyReact;
import worldFunctionality.MouseButtonReact;
import worldFunctionality.MouseMovedReact;
import worldFunctionality.MouseTargetable;
import worldFunctionality.PanelDrawable;
import worldFunctionality.ScreenReact;
import worldFunctionality.MainTickReact;
import worldFunctionality.TickReactAutoAdder;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;

public abstract class World{
    private WorldPanel worldPanel;
    
    private WorldPainter worldPainter;
    
    private List<WorldDrawable> worldDrawList;
    private List<PanelDrawable> panelDrawList;
    private List<KeyReact> keyReactList;
    private List<MouseButtonReact> mouseButtonReactList;////change with hitboxes?
    private List<MouseMovedReact> mouseMovedReactList;////take them out main list and have concrete class determine which one got clicked
    private List<ScreenReact> screenReactList;
    private MouseSelectManager mouseSelectManager;
    private Map<String,TickListenerManager<? extends TickReactAutoAdder>> tlmMap;
    
    private Timer frameTimer;
    
    private boolean deBugMode;
    
    
    public World(){
        this.worldDrawList = new ArrayList<WorldDrawable>();
        this.panelDrawList = new ArrayList<PanelDrawable>();
        this.keyReactList = new LinkedList<KeyReact>();
        this.mouseButtonReactList = new LinkedList<MouseButtonReact>();
        this.mouseMovedReactList = new LinkedList<MouseMovedReact>();
        this.screenReactList = new LinkedList<ScreenReact>();
        
        this.mouseSelectManager = new MouseSelectManager();
        
        this.tlmMap = new TreeMap<>();
        
        this.frameTimer = new Timer(getInitialFramePeriod(),new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                worldPanel.repaint();
            }
        });
        
        this.deBugMode = false;
    }
    
    //post constructor stuff. called by WorldPanel after the world recieves a panel
    public void initialize(WorldPanel wp){
        this.worldPanel = wp;
        
        //Initialize world painter with new panel
        if(worldPainter==null){
            this.worldPainter = new WorldPainter(getCamera(),worldPanel.getWidth(),worldPanel.getHeight());
        }else{
            this.worldPainter.setPanelDimensions(worldPanel.getWidth(), worldPanel.getHeight());
        }
        
        //start frame timer
        if(frameTimer.getDelay()>0){
            frameTimer.start();
        }
        
        //start all tick timers
        for(TickListenerManager<? extends TickReactAutoAdder> tlm:tlmMap.values()){
            tlm.startTimer();
        }
    }
    
    //called by WorldPanel
    ////////should panel be taken away? stop at input methods instead?
    public void deinitialize(){
        this.worldPanel = null;//////////////is there any harm in keeping the world reference? it will give the last few methods time to finish
        frameTimer.stop();
        
        //stop timers
        deinitializeTimers();
    }

    //override to stop timers from being stopped on de-initialization
    protected void deinitializeTimers() {
        for(TickListenerManager<? extends TickReactAutoAdder> tlm:tlmMap.values()){
            tlm.stopTimer();
        }
    }
    
    
    public void setWorldPanel(WorldPanel wp){
        this.worldPanel = wp;
    }
    public WorldPanel getWorldPanel(){
        return worldPanel;
    }
    
    public abstract Camera getCamera();
    
    //sets the starting frame rate. return 0 to not auto refresh frames
    public abstract int getInitialFramePeriod();
    
    public void setFramePeriod(int periodMills){
        if(periodMills<=0){
            frameTimer.setDelay(0);
            frameTimer.stop();
            return;
        }else{
            boolean wasStopped = frameTimer.getDelay()<=0;
            frameTimer.setDelay(periodMills);
            if(wasStopped){
                frameTimer.start();
            }
        }
    }
    
    public final void repaint(){
        if(worldPanel!=null){
            worldPanel.repaint();
        }
    }
    
    public <T extends TickReactAutoAdder> void add(TickListenerManager<T> tlm) {
        tlmMap.put(tlm.getTickName(), tlm);
    }
    
    public <T extends TickReactAutoAdder> void remove(TickListenerManager<T> tlm) {
        tlmMap.remove(tlm.getTickName());
    }
    
    
    //adds the object to any lists that it fits into. Override to add to more lists
    public void add(WorldObject o){
        if(o==null){
            return;
        }
        
        o.setWorld(this);
        
        if(o instanceof WorldDrawable){
            worldDrawList.add((WorldDrawable) o);
        }
        if(o instanceof PanelDrawable){
            panelDrawList.add((PanelDrawable) o);
        }
        if(o instanceof KeyReact){
            keyReactList.add((KeyReact) o);
        }
        if(o instanceof MouseButtonReact){
            mouseButtonReactList.add((MouseButtonReact) o);
        }
        if(o instanceof MouseMovedReact){
            mouseMovedReactList.add((MouseMovedReact) o);
        }
        if(o instanceof ScreenReact){
            screenReactList.add((ScreenReact) o);
        }
        if(o instanceof MouseTargetable){
            mouseSelectManager.addToTargetable((MouseTargetable) o);
        }
        if(o instanceof TickReactAutoAdder){
            TickReactAutoAdder traa = (TickReactAutoAdder) o;
            for(TickListenerManager<? extends TickReactAutoAdder> tlm:tlmMap.values()){
                tlm.tryAdd(traa);
            }
        }
        o.doOnAdd();
    }
    
    //see add
    ////////////////////////////////////////////////////memory leaks?
    public void remove(WorldObject o){
        if(o==null){
            return;
        }
        
        if(o instanceof WorldDrawable){
            worldDrawList.remove((WorldDrawable) o);
        }
        if(o instanceof PanelDrawable){
            panelDrawList.remove((PanelDrawable) o);
        }
        if(o instanceof KeyReact){
            keyReactList.remove((KeyReact) o);
        }
        if(o instanceof MouseButtonReact){
            mouseButtonReactList.remove((MouseButtonReact) o);
        }
        if(o instanceof MouseMovedReact){
            mouseMovedReactList.remove((MouseMovedReact) o);
        }
        if(o instanceof ScreenReact){
            screenReactList.remove((ScreenReact) o);
        }
        if(o instanceof MouseTargetable){
            mouseSelectManager.removeFromTargetable((MouseTargetable) o);
        }
        if(o instanceof TickReactAutoAdder){
            TickReactAutoAdder traa = (TickReactAutoAdder) o;
            for(TickListenerManager<? extends TickReactAutoAdder> tlm:tlmMap.values()){
                tlm.remove(traa);
            }
        }
        o.doOnDelete();
    }
    
    
    public boolean isDeBugMode() {
        return deBugMode;
    }

    public void setDeBugMode(boolean deBugMode) {
        this.deBugMode = deBugMode;
    }
    
    public InputStateReader getInputStateReader(){
        return worldPanel.getInputStateReader();
    }
    
    
    
    public final void draw(Graphics g){
        worldPainter.setGraphics(g);
        Graphics2D g2D = (Graphics2D) g;
        draw(worldPainter);
        
        //panel   ///////////have separate class like PanelPainter?
        if(deBugMode){
            for(PanelDrawable pd:panelDrawList){
                pd.draw(g2D);
                pd.drawDebug(g2D);
            }
        }else{
            for(PanelDrawable pd:panelDrawList){
                pd.draw(g2D);
            }
        }
    }
    
    //is drawable order sync fixed? make sure to remember that with other features using these lists
    public void draw(WorldPainter wp) {
        //draw everything: stuff at end is drawn last
        
        if(deBugMode){
            for(WorldDrawable d:worldDrawList){
                d.draw(wp);
                wp.finishedDrawingObject();
                d.drawDebug(wp);
                wp.finishedDrawingObject();
            }
        }else{
            for(WorldDrawable d:worldDrawList){
                d.draw(wp);
                wp.finishedDrawingObject();
            }
        }
    }
    
//    public abstract void drawDeBug(Graphics2D g);
    
    //puts the object at the end of the draw list and the start of the possible target list in the mouse select manager
    public void sendToFront(WorldObject wo){
        if(wo instanceof WorldDrawable){
            WorldDrawable d = (WorldDrawable) wo;
            worldDrawList.remove(d);
            worldDrawList.add(d);
        }
        
        if(wo instanceof MouseTargetable){
            MouseTargetable mt = (MouseTargetable) wo;
            mouseSelectManager.sendToFront(mt);
        }
    }
    
    //puts the object at the start of the draw list and the end of the possible target list in the mouse select manager
    public void sendToBack(WorldObject wo){
        if(wo instanceof WorldDrawable){
            WorldDrawable d = (WorldDrawable) wo;
            worldDrawList.remove(d);
            worldDrawList.add(0, d);
        }
        
        if(wo instanceof MouseTargetable){
            MouseTargetable mt = (MouseTargetable) wo;
            mouseSelectManager.sendToBack(mt);
        }
    }
    
    

    public double getMouseWorldY() {
        double mouseY = getCamera().panelYToWorldY(getInputStateReader().getMousePanelY(),getWorldPanel().getHeight());
        return mouseY;
    }
    public double getMouseWorldX() {
        double mouseX = getCamera().panelXToWorldX(getInputStateReader().getMousePanelX(),getWorldPanel().getWidth());
        return mouseX;
    }
    
    
    public void notifyMouseMoved(int x, int y){
        double mouseX = getMouseWorldX();
        double mouseY = getMouseWorldY();
        for(MouseMovedReact mmr: mouseMovedReactList){
            mmr.reactToMouseMoved(mouseX, mouseY);
        }
        
        mouseSelectManager.notifyMouseMoved(new Coordinate2DDouble(getMouseWorldX(),getMouseWorldY()));

    }

    public void notifyMouseEntered(int x, int y){
        double mouseX = getMouseWorldX();
        double mouseY = getMouseWorldY();
        
        for(MouseMovedReact mmr: mouseMovedReactList){
            mmr.reactToMouseEntered(mouseX,mouseY);
        }
        
        mouseSelectManager.notifyMouseEntered(new Coordinate2DDouble(mouseX,mouseY));
    }

    public void notifyMouseExited(int x, int y){
        double mouseX = getMouseWorldX();
        double mouseY = getMouseWorldY();
        
        for(MouseMovedReact mmr: mouseMovedReactList){
            mmr.reactToMouseExited(mouseX,mouseY);
        }
        
        mouseSelectManager.notifyMouseExited(getMouseWorldX(),getMouseWorldY());
    }

    public void notifyMouseButton(ButtonType bt,ButtonPressType bpt, int x, int y){
        double mouseXWorld = getMouseWorldX();
        double mouseYWorld = getMouseWorldY();
        
        for(MouseButtonReact mmr: mouseButtonReactList){
            mmr.reactToMouseButton(bt,bpt,mouseXWorld, mouseYWorld);
        }
        
        mouseSelectManager.notifyMouseButtonPressed(bt,bpt,mouseXWorld,mouseYWorld);
    }
    
    public void notifyKeyPressed(int keyCode){
        for(KeyReact k:keyReactList){
            k.reactToKey(keyCode,KeyReact.PressType.PRESSED);
        }
        
    }

    public void notifyKeyReleased(int keyCode){
        for(KeyReact k:keyReactList){
            k.reactToKey(keyCode,KeyReact.PressType.RELEASED);
        }
        
    }
    
//    public final void notifyKeyTyped(int keyCode){
//        for(KeyReact k:keyReactList){
//            k.reactToKey(keyCode,KeyReact.PressType.TYPED);
//        }
//        
//    }

    public void notifyPanelResized(int width, int height){
        worldPainter.setPanelDimensions(worldPanel.getWidth(),worldPanel.getHeight());
        for(ScreenReact sr:screenReactList){
            sr.notifyComponentResized(width, height);
        }
    }
    
    public void notifyPanelVisible(){
        for(ScreenReact sr:screenReactList){
            sr.notifyComponentVisible();
        }
    }
    
    public void notifyPanelMoved(){
        for(ScreenReact sr:screenReactList){
            sr.notifyComponentMoved();
        }
    }
    
    public void notifyPanelHidden(){
        for(ScreenReact sr:screenReactList){
            sr.notifyComponentHidden();
        }
    }

}

package structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import coordinate.Coordinate2DInteger;
import structure.InputState.InputStateReader;

public class InputState{
    private Coordinate2DInteger mousePanelPosition;
    private Coordinate2DInteger lastLeftDown;
    private Coordinate2DInteger lastWheelDown;
    private Coordinate2DInteger lastRightDown;
    private boolean mouseLeftDown;
    private boolean mouseRightDown;
    private boolean mouseWheelDown;
    private boolean mouseInPanel;
    private List<Integer> keysDown;
    
    private InputStateReader inputStateReader;//only has get methods. Only intractable panel should have access to the set methods
    
    public InputState(){
        this.mousePanelPosition = new Coordinate2DInteger(0,0);
        this.lastLeftDown = new Coordinate2DInteger(0,0);
        this.lastWheelDown = new Coordinate2DInteger(0,0);
        this.lastRightDown = new Coordinate2DInteger(0,0);
        
        this.mouseLeftDown = false;
        this.mouseRightDown = false;
        this.mouseWheelDown = false;
        this.mouseInPanel = true;
        
        this.keysDown = new ArrayList<>();
        
        this.inputStateReader = new InputStateReader();
    }
    
    public InputStateReader getReader() {
        return inputStateReader;
    }

    protected void setMousePanel(int x,int y){
        mousePanelPosition.set(x,y);
    }
    protected void setMousePanelX(int x){
        mousePanelPosition.setX(x);
    }
    protected void setMousePanelY(int y){
        mousePanelPosition.setY(y);
    }
    
    protected void setLastLeftDown(int x,int y){
        lastLeftDown.set(x,y);
    }
    protected void setLastLeftDownX(int x){
        lastLeftDown.setX(x);
    }
    protected void setLastLeftDownY(int y){
        lastLeftDown.setY(y);
    }
    
    protected void setLastWheelDown(int x,int y){
        lastWheelDown.set(x,y);
    }
    protected void setLastWheelDownX(int x){
        lastWheelDown.setX(x);
    }
    protected void setLastWheelDownY(int y){
        lastWheelDown.setY(y);
    }
    
    protected void setLastRightDown(int x,int y){
        lastRightDown.set(x,y);
    }
    protected void setLastRightDownX(int x){
        lastRightDown.setX(x);
    }
    protected void setLastRightDownY(int y){
        lastRightDown.setY(y);
    }
    
    
    
    
    protected void setMouseLeftDown(boolean b){
        mouseLeftDown = b;
    }
    
    protected void setMouseRightDown(boolean b){
        mouseRightDown = b;
    }
    
    protected void setMouseWheelDown(boolean b){
        mouseWheelDown = b;
    }
    
    protected void setMouseInPanel(boolean b){
        mouseInPanel = b;
    }
    
    protected void keyDown(int keyCode){
        if(!keysDown.contains(keyCode)){
            keysDown.add(keyCode);
        }
    }
    
    protected void keyUp(int keyCode){
        keysDown.remove(new Integer(keyCode));
    }

    
    
    
    
    public class InputStateReader{
        public Coordinate2DInteger getMousePanelPosition(){
            return (Coordinate2DInteger) mousePanelPosition.copy();
        }
        public int getMousePanelX() {
            return mousePanelPosition.getXInt();
        }
        public int getMousePanelY() {
            return mousePanelPosition.getYInt();
        }
        
        public int getLastLeftDownX() {
            return lastLeftDown.getXInt();
        }
        public int getLastLeftDownY() {
            return lastLeftDown.getYInt();
        }
        
        public int getLastWheelDownX() {
            return lastWheelDown.getXInt();
        }
        public int getLastWheelDownY() {
            return lastWheelDown.getYInt();
        }
        
        public int getLastRightDownX() {
            return lastRightDown.getXInt();
        }
        public int getLastRightDownY() {
            return lastRightDown.getYInt();
        }

        public boolean isMouseLeftDown() {
            return mouseLeftDown;
        }

        public boolean isMouseRightDown() {
            return mouseRightDown;
        }

        public boolean isMouseWheelDown() {
            return mouseWheelDown;
        }
        
        public boolean isAnyMouseButtonDown(){
            return mouseLeftDown || mouseRightDown || mouseWheelDown;
        }
        
        public boolean isMouseInPanel() {
            return mouseInPanel;
        }
        
        public boolean isKeyDown(int keyCode){
            return keysDown.contains(keyCode);
        }
        
        
        
        
        
        
        //draws a crude mouse on the screen showing which buttons are pressed.
        //also showed which keys are pressed down on the lower part of the mouse
        public void draw(Graphics2D g, int xCenterPanel, int yCenterPanel) {//////not center!!!!
            g.setColor(Color.GRAY);
            g.fillOval(xCenterPanel-35, yCenterPanel-60, 35*2, 60*2);
            
            if(mouseLeftDown){
                g.setColor(Color.BLUE);
                g.fillOval(xCenterPanel-20-10, yCenterPanel-26-20, 10*2, 20*2);
            }else{
                g.setColor(Color.DARK_GRAY);
                g.fillOval(xCenterPanel-20-10, yCenterPanel-26-20, 10*2, 20*2);
            }
            
            if(mouseWheelDown){
                g.setColor(Color.BLUE);
                g.fillRect(xCenterPanel-5, yCenterPanel-44, 10, 20);
            }else{
                g.setColor(Color.DARK_GRAY);
                g.fillRect(xCenterPanel-5, yCenterPanel-44, 10, 20);
            }
            
            if(mouseRightDown){
                g.setColor(Color.BLUE);
                g.fillOval(xCenterPanel+20-10, yCenterPanel-26-20, 10*2, 20*2);
            }else{
                g.setColor(Color.DARK_GRAY);
                g.fillOval(xCenterPanel+20-10, yCenterPanel-26-20, 10*2, 20*2);
            }
            
            
            
            int drawPositionY = yCenterPanel;
            g.setColor(Color.BLACK);
            for(int keyCode:keysDown){
                g.drawString(KeyEvent.getKeyText(keyCode), xCenterPanel-7, drawPositionY);
                drawPositionY+=10;
            }
            
        }
    }



    

}

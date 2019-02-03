//listens for, gives notice of, and remembers the status of basic inputs to the panel

package structure;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import structure.InputState.InputStateReader;

public abstract class InteractablePanel extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,ComponentListener{

    private InputState inputState;
    private InputStateReader inputStateReader;
    
    public InteractablePanel(){
        this.inputState = new InputState();
        this.inputStateReader = inputState.getReader();
        
        //add Listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
        addKeyListener(this);
        
    }
    
    public InputStateReader getInputStateReader(){
        return inputStateReader;
    }
    
    private void updateMousePosition(MouseEvent e) {
        inputState.setMousePanelX(e.getX());
        inputState.setMousePanelY(e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocusInWindow();
        inputState.setMouseInPanel(true);
        updateMousePosition(e);
        inputState.setMouseLeftDown(false);
        inputState.setMouseRightDown(false);
        inputState.setMouseWheelDown(false);
        notifyMouseEntered(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        inputState.setMouseInPanel(false);
        updateMousePosition(e);
        inputState.setMouseLeftDown(false);
        inputState.setMouseRightDown(false);
        inputState.setMouseWheelDown(false);
        notifyMouseExited(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
        if(e.getButton()==1){
            inputState.setMouseLeftDown(true);
            inputState.setLastLeftDown(e.getX(), e.getY());
            notifyMouseLeftPressed(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else if(e.getButton()==2){
            inputState.setMouseWheelDown(true);
            inputState.setLastWheelDown(e.getX(), e.getY());
            notifyMouseWheelPressed(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else if(e.getButton()==3){
            inputState.setMouseRightDown(true);
            inputState.setLastRightDown(e.getX(), e.getY());
            notifyMouseRightPressed(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else{
            System.out.println("unknown mouse button pressed");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()==1){
            inputState.setMouseLeftDown(false);
            notifyMouseLeftReleased(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else if(e.getButton()==2){
            inputState.setMouseWheelDown(false);
            notifyMouseWheelReleased(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else if(e.getButton()==3){
            inputState.setMouseRightDown(false);
            notifyMouseRightReleased(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }else{
            System.out.println("unknown mouse button released");
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {//activates if any of the buttons are pressed
        updateMousePosition(e);
        notifyMouseMoved(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {//does not fire when dragged
        updateMousePosition(e);
        notifyMouseMoved(inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation()==0){
            System.out.println("scroll 0?");
        }else {
            notifyScroll(e.getWheelRotation()>0,inputStateReader.getMousePanelX(),inputStateReader.getMousePanelY());
        }
    }
    
    ////////////////////////////////////
    //todo: Better to avoid using KeyListeners with JPanel,
    //use KeyBindings instead. JPanel cannot gain focus so cannot interact with
    //KeyEvents. Using KeyBindings, you can map an Action to a KeyStroke 
    //even when a component doesn't have focus
    
    
  //Invoked when a key has been pressed. https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
    inputState.keyDown(e.getKeyCode());
    notifyKeyPressed(e.getKeyCode());
        
    }
    //Invoked when a key has been released.
    @Override
    public void keyReleased(KeyEvent e) {
        inputState.keyUp(e.getKeyCode());
        notifyKeyReleased(e.getKeyCode());
    }
    //This event occurs when a key press is followed by a key release
    @Override
    public void keyTyped(KeyEvent e) {
//        notifyKeyTyped(e.getKeyCode());/////////////////don't need now?
    }
    
    @Override
    public void componentHidden(ComponentEvent arg0) {
        notifyComponentHidden();
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
        notifyComponentMoved();
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        notifyComponentResized(getWidth(),getHeight());
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
        notifyComponentVisible();
    }

    protected abstract void notifyMouseEntered(int x, int y);

    protected abstract void notifyMouseExited(int x, int y);

    protected abstract void notifyMouseLeftPressed(int x, int y);
    
    protected abstract void notifyMouseWheelPressed(int x, int y);
    
    protected abstract void notifyMouseRightPressed(int x, int y);

    protected abstract void notifyMouseLeftReleased(int x, int y);
    
    protected abstract void notifyMouseWheelReleased(int x, int y);
    
    protected abstract void notifyMouseRightReleased(int x, int y);

    protected abstract void notifyMouseMoved(int x, int y);

    protected abstract void notifyScroll(boolean b, int x, int y);
    
    protected abstract void notifyKeyPressed(int keyCode);
    
    protected abstract void notifyKeyReleased(int keyCode);
    
//    protected abstract void notifyKeyTyped(int keyCode);

    protected abstract void notifyComponentHidden();

    protected abstract void notifyComponentVisible();

    protected abstract void notifyComponentMoved();

    protected abstract void notifyComponentResized(int width, int height);

    
}

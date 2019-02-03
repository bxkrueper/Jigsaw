package structure;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public abstract class TogglePanel extends JPanel implements MouseListener{

    private boolean active;
    
    public TogglePanel(boolean active){
        this.active = active;
        this.addMouseListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent arg0) {
        if(acceptActivate(arg0)){
            active = !active;
            doOnToggle();
        }
    }
    
    
    
    //does not do anything if setting it to the same state
    public void setActive(boolean b){
        if(active!=b){
            active = b;
            doOnToggle();
        }
    }
    
    public boolean isActive(){
        return active;
    }

    //here in case you want to do something like only activating if a part of the panel
    //is clicked on or letting the panel be disabled. For normal functionallity, just return true
    public abstract boolean acceptActivate(MouseEvent arg0);
    
    //update internal things like display and background color when the state is changed
    public abstract void doOnToggle();
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }
    @Override
    public void mouseExited(MouseEvent arg0) {
    }
    @Override
    public void mousePressed(MouseEvent arg0) {
    }
    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}

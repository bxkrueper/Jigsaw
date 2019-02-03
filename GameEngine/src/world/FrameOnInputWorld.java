package world;

import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;

public abstract class FrameOnInputWorld extends World{
    
    
    public void notifyMouseMoved(int x, int y){
        super.notifyMouseMoved(x,y);
        repaint();
    }

    public void notifyMouseEntered(int x, int y){
        super.notifyMouseEntered(x, y);
        repaint();
    }

    public void notifyMouseExited(int x, int y){
        super.notifyMouseExited(x, y);
        repaint();
    }

    public void notifyMouseButton(ButtonType bt,ButtonPressType bpt, int x, int y){
        super.notifyMouseButton(bt, bpt, x, y);
        repaint();
    }
    
    public void notifyKeyPressed(int keyCode){
        super.notifyKeyPressed(keyCode);
        repaint();
    }

    public void notifyKeyReleased(int keyCode){
        super.notifyKeyReleased(keyCode);
        repaint();
    }
    
    public void notifyPanelVisible(){
        super.notifyPanelVisible();
        repaint();
    }
    
    //useless?
    public void notifyPanelMoved(){
        super.notifyPanelMoved();
        repaint();
//        System.out.println("panel moved");
    }
}

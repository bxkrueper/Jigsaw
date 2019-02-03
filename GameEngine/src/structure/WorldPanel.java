//passes on notifications to the world in the panel
//can also tell the world to start painting

package structure;

import java.awt.Graphics;

import javax.swing.JMenuBar;

import world.World;
import worldFunctionality.MouseButtonReact.ButtonPressType;
import worldFunctionality.MouseButtonReact.ButtonType;

public class WorldPanel extends InteractablePanel{
    
    private World world;
    
    public WorldPanel(World w){
        setLayout(null);
        setWorld(w);
    }
    
    public void setWorld(World w){
        if(world!=null){
            world.deinitialize();
        }
        this.world = w;
        world.initialize(this);////////////////what if it was already initilized before?
        repaint();//added in May 2018
    }
    
    public World getWorld(){
        return world;
    }

    @Override
    protected void notifyMouseEntered(int x, int y) {
        world.notifyMouseEntered(x,y);
    }

    @Override
    protected void notifyMouseExited(int x,int y) {
        world.notifyMouseExited(x,y);
    }

    @Override
    protected void notifyMouseLeftPressed(int x, int y) {
        world.notifyMouseButton(ButtonType.LEFT, ButtonPressType.DOWN, x, y);
    }
    @Override
    protected void notifyMouseWheelPressed(int x, int y) {
        world.notifyMouseButton(ButtonType.MIDDLE, ButtonPressType.DOWN, x, y);
    }
    @Override
    protected void notifyMouseRightPressed(int x, int y) {
        world.notifyMouseButton(ButtonType.RIGHT, ButtonPressType.DOWN, x, y);
    }

    @Override
    protected void notifyMouseLeftReleased(int x, int y) {
        world.notifyMouseButton(ButtonType.LEFT, ButtonPressType.UP, x, y);
    }
    @Override
    protected void notifyMouseWheelReleased(int x, int y) {
        world.notifyMouseButton(ButtonType.MIDDLE, ButtonPressType.UP, x, y);
    }
    @Override
    protected void notifyMouseRightReleased(int x, int y) {
        world.notifyMouseButton(ButtonType.RIGHT, ButtonPressType.UP, x, y);
    }

    @Override
    protected void notifyMouseMoved(int x, int y) {
        world.notifyMouseMoved(x,y);
    }

    @Override
    protected void notifyScroll(boolean b, int x, int y) {
        world.notifyMouseButton(ButtonType.SCROLL, b?ButtonPressType.DOWN:ButtonPressType.UP, x, y);
    }//switch ?     should clean up entierly
    
    @Override
    protected void notifyKeyPressed(int keyCode) {
        world.notifyKeyPressed(keyCode);
    }

    @Override
    protected void notifyKeyReleased(int keyCode) {
        world.notifyKeyReleased(keyCode);
    }
    
//    @Override
//    protected void notifyKeyTyped(int keyCode) {
//        world.notifyKeyTyped(keyCode);
//    }

    @Override
    protected void notifyComponentHidden() {
        world.notifyPanelHidden();
    }

    @Override
    protected void notifyComponentVisible() {
        world.notifyPanelVisible();
    }

    @Override
    protected void notifyComponentMoved() {
        world.notifyPanelMoved();
    }

    @Override
    protected void notifyComponentResized(int width, int height) {
        world.notifyPanelResized(width,height);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        world.draw(g);
    }


}

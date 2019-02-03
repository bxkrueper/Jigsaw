package worldUtility;

import java.awt.Color;
import java.awt.Graphics2D;

import camera.Camera;
import structure.InputState.InputStateReader;
import world.World;
import world.WorldObject;
import worldFunctionality.PanelDrawable;

public class DebugInputDisplay extends WorldObject implements PanelDrawable{
    
    private World world;
    
    public DebugInputDisplay(World world){
        this.world = world;
    }
    
    
    @Override
    public void draw(Graphics2D g) {
    }
    
    @Override
    public void drawDebug(Graphics2D g) {
        InputStateReader isr = getInputStateReader();
        g.setColor(Color.BLACK);
        g.drawString("Mouse panel: (" + Integer.toString(isr.getMousePanelX()) + "," + Integer.toString(isr.getMousePanelY()) + ")", 0, 20);
        g.drawString("Last left down panel: (" + Integer.toString(isr.getLastLeftDownX()) + "," + Integer.toString(isr.getLastLeftDownY()) + ")", 0, 50);
        g.drawString("Last wheel down panel: (" + Integer.toString(isr.getLastWheelDownX()) + "," + Integer.toString(isr.getLastWheelDownY()) + ")", 0, 80);
        g.drawString("Last right down panel: (" + Integer.toString(isr.getLastRightDownX()) + "," + Integer.toString(isr.getLastRightDownY()) + ")", 0, 110);
        
        g.drawString("Mouse world: (" + Double.toString(world.getMouseWorldX()) + "," + Double.toString(world.getMouseWorldY()) + ")", 150, 20);
//        g.drawString("Mouse world: " + Double.toString(world.getMouseWorldY()), 0, 170);
        g.drawString("Panel Width: " + Integer.toString(world.getWorldPanel().getWidth()), 0, 200);
        g.drawString("Panel Height: " + Integer.toString(world.getWorldPanel().getHeight()), 0, 230);
        g.drawString("Camera center X: " + Double.toString(world.getCamera().getX()), 0, 260);
        g.drawString("Camera center Y: " + Double.toString(world.getCamera().getY()), 0, 290);
        g.drawString("Camera zoom: " + Double.toString(world.getCamera().getZoom()), 0, 320);
        g.drawString("Camera top: " + Double.toString(world.getCamera().getTopCoordinate(world.getWorldPanel().getHeight())), 0, 350);
        g.drawString("Camera bottom: " + Double.toString(world.getCamera().getBottomCoordinate(world.getWorldPanel().getHeight())), 0, 380);
        g.drawString("Camera left: " + Double.toString(world.getCamera().getLeftCoordinate(world.getWorldPanel().getWidth())), 0, 410);
        g.drawString("Camera right: " + Double.toString(world.getCamera().getRightCoordinate(world.getWorldPanel().getWidth())), 0, 440);
        isr.draw(g, 50, 510);
    }

    @Override
    public void doOnAdd() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doOnDelete() {
        // TODO Auto-generated method stub
        
    }

}

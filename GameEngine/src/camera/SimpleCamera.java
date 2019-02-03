package camera;

import java.awt.Graphics2D;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;

public class SimpleCamera extends Camera{
    
    public SimpleCamera(double gameX, double gameY) {
        super(gameX, gameY);
    }
    
    @Override
    public double getZoom() {
        return 1.0;
    }
    
    @Override
    public double getLeftCoordinate(int panelWidth) {
        // TODO Auto-generated method stub
        return getX() - panelWidth/2;
    }

    @Override
    public double getRightCoordinate(int panelWidth) {
        // TODO Auto-generated method stub
        return getX() + panelWidth/2;
    }

    @Override
    public double getTopCoordinate(int panelHeight) {
        // TODO Auto-generated method stub
        return getY() + panelHeight/2;
    }

    @Override
    public double getBottomCoordinate(int panelHeight) {
        // TODO Auto-generated method stub
        return getY() - panelHeight/2;
    }

    @Override
    public double panelXToWorldX(int mousePanelX, int panelWidth) {
        return (mousePanelX - (panelWidth/2))+getX();
    }

    @Override
    public double panelYToWorldY(int mousePanelY, int panelHeight) {
        return ((panelHeight/2) - mousePanelY)+getY();
    }
    
    @Override
    public Coordinate panelPositionToWorldPosition(Coordinate2DDouble mousePanelPosition, int panelWidth, int panelHeight){
        return new Coordinate2DDouble(panelXToWorldX((int)mousePanelPosition.getX(),panelWidth),panelYToWorldY((int)mousePanelPosition.getY(),panelHeight));
    }

    @Override
    public int worldXToPanelX(double worldX, int panelWidth) {
        return (int) ((worldX-getX()) + (panelWidth/2));
    }

    @Override
    public int worldYToPanelY(double worldY, int panelHeight) {
        return (int)(-((worldY-getY()) - (panelHeight/2)));
    }
    
    @Override
    public Coordinate2DDouble worldPositionToPanelPosition(Coordinate worldPosition, int panelWidth, int panelHeight){
        return new Coordinate2DDouble(worldXToPanelX((int)worldPosition.getX(),panelWidth),worldYToPanelY((int)worldPosition.getY(),panelHeight));
    }
    
    @Override
    public void transformGraphicsObject(Graphics2D g,int panelWidth, int panelHeight) {
        g.translate(panelWidth/2, panelHeight/2);
        g.scale(1, -1);
        g.translate(-getX(), -getY());
    }
    
    @Override
    public void doOnAdd() {
    }
    @Override
    public void doOnDelete() {
    }
}

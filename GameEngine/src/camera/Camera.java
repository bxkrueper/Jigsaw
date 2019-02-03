package camera;

import java.awt.Graphics2D;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import world.WorldObject;

public abstract class Camera extends WorldObject{
    
    private Coordinate position;//center
    
    public Camera(double gameX, double gameY) {
        position = new Coordinate2DDouble(gameX,gameY);
    }
    
    public double getX() {
        return position.getX();
    }
    public void setX(double gameX) {
        position.setX(gameX);
    }
    public double getY() {
        return position.getY();
    }
    public void setY(double gameY) {
        position.setY(gameY);
    }
    public Coordinate getPosition() {
        return position;
    }
    public void setPosition(Coordinate c) {
        position.set(c);
    }
    public void setPosition(double gameX, double gameY) {
        position.setX(gameX);
        position.setY(gameY);
    }
    
    
    public abstract double getZoom();
    
    public abstract double panelXToWorldX(int mousePanelX, int panelWidth);
    public abstract double panelYToWorldY(int mousePanelY, int panelHeight);
    public abstract Coordinate panelPositionToWorldPosition(Coordinate2DDouble mousePanelPosition, int panelWidth, int panelHeight);
    ////////use coordinate2D.Integer?
    
    public abstract int worldXToPanelX(double worldX, int panelWidth);
    public abstract int worldYToPanelY(double worldY, int panelHeight);
    public abstract Coordinate2DDouble worldPositionToPanelPosition(Coordinate worldPosition, int panelWidth, int panelHeight);
    ////////use coordinate2D.Integer?
    
    public abstract double getLeftCoordinate(int panelWidth);
    public abstract double getRightCoordinate(int panelWidth);
    public abstract double getTopCoordinate(int panelHeight);
    public abstract double getBottomCoordinate(int panelHeight);
    
    
    
    public abstract void transformGraphicsObject(Graphics2D g,int panelWidth, int panelHeight);
}

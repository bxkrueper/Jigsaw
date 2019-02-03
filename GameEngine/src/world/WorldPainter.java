//graphics objects need to be updated every frame?   also update panel width and height every frame

//needs more flexibility

package world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import camera.Camera;
import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Picture;

public class WorldPainter {
    private Camera camera;//set with initialize
    private int panelWidth;//needs to be updated
    private int panelHeight;//needs to be updated
    
    private Graphics2D worldGraphics;
    private Graphics2D originalWorldCoordinateGraphics;//copied if worldGraphics is changed
    
    private boolean worldGraphicsChanged;//reset worldGraphics if a object messed with it
    
    public enum ReletiveThickness {WORLD,PIXLE}
    
    public WorldPainter(Camera c, int panelWidth, int panelHeight){
        this.camera = c;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.worldGraphicsChanged = false;
    }
    
    //get new base graphics objects every frame
    public void setGraphics(Graphics g) {
        
        this.originalWorldCoordinateGraphics = (Graphics2D) g.create();
        
        camera.transformGraphicsObject(originalWorldCoordinateGraphics, panelWidth, panelHeight);
        this.worldGraphics = (Graphics2D) originalWorldCoordinateGraphics.create();
    }

    //just sets what it knows to be the width, does not actually change the width of the panel
    //this must be updated every time the width changes.
    public void setPanelDimensions(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
    }
    
    public int getPanelWidth(){
        return panelWidth;
    }
    public int getPanelHeight(){
        return panelHeight;
    }
    
    
    public Font getFont(){
        return worldGraphics.getFont();
    }
    //worldGraphicsChanged does not keep track of these, as they can be set by the drawn objects as needed
    public void setFont(Font font){
        worldGraphics.setFont(font);
    }
    public void setColor(Color fillColor){
        worldGraphics.setColor(fillColor);
    }
    public void setStroke(double thickness, ReletiveThickness rt){
        if(rt == ReletiveThickness.WORLD){
            worldGraphics.setStroke(new BasicStroke((float) (thickness)));
        }else if(rt == ReletiveThickness.PIXLE){
            worldGraphics.setStroke(new BasicStroke((float) (thickness*camera.getZoom())));
        }
    }
    
    
    public void drawPixleContainingWorldCoordinate(double xValue, double yValue) {
        fillRectangle(xValue, yValue, getZoom(), getZoom());
    }
    
    public void drawLine(double x1, double y1, double x2, double y2){
        worldGraphics.draw(new Line2D.Double(x1,y1,x2,y2));
    }
    
    ///////////////change to not make new shape every time
    public void fillRectangle(double xLeft, double yBottom, double width, double height){
        worldGraphics.fill(new Rectangle.Double(xLeft,yBottom,width,height));
    }
    public void drawRectangle(double xLeft, double yBottom, double width, double height){
        worldGraphics.draw(new Rectangle.Double(xLeft,yBottom,width,height));
    }
    public void fillEllipse(double xCenter, double yCenter, double xRadius, double yRadius){
        worldGraphics.fill(new Ellipse2D.Double(xCenter-xRadius,yCenter-yRadius,xRadius*2,yRadius*2));
    }
    public void drawEllipse(double xCenter, double yCenter, double xRadius, double yRadius){
        worldGraphics.draw(new Ellipse2D.Double(xCenter-xRadius,yCenter-yRadius,xRadius*2,yRadius*2));
    }
    
    //coordinates as inputs instead
    public void drawLine(Coordinate2DDouble c1, Coordinate2DDouble c2){
        worldGraphics.draw(new Line2D.Double(c1.getX(),c1.getY(),c2.getX(),c2.getY()));
    }
    public void fillRectangle(Coordinate2DDouble blPosition, double width, double height){
        fillRectangle(blPosition.getX(),blPosition.getY(),width,height);
    }
    public void drawRectangle(Coordinate2DDouble blPosition, double width, double height){
        drawRectangle(blPosition.getX(),blPosition.getY(),width,height);
    }
    public void fillEllipse(Coordinate2DDouble center, double xRadius, double yRadius){
        fillEllipse(center.getX(),center.getY(),xRadius,yRadius);
    }
    public void drawEllipse(Coordinate2DDouble center, double xRadius, double yRadius){
        drawEllipse(center.getX(),center.getY(),xRadius,yRadius);
    }
    
    public void drawShape(Shape shape){
        worldGraphics.draw(shape);
    }
    public void fillShape(Shape shape){
        worldGraphics.fill(shape);
    }
    
    /////////////add rt argument?
    public void drawText(String text,double x,double y){
        worldGraphics.scale(1, -1);
        worldGraphics.drawString(text, (float) x, (float) -y);
        worldGraphics.scale(1, -1);
    }
    public void drawText(String text,double x,double y,int size,ReletiveThickness rt){
        if(rt == ReletiveThickness.WORLD){
            drawText(text,x,y);
        }else if(rt == ReletiveThickness.PIXLE){
            double zoom = getZoom();
            scale(1*zoom,1*zoom);
            setFontSize(size);
            drawText(text,x/getZoom(),y/getZoom());
            scale(1/zoom,1/zoom);
        }
        
    }
    
    public void setFontSize(int size){
        setFont(new Font(getFont().getName(),getFont().getStyle(),size));
    }
    
    
    //protected methods: objects don't have to worry about changing graphics back
    //WARNING: only automatically changes back after the whole Drawable object is done
    //if an object is not in the drawable list, the graphics will not reset right after it is done
    public void translate(double x, double y){
        worldGraphicsChanged = true;
        worldGraphics.translate(x, y);
    }
    public void translate(Coordinate c){
        translate(c.getX(),c.getY());
    }
    public void scale(double x, double y){
        worldGraphicsChanged = true;
        worldGraphics.scale(x, y);
    }
    public void rotate(double amount){
        worldGraphicsChanged = true;
        worldGraphics.rotate(amount);
    }
    public void rotateAbout(double amount,double x, double y){
        worldGraphicsChanged = true;
        worldGraphics.rotate(amount, x, y);
    }
    public void transform(AffineTransform at){
        worldGraphicsChanged = true;
        worldGraphics.transform(at);
    }
    public void clip(Shape clipShape){//clip is automatically changed when transformed to match the same area in different coordinates
        worldGraphicsChanged = true;
        worldGraphics.clip(clipShape);
    }
    
    //not sure what to set the clip to   just the camera boundaries?
//    public void restoreWorldClip(){
//        worldCoordinateGraphics.setClip(not null);
//    }
    
    //called by world draw method only. makes sure object didn't mess up worldCoordinateGraphics for next object's draw method
    public void finishedDrawingObject() {
        if(worldGraphicsChanged){
            resetWorldGraphics();
        }
    }

    public void resetWorldGraphics() {
        this.worldGraphics = (Graphics2D) originalWorldCoordinateGraphics.create();
        worldGraphicsChanged = false;
    }
    
    //int method mess things up with deep zoom
    //world coordinates  bl==bottomLeft
    //-height because world y axis is fliped. This does not affect solid color fills, but it does affect pictures
    public void drawPicture(Picture image, Coordinate2DDouble blPosition, double width, double height){
        drawPicture(image,blPosition.getX(),blPosition.getY(),width,height);
    }
    public void drawPicture(Picture image, double x, double y, double width, double height){
//        worldCoordinateGraphics.drawImage(image,(int) (x),(int) (y+height),(int) width,(int) -height,null);
        double yPlusHeight = y+height;
        worldGraphics.translate(x, yPlusHeight);
        worldGraphics.scale(width,-height);
        image.draw(worldGraphics,0,0,1,1);
//        worldGraphics.drawImage(image.getBufferedImage(),0,0,1,1,null);
        worldGraphics.scale(1/width,-1/height);
        worldGraphics.translate(-x, -(yPlusHeight));
    }
    
    public double panelXToWorldX(int mousePanelX){
        return camera.panelXToWorldX(mousePanelX, panelWidth);
    }
    public double panelYToWorldY(int mousePanelY){
        return camera.panelYToWorldY(mousePanelY, panelHeight);
    }
    public Coordinate panelPositionToWorldPosition(Coordinate2DDouble mousePanelPosition){
        return camera.panelPositionToWorldPosition(mousePanelPosition, panelWidth, panelHeight);
    }
    
    public int worldXToPanelX(double worldX){
        return camera.worldXToPanelX(worldX, panelWidth);
    }
    public int worldYToPanelY(double worldY){
        return camera.worldYToPanelY(worldY, panelHeight);
    }
    public Coordinate2DDouble worldPositionToPanelPosition(Coordinate worldPosition){
        return camera.worldPositionToPanelPosition(worldPosition, panelWidth, panelHeight);
    }
    public double getLeftCoordinate(){
        return camera.getLeftCoordinate(panelWidth);
    }
    public double getRightCoordinate(){
        return camera.getRightCoordinate(panelWidth);
    }
    public double getTopCoordinate(){
        return camera.getTopCoordinate(panelHeight);
    }
    public double getBottomCoordinate(){
        return camera.getBottomCoordinate(panelHeight);
    }

    public double getZoom() {
        return camera.getZoom();
    }

    

    
}

package hitbox;
//package utility;
//
//import java.awt.Color;
//
//import structure.Coordinate;
//import structure.WorldPainter;
//import worldFunctionality.SpatialEntity;
//
//public class CircularHitBox implements HitBox{
//    private double centerX,centerY,radius;//rel to owner's position
//    private static final Color color = new Color(255,255,0,100);
//    
//    public CircularHitBox(double relCenterX,double relCenterY, double radius){
//        this.centerX = relCenterX;
//        this.centerY = relCenterY;
//        this.radius = radius;
//    }
//    
//    @Override
//    public boolean occupies(double x, double y,SpatialEntity owner) {
//        Coordinate ownerPosition = owner.getPosition();
//        double centerWorldX = ownerPosition.getX()+centerX;
//        double centerWorldY = ownerPosition.getY()+centerY;
//        return radius>=Math.hypot(centerWorldX-x, centerWorldY-y);
//    }
//
//    @Override
//    public void draw(WorldPainter wp,SpatialEntity owner) {
//        wp.setColor(color);
//        Coordinate ownerPosition = owner.getPosition();
//        wp.fillEllipse(ownerPosition.getX()+centerX, ownerPosition.getY()+centerY, radius, radius);
//    }
//
//    @Override
//    public boolean occupiesRelative(double x, double y) {
//        return radius>=Math.hypot(centerX-x, centerY-y);
//    }
//}

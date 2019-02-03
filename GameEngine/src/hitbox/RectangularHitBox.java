package hitbox;
//package utility;
//
//import java.awt.Color;
//
//import structure.Coordinate;
//import structure.WorldPainter;
//import worldFunctionality.SpatialEntity;
//
//public class RectangularHitBox implements HitBox{
//
//    private double blx,bly,width,height;//rel to owner's position
//    private static final Color color = new Color(255,0,0,100);
//    
//    public RectangularHitBox(double relblx,double relbly, double width, double height){
//        this.blx = relblx;
//        this.bly = relbly;
//        this.width = width;
//        this.height = height;
//    }
//    
//    @Override
//    public boolean occupies(double x, double y,SpatialEntity owner) {
//        Coordinate ownerPosition = owner.getPosition();
//        return (x<ownerPosition.getX()+blx+width && x>ownerPosition.getX()+blx) && (y<ownerPosition.getY()+bly+height && y>ownerPosition.getY()+bly);
//    }
//
//    @Override
//    public void draw(WorldPainter wp,SpatialEntity owner) {
//        wp.setColor(color);
//        Coordinate ownerPosition = owner.getPosition();
//        wp.fillRectangle(ownerPosition.getX()+blx,ownerPosition.getY()+bly, width, height);
//    }
//
//    @Override
//    public boolean occupiesRelative(double x, double y) {
//        return (x<blx+width && x>blx) && (y<bly+height && y>bly);
//    }
//
//}

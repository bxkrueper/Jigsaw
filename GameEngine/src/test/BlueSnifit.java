//package test;
//
//import java.awt.event.KeyEvent;
//
//import coordinate.Coordinate;
//import coordinate.Coordinate2DDouble;
//import images.HitBoxSyncedAnimation;
//import images.ResourceGetter;
//import world.WorldObject;
//import world.WorldPainter;
//import worldFunctionality.WorldDrawable;
//import worldFunctionality.KeyReact;
//import worldFunctionality.MouseButtonReact.ButtonType;
//import worldFunctionality.MouseClickedOnReact;
//import worldFunctionality.MouseGrabbable;
//import worldFunctionality.SpatialEntity;
//
//public class BlueSnifit extends WorldObject implements WorldDrawable, KeyReact, MouseGrabbable, MouseClickedOnReact, SpatialEntity{
//    private HitBoxSyncedAnimation shootAnimation;
//    private Coordinate2DDouble position;//center
//    private double size;
//    
//    public BlueSnifit(Coordinate2DDouble position, double size){
//        this.shootAnimation = ResourceGetter.getHitBoxSyncedAnimation("images/BlueSnifit/animations/Shooting");
//        shootAnimation.startFromBeginning();
//        this.position = position;
//        this.size = size;
//        
////        Path2D path = new Path2D.Double(new Rectangle2D.Double(size/4,-size/2,size/4,size));
//        
////        HitBox hitbox1 = new ShapeHitBox(this,path);
//////        HitBox hitbox2 = new RectangularHitBox(this,-size/2,0,size/2,size/2);
////        HitBox hitbox3 = new RectangularHitBox(this,-size/2,-size/2,size/2,size/2);
//////        HitBox hitbox4 = new RectangularHitBox(this,0,-size/2,size/2,size/2);
////        HitBox hitbox5 = new CircularHitBox(this,0,0,size/2);
////        this.hitbox = new GroupHitBox(this,hitbox1,hitbox3,hitbox5);
//    }
//
//    @Override
//    public void draw(WorldPainter wp) {
//        wp.drawPicture(shootAnimation,new Coordinate2DDouble(position.getX()-size/2,position.getY()-size/2),size,size);////////////off
//    }
//    @Override
//    public void drawDebug(WorldPainter wp){
////        shootAnimation.getHitBox().draw(wp,this);
//    }
//    @Override
//    public void reactToKey(int keyCode,PressType t) {
//        if(keyCode == KeyEvent.VK_C && t==KeyReact.PressType.PRESSED){
//            position.setX(position.getX()+1);
//        }
//    }
//    @Override
//    public boolean occupies(double x, double y) {
//        return true;
////        return shootAnimation.getHitBox().occupies(x, y,this);
//    }
//
//    @Override
//    public boolean acceptTarget(ButtonType bt) {
//        return bt==ButtonType.LEFT;
//    }
//
//    @Override
//    public boolean consumeTargetable(ButtonType bt) {
//        return true;
//    }
//
//    @Override
//    public boolean acceptBeingGrabbed(ButtonType bt) {
//        return true;
//    }
//
//    @Override
//    public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce,Coordinate objectOrigionalLocation) {
//        position.set(objectOrigionalLocation.getX()+(grabLocation.getX()-grabSorce.getX()),objectOrigionalLocation.getY()+(grabLocation.getY()-grabSorce.getY()));
//    }
//
//    @Override
//    public void unGrabbed() {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public Coordinate getPosition() {
//        return position;
//    }
//
//    @Override
//    public void reactToMouseClickedOn(ButtonType bt, double x, double y) {
//        position.setX(position.getX()*-1);
//    }
//    @Override
//    public void reactToMouseDownOn(ButtonType bt, double x, double y) {
//    }
//
//    @Override
//    public void doOnAdd() {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public void doOnDelete() {
//        // TODO Auto-generated method stub
//        
//    }
//
//}

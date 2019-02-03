package jigsaw;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import coordinate.Coordinate;
import coordinate.Coordinate2DDouble;
import images.Picture;
import myAlgs.MyEvent;
import myAlgs.MyListener;
import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.MouseButtonReact.ButtonType;
import worldFunctionality.MouseClickedOnReact;
import worldFunctionality.MouseGrabbable;
import worldFunctionality.WorldDrawable;


public class JigSawPuzzle extends WorldObject {
    private Picture picture;
    
    private List<JigSawPiece> pieceList;
    
    private final double WIDTH;
    private final double HEIGHT;
    private final int ROWS;
    private final int COLUMNS;
    
    public final static Color shadowColor = new Color(127,127,127,127);
    
    private final double WIDTH_OF_SQUARE;
    private final double HEIGHT_OF_SQUARE;
    
    private final double TONGUE_WIDTH;
    private final double TONGUE_HEIGHT;
    private final double TONGUE_DEPTH;
    
    private final double CORNER_WIDTH;
    private final double CORNER_HEIGHT;
    
    private static final double R0 = 0;
    private static final double R90 = Math.PI/2;
    private static final double R180 = Math.PI;
    private static final double R270 = -R90;
    
    private enum EdgeType {HOLE,SIDE,TONGUE}
    private enum RotationType {CCW,CW}
    private enum RelLocation {TOP,RIGHT,BOTTOM,LEFT}
    
    private boolean rotatePieces;
    
    private MyListener listener;
    
    //stretches the picture if necessary to fit given width and height
    public JigSawPuzzle(Picture pic, double width, double height, int r, int c,boolean rotatePieces,MyListener listener){
        this.picture=pic;
        
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ROWS = r;
        this.COLUMNS = c;
        this.WIDTH_OF_SQUARE = WIDTH/COLUMNS;
        this.HEIGHT_OF_SQUARE = HEIGHT/ROWS;
        this.TONGUE_WIDTH = WIDTH_OF_SQUARE/5;//////////5 is kind of arbitrary  width of top and bottom fits
        this.TONGUE_HEIGHT = HEIGHT_OF_SQUARE/5;//////////5 is kind of arbitrary   height of right and left fits
        this.TONGUE_DEPTH = Math.min(TONGUE_WIDTH, TONGUE_HEIGHT);////?
        this.CORNER_WIDTH = (WIDTH_OF_SQUARE-TONGUE_WIDTH)/2;
        this.CORNER_HEIGHT = (HEIGHT_OF_SQUARE-TONGUE_HEIGHT)/2;
        this.rotatePieces = rotatePieces;
        this.listener = listener;
        
        makePieces();
        picture.start();
    }
    
    //uses the width and height of the picture
    public JigSawPuzzle(Picture pic, int r, int c,boolean rotatePieces, MyListener listener){
        this(pic,pic.getWidth(),pic.getHeight(),r,c,rotatePieces,listener);
    }
    
    @Override
    public void doOnAdd() {
        for(JigSawPiece piece: pieceList){
            this.addToWorld(piece);
        }
        distributePieces(new Rectangle2D.Double(-WIDTH,-HEIGHT,2*WIDTH,2*HEIGHT));/////////////need parameter
//        this.deleteFromWorld();//////////////////////////???????????????
    }
    
    @Override
    public void doOnDelete() {
        for(JigSawPiece p:pieceList){
            p.deleteFromWorld();
        }
        pieceList.clear();
    }
    
    private void distributePieces(Rectangle2D area){
        for(JigSawPiece piece: pieceList){
            //move center
            Coordinate2DDouble newLocation = getRandomPoint(area);
            piece.moveCenterTo(newLocation);
            
            //rotate
            if(rotatePieces){
                Random rand = new Random();
                int num = rand.nextInt(4);
                switch(num){
                case 0:
                    piece.rotate(RotationType.CCW,piece.getCenterOfPieceWorld());
                    break;
                case 1:
                    piece.rotate(RotationType.CW,piece.getCenterOfPieceWorld());
                    break;
                case 2:
                    piece.rotate180(piece.getCenterOfPieceWorld());
                    break;
                default:
                    //don't rotate
                }
            }
            
        }
    }
    
    
    //returns a random point in the rectangle
    private Coordinate2DDouble getRandomPoint(Rectangle2D area){
        double x = Math.random()*area.getWidth()+area.getX();
        double y = Math.random()*area.getHeight()+area.getY();
        return new Coordinate2DDouble(x,y);
    }
    
    public final void makePieces(){
        pieceList = new ArrayList<>(ROWS*COLUMNS);
        
        Fit[][] rightFits = new Fit[ROWS][COLUMNS-1];
        Fit[][] leftFits = new Fit[ROWS][COLUMNS-1];
        Fit[][] upperFits = new Fit[ROWS-1][COLUMNS];
        Fit[][] bottomFits = new Fit[ROWS-1][COLUMNS];
        
        
        Random rand = new Random();
        //fill rights and lefts, decide tongues and holes
        for(int row=0;row<rightFits.length;row++){
            for(int column=0;column<rightFits[0].length;column++){
                EdgeType rightEdgeType = rand.nextInt(2)==0?EdgeType.HOLE:EdgeType.TONGUE;
                rightFits[row][column] = new Fit(rightEdgeType,RelLocation.RIGHT);
                leftFits[row][column] = new Fit(rightEdgeType==EdgeType.HOLE?EdgeType.TONGUE:EdgeType.HOLE,RelLocation.LEFT);
                rightFits[row][column].setPartner(leftFits[row][column]);
                leftFits[row][column].setPartner(rightFits[row][column]);
            }
        }
        //fill uppers and bottoms, decide tongues and holes
        for(int row=0;row<upperFits.length;row++){
            for(int column=0;column<upperFits[0].length;column++){
                EdgeType upperEdgeType = rand.nextInt(2)==0?EdgeType.HOLE:EdgeType.TONGUE;
                upperFits[row][column] = new Fit(upperEdgeType,RelLocation.TOP);
                bottomFits[row][column] = new Fit(upperEdgeType==EdgeType.HOLE?EdgeType.TONGUE:EdgeType.HOLE,RelLocation.BOTTOM);
                upperFits[row][column].setPartner(bottomFits[row][column]);
                bottomFits[row][column].setPartner(upperFits[row][column]);
            }
        }
        
        //make pieces
        for(int row=0;row<ROWS;row++){
            for(int column=0;column<COLUMNS;column++){
                pieceList.add(makeJigSawPiece(row,column,rightFits,upperFits,leftFits,bottomFits));
            }
        }
    }
    
    //fits with side edge types are only used for building the shape (filling in the hole in the base piece)
    private JigSawPiece makeJigSawPiece(int row, int column, Fit[][] rightFits,Fit[][] upperFits, Fit[][] leftFits,Fit[][] bottomFits){
        List<Fit> fitList = new LinkedList<Fit>();
        fitList.add(column==COLUMNS-1?new Fit(EdgeType.SIDE,RelLocation.RIGHT):rightFits[row][column]);
        fitList.add(row==ROWS-1?new Fit(EdgeType.SIDE,RelLocation.TOP):upperFits[row][column]);
        
        fitList.add(column==0?new Fit(EdgeType.SIDE,RelLocation.LEFT):leftFits[row][column-1]);
        fitList.add(row==0?new Fit(EdgeType.SIDE,RelLocation.BOTTOM):bottomFits[row-1][column]);
        
        return new JigSawPiece(row,column,fitList);
    }
    
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        System.out.println("destroyed " + this);
//    }
    
    private void puzzleComplete(){
        MyEvent e = new MyEvent(this,"Puzzle Finished",null);
        listener.eventHappened(e);
    }
    
    //assumes there is only one piece in pieceList at this point
    public FinishedPuzzle getFinishedPicture() {
        return new FinishedPuzzle(picture,(Coordinate2DDouble) pieceList.get(0).getPosition(),pieceList.get(0).getRotation(),new Coordinate2DDouble(-WIDTH/2,-HEIGHT/2),WIDTH,HEIGHT);
    }
    
    
    
    
    
    
    
    
    private class JigSawPiece extends WorldObject implements WorldDrawable, MouseGrabbable,MouseClickedOnReact{
        
        private final int row;//////////not needed?
        private final int column;
        
        //Relative to picture coordinates
        private final Path2D picturePath;
        private Coordinate2DDouble pictureSorceWorldPosition;
        
        private List<Fit> fitList;
        
        private double rotation;
        
        
        private boolean beingDragged;
        
        public JigSawPiece(int row, int column,List<Fit> fitList){
            this.row = row;
            this.column = column;
            Coordinate2DDouble blPositionOfSquare = new Coordinate2DDouble(this.column*WIDTH_OF_SQUARE,this.row*HEIGHT_OF_SQUARE);
            
            this.pictureSorceWorldPosition = new Coordinate2DDouble(0,0);//fixed bug: Coordinate2D.zero was being shared by all pieces
            this.rotation = 0;
            this.beingDragged = false;
            this.fitList = fitList;///////will get rid of sides later
            this.picturePath = createBasePath(blPositionOfSquare.getX(),blPositionOfSquare.getY());
            claimFits(blPositionOfSquare.getX(),blPositionOfSquare.getY());
            finishPath();
        }
        
        
        public double getRotation(){
            return rotation;
        }
        
        ///////////correct?????
        public AffineTransform getTransformIntoWorldSpace(){
            AffineTransform at = new AffineTransform();
            at.rotate(rotation, pictureSorceWorldPosition.getX(), pictureSorceWorldPosition.getY());
            at.translate(pictureSorceWorldPosition.getX(), pictureSorceWorldPosition.getY());
            return at;
        }
        
        
        public void mergePath(Path2D newPath){
            picturePath.append(newPath, false);
        }
        
        public void giveFit(Fit fit){
            fit.setPiece(this);
            fitList.add(fit);
        }
        
        public void removeFit(Fit fit){
            fitList.remove(fit);
        }
        
        //assigns itself as its fits' piece
        //side fits have already been cleared out
        private void claimFits(double blSquareX, double blSquareY){
            for(Fit f: fitList){
                f.setPiece(this);
                f.calculateBlCornerInitial(blSquareX, blSquareY);
            }
        }
        
        //creates a all-hole piece
        private Path2D createBasePath(double picX, double picY){//picX,Y is lower left corner of piece (pic coordinates)
            Path2D path = new Path2D.Double(new Rectangle2D.Double(picX+TONGUE_DEPTH,picY+TONGUE_DEPTH,WIDTH_OF_SQUARE-2*TONGUE_DEPTH,HEIGHT_OF_SQUARE-2*TONGUE_DEPTH));
            
            path.append(new Rectangle2D.Double(picX,picY,CORNER_WIDTH,CORNER_HEIGHT),false);
            path.append(new Rectangle2D.Double(picX+WIDTH_OF_SQUARE-CORNER_WIDTH,picY,CORNER_WIDTH,CORNER_HEIGHT),false);
            path.append(new Rectangle2D.Double(picX,picY+HEIGHT_OF_SQUARE-CORNER_HEIGHT,CORNER_WIDTH,CORNER_HEIGHT),false);
            path.append(new Rectangle2D.Double(picX+WIDTH_OF_SQUARE-CORNER_WIDTH,picY+HEIGHT_OF_SQUARE-CORNER_HEIGHT,CORNER_WIDTH,CORNER_HEIGHT),false);
            
            return path;
        }
        
        //for each side: fills in the hole if side, fills in hole and adds tongue if tongue
        //removes fits that are actually sides
        private void finishPath(){
            Iterator<Fit> listIterator = fitList.iterator();
            while(listIterator.hasNext()){
                Fit fit = listIterator.next();
                fit.fillInPieceShape(picturePath);
                if(fit.edgeType==EdgeType.SIDE){
                    listIterator.remove();//side fits have served their purpose now
                }
            }
        }
        
        public void rotate(RotationType rt,Coordinate2DDouble worldPosition){
            if(rotation==R0){
                rotation = rt==RotationType.CCW?R90:R270;
            }else if(rotation==R90){
                rotation = rt==RotationType.CCW?R180:R0;
            }else if(rotation==R180){
                rotation = rt==RotationType.CCW?R270:R90;
            }else{
                rotation = rt==RotationType.CCW?R0:R180;
            }
            
            double amount = rt==RotationType.CCW?R90:R270;
            pictureSorceWorldPosition.rotateAbout(worldPosition, amount);//////may drift over time?
        }
        
        public void rotate180(Coordinate2DDouble worldPosition){
            if(rotation==R0){
                rotation = R180;
            }else if(rotation==R90){
                rotation = R270;
            }else if(rotation==R180){
                rotation = R0;
            }else{
                rotation = R90;
            }
            
            double amount = R180;
            pictureSorceWorldPosition.rotateAbout(worldPosition, amount);//////may drift over time?
        }
        
        
        //align wp with bl corner of whole picture first
        @Override
        public void draw(WorldPainter wp) {
            wp.translate(pictureSorceWorldPosition.getX(), pictureSorceWorldPosition.getY());
            wp.rotate(rotation);
            if(beingDragged){//draw shadow
                double directionToTravel = -Math.PI/4-rotation;//Relative to rotated axis
                double shiftDistanceX = WIDTH_OF_SQUARE/10*Math.cos(directionToTravel);
                double shiftDistanceY = HEIGHT_OF_SQUARE/10*Math.sin(directionToTravel);
                wp.translate(shiftDistanceX, shiftDistanceY);
                wp.setColor(shadowColor);
                wp.fillShape(picturePath);
                wp.translate(-shiftDistanceX, -shiftDistanceY);
            }
            wp.clip(picturePath);//order matters
            
            wp.drawPicture(picture,Coordinate2DDouble.zero,WIDTH,HEIGHT);
            
            
//            wp.rotateWorld(-rotation);
//            wp.translateWorld(-pictureSorceWorldPosition.getX(), -pictureSorceWorldPosition.getY());
//            wp.restoreWorldClip();
        }
        
        public void drawDebug(WorldPainter wp){
            wp.translate(pictureSorceWorldPosition.getX(), pictureSorceWorldPosition.getY());
            wp.rotate(rotation);
            
            wp.setColor(Color.orange);
            for(Fit f: fitList){
                wp.fillShape(f.getShape());
            }
            
            wp.rotate(-rotation);
            wp.translate(-pictureSorceWorldPosition.getX(), -pictureSorceWorldPosition.getY());
            
            wp.setColor(Color.darkGray);
            for(Fit f: fitList){
                wp.fillShape(f.getPathWorld().getBounds2D());
            }
            
            Coordinate2DDouble centerOfPieceWorld = getCenterOfPieceWorld();
            wp.setColor(Color.YELLOW);
            wp.fillEllipse(centerOfPieceWorld.getX(), centerOfPieceWorld.getY(), 3, 3);
            wp.setColor(Color.RED);
            wp.fillEllipse(pictureSorceWorldPosition.getX(), pictureSorceWorldPosition.getY(), 3, 3);
        }
        
        @Override
        public boolean occupies(double x, double y) {
            Coordinate2DDouble picturePosition = worldCoordinateToPictureCoordinate(new Coordinate2DDouble(x,y));
            return picturePath.contains(picturePosition.getX(), picturePosition.getY());
        }
        
        private Coordinate2DDouble worldCoordinateToPictureCoordinate(Coordinate2DDouble worldPosition){
            Coordinate2DDouble picturePosition = new Coordinate2DDouble(worldPosition.getX()-pictureSorceWorldPosition.getX(),worldPosition.getY()-pictureSorceWorldPosition.getY());
            picturePosition.rotateAbout(Coordinate2DDouble.zero, -rotation);
            return picturePosition;
        }

        @Override
        public boolean acceptTarget(ButtonType bt) {
            return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
        }

        @Override
        public boolean consumeTargetable(ButtonType bt) {
            return bt==ButtonType.LEFT || bt==ButtonType.RIGHT;
        }

        @Override
        public boolean acceptBeingGrabbed(ButtonType bt) {
            beingDragged = true;
            sendToFront();
            return true;
        }

        @Override
        public void unGrabbed() {
            beingDragged = false;
            
            tryToMerge();
        }
        
        private void tryToMerge(){
            JigSawPiece pieceItHasMergedWith = null;
            
            Iterator<Fit> listIterator = fitList.iterator();
            while(listIterator.hasNext()&&pieceItHasMergedWith==null){
                Fit fit = listIterator.next();
                pieceItHasMergedWith = attemptToMerge(fit);
                if(pieceItHasMergedWith!=null){
                    listIterator.remove();
                    break;
                }
            }
            
            //deal with other fits with the piece it has merged with
            if(pieceItHasMergedWith!=null){
                listIterator = fitList.iterator();//start from the beginning???
                while(listIterator.hasNext()){
                    Fit fit = listIterator.next();
                    listIterator.remove();
                    
                    Fit partnerFit = fit.getPartner();
                    JigSawPiece fitPartnersPiece = partnerFit.getPiece();
                    if(fitPartnersPiece==pieceItHasMergedWith){
                        pieceItHasMergedWith.removeFit(partnerFit);
                    }else{
                        pieceItHasMergedWith.giveFit(fit);
                    }
                }
                deleteFromWorld();
                pieceList.remove(this);//to remove from memory completely
                if(pieceList.size()==1){
                    puzzleComplete();
                }
            }
            
        }
        
        
        //tries to find a piece to merge with
        public JigSawPiece attemptToMerge(Fit fit) {
            if(fit.fitsWithPartner()){
                Fit partnerFit = fit.getPartner();
                JigSawPiece newPiece = partnerFit.getPiece();
                newPiece.mergePath(picturePath);
                ////////delete fit from this one?  would mess up iterator?
                newPiece.removeFit(partnerFit);
                return newPiece;
            }else{
                return null;
            }
        }
        

        @Override
        public void actOnGrab(Coordinate grabLocation, Coordinate grabSorce, Coordinate objectOrigionalLocation) {
            movePictureSorceWorldPositionTo(objectOrigionalLocation.getX()+(grabLocation.getX()-grabSorce.getX()),objectOrigionalLocation.getY()+(grabLocation.getY()-grabSorce.getY()));
        }
        
        public void movePictureSorceWorldPositionTo(double newX, double newY){
            pictureSorceWorldPosition.set(newX,newY);
        }

        @Override
        public Coordinate getPosition() {
            return pictureSorceWorldPosition;
        }
        
        public void moveCenterTo(Coordinate2DDouble newPosition){
            Coordinate2DDouble centerPic = getCenterOfPiecePicture();
//            Coordinate2D centerW = getCenterOfPieceWorld();
            movePictureSorceWorldPositionTo(newPosition.getX()-centerPic.getX(),newPosition.getY()-centerPic.getY());
        }
        
        public Coordinate2DDouble getCenterOfPiecePicture(){
            Rectangle2D rect = picturePath.getBounds2D();
            return new Coordinate2DDouble(rect.getCenterX(),rect.getCenterY());//center of piece in pic coordinates
        }
        
        //worldCoordinates    wrong?-think it is fixed (positive rotations)   slightly off because of tongues
        public Coordinate2DDouble getCenterOfPieceWorld(){
            Coordinate2DDouble add = getCenterOfPiecePicture();
            add.rotateAbout(0, 0, rotation);
            
            Coordinate2DDouble center = new Coordinate2DDouble(pictureSorceWorldPosition);
            center.set(center.getX()+add.getX(),center.getY()+add.getY());
            return center;
        }
        
        @Override
        public void reactToMouseClickedOn(ButtonType bt, double x, double y) {
            if(!rotatePieces){
                return;
            }
            
            Coordinate2DDouble about = new Coordinate2DDouble(x,y);
            if(bt==ButtonType.LEFT){
                rotate(RotationType.CCW,about);
            }else if(bt==ButtonType.RIGHT){
                rotate(RotationType.CW,about);
            }
        }
        @Override
        public void reactToMouseDownOn(ButtonType bt, double x, double y) {
        }


        @Override
        public void doOnAdd() {
            
        }


        @Override
        public void doOnDelete() {
            
        }
        
//        @Override
//        protected void finalize() throws Throwable {
//            super.finalize();
//            System.out.println("destroyed " + this);
//        }
    }
    
    
    
    
    
    
    
    private class Fit{
        public final EdgeType edgeType;
        public final RelLocation relLocation;
        private final Coordinate2DDouble blCorner;//picture coordinates
        ////////todo: save constant vars from piece instead of calculating
        
        private Fit partner;
        private JigSawPiece piece;
        
        public Fit(EdgeType edgeType,RelLocation relLocation){
            this.edgeType = edgeType;
            this.relLocation = relLocation;
            blCorner = new Coordinate2DDouble(0,0);//0's temperary
            
            
            partner = null;
            piece = null;
        }
        
        public Fit getPartner(){
            return partner;
        }
        public JigSawPiece getPiece(){
            return piece;
        }
        

        //Initialized later, unless it is a side
        public void setPartner(Fit partner){
            this.partner = partner;
        }
        //for constructor only
        //in picture coordinates
        public void calculateBlCornerInitial(double blSquareX, double blSquareY){
            if(relLocation==RelLocation.BOTTOM){
                if(edgeType==EdgeType.TONGUE){
                    blCorner.set(blSquareX+CORNER_WIDTH,blSquareY-TONGUE_DEPTH);
                }else{//is hole or side
                    blCorner.set(blSquareX+CORNER_WIDTH,blSquareY);
                }
            }else if(relLocation==RelLocation.LEFT){
                if(edgeType==EdgeType.TONGUE){
                    blCorner.set(blSquareX-TONGUE_DEPTH,blSquareY+CORNER_HEIGHT);
                }else{//is hole or side
                    blCorner.set(blSquareX,blSquareY+CORNER_HEIGHT);
                }
            }else if(relLocation==RelLocation.TOP){
                if(edgeType==EdgeType.TONGUE){
                    blCorner.set(blSquareX+CORNER_WIDTH,blSquareY+HEIGHT_OF_SQUARE-TONGUE_DEPTH);
                }else{//is hole or side
                    blCorner.set(blSquareX+CORNER_WIDTH,blSquareY+HEIGHT_OF_SQUARE-TONGUE_DEPTH);
                }
            }else{//relLocation==RelLocation.RIGHT
                if(edgeType==EdgeType.TONGUE){
                    blCorner.set(blSquareX+WIDTH_OF_SQUARE-TONGUE_DEPTH,blSquareY+CORNER_HEIGHT);
                }else{//is hole or side
                    blCorner.set(blSquareX+WIDTH_OF_SQUARE-TONGUE_DEPTH,blSquareY+CORNER_HEIGHT);
                }
            }
        }
        
        
        
        public void setPiece(JigSawPiece piece){
            this.piece = piece;
        }
        
        public double getWidth(){
            if(relLocation==RelLocation.BOTTOM || relLocation==RelLocation.TOP){
                return TONGUE_WIDTH;
            }else{// (relLocation==RelLocation.LEFT || relLocation==RelLocation.RIGHT)
                if(edgeType==EdgeType.TONGUE){
                    return 2*TONGUE_DEPTH;
                }else{//is hole or side
                    return TONGUE_DEPTH;
                }
            }
        }
        
        public double getHeight(){
            if(relLocation==RelLocation.BOTTOM || relLocation==RelLocation.TOP){
                if(edgeType==EdgeType.TONGUE){
                    return 2*TONGUE_DEPTH;
                }else{//is hole or side
                    return TONGUE_DEPTH;
                }
            }else{// (relLocation==RelLocation.LEFT || relLocation==RelLocation.RIGHT)
                return TONGUE_HEIGHT;
            }
        }
        
        //picture coordinates
        public Shape getShape(){
            double width = getWidth();
            double height = getHeight();
            return new Rectangle2D.Double(blCorner.getX(),blCorner.getY(),width,height);
        }
        
        public Path2D getPathWorld(){
            Path2D worldPath = new Path2D.Double(getShape());//picture coordinates now
            worldPath.transform(piece.getTransformIntoWorldSpace());
            return worldPath;
        }
        
        public void fillInPieceShape(Path2D picturePath) {
            if(edgeType==EdgeType.HOLE){
                return;//do nothing
            }
            
            picturePath.append(getShape(),false);
        }
        
        
        //check if it is intersecting in the world
        //////only works precisely for 90 degree rotations
        public boolean fitsWithPartner(){
            if(piece.getRotation()!=partner.getPiece().getRotation()){
                return false;
            }
            
            Path2D thisWorldPath = getPathWorld();
            Path2D partnerPath = partner.getPathWorld();
            boolean answer = thisWorldPath.getBounds2D().intersects(partnerPath.getBounds2D());
            return answer;
        }
        
        
        public String toString(){
            return "Piece: " + piece + " blCorner: " + blCorner + " EdgeType: " + edgeType + " relLocation: " + relLocation;
        }
        
        
    }

}

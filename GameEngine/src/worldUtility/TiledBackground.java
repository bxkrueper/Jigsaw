package worldUtility;

import java.awt.Color;
//arranges the texture in a grid with no flipping

import images.Picture;
import myAlgs.MyMath;
import images.ResourceGetter;
import world.WorldPainter;

/////////todo: flicker between normal and optimized when dragging at threshold
public class TiledBackground extends Background{
    
    private Color averageColor;
    private int maxPictures;//the maximum amount of pictures that are allowed to be drawn before just using average color
    private int step;//about how many pixels to skip when calculating color
    
    public TiledBackground(Picture pic,double width, double height, int maxPictures, int step){
        super(pic,width,height);
        this.maxPictures = maxPictures;
        this.step = step;
        this.averageColor = getAverageColor(pic);
    }
    public TiledBackground(Picture image, int maxPictures, int step){
        this(image,image.getWidth(),image.getHeight(), maxPictures, step);
    }
    public TiledBackground(String fileLocation,double width, double height, int maxPictures, int step){
        this(ResourceGetter.getPicture(fileLocation),width,height, maxPictures, step);
    }
    public TiledBackground(String fileLocation, int maxPictures, int step){
        this(ResourceGetter.getPicture(fileLocation), maxPictures, step);
    }
    
    private Color getAverageColor(Picture pic) {
        long sumR=0, sumG=0, sumB=0;
        int width = pic.getWidth(), height = pic.getHeight();
        int stepX = step, stepY = step;
        for(int row=0;row<width;row+=stepX){
            for(int column=0;column<height;column+=stepY){
                Color pixel = new Color(pic.getRGB(row, column));
                sumR += pixel.getRed();
                sumG += pixel.getGreen();
                sumB += pixel.getBlue();
            }
        }
        int num = (width * height)/(step*step);
        return new Color((int)(sumR/num), (int)(sumG/num), (int)(sumB/num));//color doesn't like longs?
    }
    
    @Override
    public void draw(WorldPainter wp) {
        double left = wp.getLeftCoordinate();
        double right = wp.getRightCoordinate();
        double top = wp.getTopCoordinate();
        double bottom = wp.getBottomCoordinate();
        
        double yStart = MyMath.roundDownToNearestMultipleOf(bottom,HEIGHT);
        double xStart = MyMath.roundDownToNearestMultipleOf(left,WIDTH);
        
        //determine if there are too many pictures to draw
        int rowsOfPictures = (int) ((right-xStart)/WIDTH);
        int columnsOfPictures = (int) ((top-yStart)/HEIGHT);
        if(rowsOfPictures*columnsOfPictures>maxPictures){
            wp.setColor(averageColor);
            wp.fillRectangle(left, bottom, right-left, top-bottom);
            return;
        }
        
        for(double y = yStart;y<top;y+=HEIGHT){
            for(double x = xStart;x<right;x+=WIDTH){
                drawImage(wp,x,y);
            }
        }
    }
    protected void drawImage(WorldPainter wp, double x, double y) {
        wp.drawPicture(getImage(), x, y, WIDTH, HEIGHT);
    }
}

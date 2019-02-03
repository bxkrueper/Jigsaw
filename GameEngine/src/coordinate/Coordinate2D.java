package coordinate;

public abstract class Coordinate2D implements Coordinate{

    
    public static double getXAxisAngle(Coordinate2DDouble c1, Coordinate2DDouble c2)
    {
        if (c1.compareYTo(c2)<0)
            return Math.acos((c2.getX()-c1.getX())/findDistance(c1,c2));
        else if (c1.compareYTo(c2)>0)
            return Math.acos((c1.getX()-c2.getX())/findDistance(c1,c2));
        else return 0.0;
    }

    @Override
    public double distanceTo(Coordinate c2) {
        return Math.sqrt(Math.pow(getX()-c2.getX(), 2)+Math.pow(getY()-c2.getY(), 2));
    }
    
    public static double findDistance(Coordinate2D c1, Coordinate2D c2)
    {
        return Math.sqrt(Math.pow(c1.getY()-c2.getY(),2)+Math.pow(c1.getX()-c2.getX(),2));
    }

    public static double findDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
}

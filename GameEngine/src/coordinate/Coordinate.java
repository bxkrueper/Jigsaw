package coordinate;

public interface Coordinate {

    public void set(Coordinate c2);
    
    public void setX(double x);

    public void setY(double y);

    public double getX();

    public double getY();

    public String toString();

    public boolean equals(Coordinate c2);

    public double distanceTo(Coordinate c2);

    public double compareXTo(Coordinate c2);

    public double compareYTo(Coordinate c2);
    
    public Coordinate copy();

    public static double fixAngle(double angle)
    {
        if (angle<0)
            return angle + 2*Math.PI;
        else if (angle >= 2*Math.PI)
            return angle - 2*Math.PI;
        else
            return angle;
    }

    public static double toDegrees(double angle)
    {
        return angle/Math.PI*180;
    }
    public static double toRadians(double angle)
    {
        return angle*Math.PI/180;
    }
}
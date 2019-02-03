package coordinate;

public class Coordinate2DInteger extends Coordinate2D{
    private int x;
    private int y;
    
    public static final Coordinate2DInteger zero = new Coordinate2DInteger(0,0);

    public Coordinate2DInteger(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Coordinate2DInteger(Coordinate2DInteger c)
    {
        this.x = (int) c.getX();
        this.y = (int) c.getY();
    }

    @Override
    public void set(Coordinate c2) {
        this.x = (int) c2.getX();
        this.y = (int) c2.getY();
    }
    
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getXInt()
    {
        return this.x;
    }

    public int getYInt()
    {
        return this.y;
    }
    
    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Coordinate c2)
    {
        if ((c2 instanceof Coordinate2DInteger) && this.getX() == c2.getX() && this.getY() == c2.getY())
            return true;
        else
            return false;
    }
    
    public double compareXTo(Coordinate c2)
    {
        return this.getX()-c2.getX();
    }

    public double compareYTo(Coordinate c2)
    {
        return this.getY()-c2.getY();
    }

    

    

    

    @Override
    public Coordinate copy() {
        return new Coordinate2DInteger(this);
    }

    @Override
    public void setX(double x) {
        this.x = (int) x;
    }

    @Override
    public void setY(double y) {
        this.y = (int) y;
    }
}

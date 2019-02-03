package coordinate;

public class Coordinate2DDouble extends Coordinate2D
{
    private double x;
    private double y;
    
    public static final Coordinate2DDouble zero = new Coordinate2DDouble(0,0);

    public Coordinate2DDouble(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Coordinate2DDouble(Coordinate2DDouble c)
    {
        this.x = c.getX();
        this.y = c.getY();
    }

    @Override
    public void set(Coordinate c2) {
        this.x = c2.getX();
        this.y = c2.getY();
    }
    
    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
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
        if ((c2 instanceof Coordinate2DDouble) && this.getX() == c2.getX() && this.getY() == c2.getY())
            return true;
        else
            return false;
    }


    public Coordinate getRotateAbout(Coordinate2DDouble c2, double angle)
    {
        //Coordinate rotated = new Coordinate(this.getX(),this.getY());
        //rotated.rotateAbout(c2,angle);
        return new Coordinate2DDouble((this.getX()-c2.getX())*Math.cos(angle)-(this.getY()-c2.getY())*Math.sin(angle)+c2.getX(),(this.getX()-c2.getX())*Math.sin(angle)+(this.getY()-c2.getY())*Math.cos(angle)+c2.getY());
    }

    public void rotateAbout(Coordinate2DDouble c2, double angle)
    {
        rotateAbout(c2.getX(),c2.getY(),angle);
    }
    
    public void rotateAbout(double xr, double yr, double angle){
        double oldX = this.getX();
        double oldY = this.getY();
        setX((oldX-xr)*Math.cos(angle)-(oldY-yr)*Math.sin(angle)+xr);
        setY((oldX-xr)*Math.sin(angle)+(oldY-yr)*Math.cos(angle)+yr);
    }

    public Coordinate getRelativeCoordinates(Coordinate2DDouble c2, double angle)
    {
        return new Coordinate2DDouble((this.getX()-c2.getX())*Math.cos(angle)-(this.getY()-c2.getY())*Math.sin(angle),(this.getX()-c2.getX())*Math.sin(angle)+(this.getY()-c2.getY())*Math.cos(angle));
    }

    public Coordinate getNormalCoordinates(Coordinate2DDouble c2, double angle)
    {
        return new Coordinate2DDouble((this.getX())*Math.cos(angle)-(this.getY())*Math.sin(angle)+c2.getX(),(this.getX())*Math.sin(angle)+(this.getY())*Math.cos(angle)+c2.getY());
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
        return new Coordinate2DDouble(this);
    }
    

    

}
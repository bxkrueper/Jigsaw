package worldFunctionality;

import coordinate.Coordinate;

public interface SpatialEntity {
    Coordinate getPosition();
    boolean occupies(double x, double y);
    //shape getArea()??? not everything needs a shape object
    //reletave coordinates?
}
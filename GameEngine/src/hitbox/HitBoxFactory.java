package hitbox;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class HitBoxFactory {

    public static HitBox getHitBox(String rowString) {
        String[] splitString = rowString.split(",");
        String firstWord = splitString[0];
        switch(firstWord){
        case "Rectangle":
            double blx = Double.parseDouble(splitString[1]);
            double bly = Double.parseDouble(splitString[2]);
            double width = Double.parseDouble(splitString[3]);
            double height = Double.parseDouble(splitString[4]);
            return new ShapeHitBox(new Rectangle.Double(blx,bly,width,height));
        case "Circle":
            double centerX = Double.parseDouble(splitString[1]);
            double centerY = Double.parseDouble(splitString[2]);
            double radius = Double.parseDouble(splitString[3]);
            double diameter = radius*2;
            Ellipse2D elipse = new Ellipse2D.Double(centerX-radius,centerY-radius,diameter,diameter);
            return new ShapeHitBox(elipse);
        case "Null":
            return new NullHitBox();
        default:
            return null;
        }
    }

}

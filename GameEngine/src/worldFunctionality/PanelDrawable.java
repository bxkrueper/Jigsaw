package worldFunctionality;

import java.awt.Graphics2D;

public interface PanelDrawable {
    public void draw(Graphics2D g);
    public default void drawDebug(Graphics2D g){}
}

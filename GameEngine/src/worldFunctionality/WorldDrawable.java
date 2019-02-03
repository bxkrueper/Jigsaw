package worldFunctionality;

import world.WorldPainter;

public interface WorldDrawable {
    public void draw(WorldPainter wp);
    public default void drawDebug(WorldPainter wp){}
}

package worldUtility;

import java.awt.Color;

import world.WorldPainter;

//Grid, but only displays in debug mode
public class DebugGrid extends Grid{
    
    @Override
    public void draw(WorldPainter wp) {
    }
    
    @Override
    public void drawDebug(WorldPainter wp) {
        super.draw(wp);
    }
}

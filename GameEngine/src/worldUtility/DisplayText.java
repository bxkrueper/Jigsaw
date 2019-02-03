package worldUtility;

import java.awt.Color;
import java.awt.Font;

import world.WorldObject;
import world.WorldPainter;
import worldFunctionality.WorldDrawable;

public class DisplayText extends WorldObject implements WorldDrawable{
    
    private String text;
    private Font font;
    private Color color;
    private double x,y;
    
    public DisplayText(String text, Font font, Color color, double x, double y){
        this.text = text;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
    }
    
    public void setText(String newText){
        this.text = newText;
    }
    
    
    @Override
    public void draw(WorldPainter wp) {
      wp.setFont(font);
      wp.setColor(color);
      wp.drawText(text,x,y);
    }

    @Override
    public void doOnAdd() {
    }

    @Override
    public void doOnDelete() {
    }

}

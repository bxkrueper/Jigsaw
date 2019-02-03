package worldUtility;

import images.Picture;
import images.ResourceGetter;
import world.WorldObject;
import worldFunctionality.WorldDrawable;

public abstract class Background extends WorldObject implements WorldDrawable{
    private Picture image;
    public final double WIDTH;
    public final double HEIGHT;
    
    public Background(Picture pic,double width, double height){
        this.image = pic;
        this.WIDTH = width;
        this.HEIGHT = height;
    }
    public Background(Picture image){
        this(image,image.getWidth(),image.getHeight());
    }
    public Background(String fileLocation,double width, double height){
        this(ResourceGetter.getPicture(fileLocation),width,height);
    }
    public Background(String fileLocation){
        this(ResourceGetter.getPicture(fileLocation));
    }
    
    //////////to getPicture
    public Picture getImage(){
        return image;
    }

    @Override
    public void doOnAdd() {
        sendToBack();
    }
    @Override
    public void doOnDelete() {
    }
}

package worldFunctionality;

public interface ScreenReact {
    void notifyComponentHidden();

    void notifyComponentVisible();

    void notifyComponentMoved();

    void notifyComponentResized(int width, int height);
    
}

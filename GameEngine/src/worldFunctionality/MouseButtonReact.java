package worldFunctionality;

public interface MouseButtonReact {
    public enum ButtonType {LEFT,RIGHT,MIDDLE,SCROLL}//Separate?
    public enum ButtonPressType {DOWN,UP}//Separate?
    void reactToMouseButton(ButtonType bt,ButtonPressType bpt,double x, double y);
}

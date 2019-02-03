package worldFunctionality;

import worldFunctionality.MouseButtonReact.ButtonType;

public interface MouseClickedOnReact extends MouseTargetable{
    void reactToMouseClickedOn(ButtonType bt,double x, double y);////////change to reletive coordinates?
    void reactToMouseDownOn (ButtonType bt,double x, double y);//may not result in a click if it turns into a grab
}

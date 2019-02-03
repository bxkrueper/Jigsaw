package worldFunctionality;

import worldFunctionality.MouseButtonReact.ButtonPressType;

public interface MouseScrolledOnReact extends MouseTargetable{
    void reactToMouseScrolledOn(ButtonPressType bpt, double x, double y);//////reletive coordinates?
}

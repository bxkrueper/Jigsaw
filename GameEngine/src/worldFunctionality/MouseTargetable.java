package worldFunctionality;

import worldFunctionality.MouseButtonReact.ButtonType;
//ray casting
public interface MouseTargetable extends SpatialEntity{
    boolean acceptTarget(ButtonType bt);//return true if it wants to be targeted
    boolean consumeTargetable(ButtonType bt);//return true it the object wants to stop other targets under it from being selected
}
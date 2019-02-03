package worldFunctionality;

public interface KeyReact {
    public enum PressType {PRESSED,RELEASED,TYPED}
    void reactToKey(int keyCode,PressType t);//example:   if(keyCode==KeyEvent.VK_C)
}

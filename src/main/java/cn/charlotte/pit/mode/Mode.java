package cn.charlotte.pit.mode;

/**
 * @author Araykal
 * @since 2025/2/1
 */
public enum Mode {
    Myhic("Mythic"," "),Normal("Normal","RPGItem");
    public final String name;
    public final String internal;
    Mode(String name,String internal){
        this.name = name;
        this.internal = internal;
    }
}

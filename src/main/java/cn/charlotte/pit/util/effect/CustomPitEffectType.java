package cn.charlotte.pit.util.effect;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/9 14:35
 */
public enum CustomPitEffectType {

    PIN_DOWN("&2阻滞"),
    BLEED("&c流血"),
    HEMORRHAGED("&4失衡");

    private final String displayName;

    CustomPitEffectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

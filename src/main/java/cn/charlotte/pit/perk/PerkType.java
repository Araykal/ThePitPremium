package cn.charlotte.pit.perk;

import lombok.Getter;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/19 13:08
 */
@Getter
public enum PerkType {
    PERK("天赋"),
    KILL_STREAK("连杀天赋"),
    MEGA_STREAK("超级连杀天赋");

    private final String displayName;

    PerkType(String displayName) {
        this.displayName = displayName;
    }
}

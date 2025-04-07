package cn.charlotte.pit.event;

import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 1:09
 */
public class PitDamageEvent extends PitEvent {
    private final Player attacker;
    private final double finalDamage;
    private final double damage;

    public PitDamageEvent(Player attacker, double finalDamage, double damage) {
        this.attacker = attacker;
        this.finalDamage = finalDamage;
        this.damage = damage;
    }

    public Player getAttacker() {
        return attacker;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getDamage() {
        return damage;
    }

}

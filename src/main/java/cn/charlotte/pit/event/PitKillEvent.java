package cn.charlotte.pit.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 1:07
 */
public class PitKillEvent extends PitEvent {
    private final Player killer;
    private final LivingEntity target;
    private double coins;
    private double exp;

    public PitKillEvent(Player killer, LivingEntity target, double coins, double exp) {
        this.killer = killer;
        this.target = target;
        this.coins = coins;
        this.exp = exp;
    }

    public Player getKiller() {
        return killer;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
}

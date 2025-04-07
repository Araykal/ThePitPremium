package cn.charlotte.pit.event;

import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/4 13:03
 */

public class PitAssistEvent extends PitEvent {
    private final Player assist;
    private final Player target;

    private double coins;
    private double exp;

    public PitAssistEvent(Player assist, Player target, double coins, double exp) {
        this.assist = assist;
        this.target = target;
        this.coins = coins;
        this.exp = exp;
    }

    public Player getAssist() {
        return assist;
    }

    public Player getTarget() {
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

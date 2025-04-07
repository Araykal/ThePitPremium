package cn.charlotte.pit.data.temp;

import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/28 13:24
 */
public class TradeRequest {
    private Player player;
    private Player target;
    private Cooldown cooldown;

    public TradeRequest(Player player, Player target) {
        this.player = player;
        this.target = target;
        this.cooldown = new Cooldown(15, TimeUnit.SECONDS);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getTarget() {
        return this.target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TradeRequest)) return false;
        final TradeRequest other = (TradeRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) return false;
        final Object this$target = this.getTarget();
        final Object other$target = other.getTarget();
        if (this$target == null ? other$target != null : !this$target.equals(other$target)) return false;
        final Object this$cooldown = this.getCooldown();
        final Object other$cooldown = other.getCooldown();
        if (this$cooldown == null ? other$cooldown != null : !this$cooldown.equals(other$cooldown)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TradeRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $target = this.getTarget();
        result = result * PRIME + ($target == null ? 43 : $target.hashCode());
        final Object $cooldown = this.getCooldown();
        result = result * PRIME + ($cooldown == null ? 43 : $cooldown.hashCode());
        return result;
    }

    public String toString() {
        return "TradeRequest(player=" + this.getPlayer() + ", target=" + this.getTarget() + ", cooldown=" + this.getCooldown() + ")";
    }
}

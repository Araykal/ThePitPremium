package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * @Creator Misoryan
 * @Date 2021/5/8 20:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PitRegainHealthEvent extends PitEvent implements Cancellable {
    private final Player player;
    private double amount;
    private boolean cancelled;

    public PitRegainHealthEvent(Player player, double amount) {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public Player getPlayer() {
        return player;
    }
}

package cn.charlotte.pit.event;

import cn.charlotte.pit.data.PlayerProfile;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Cancellable;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/25 22:25
 */
@EqualsAndHashCode(callSuper = true)
public class PitStreakKillChangeEvent extends PitEvent implements Cancellable {
    private final PlayerProfile playerProfile;
    private final double from;
    private final double to;
    private boolean cancelled;

    public PitStreakKillChangeEvent(PlayerProfile playerProfile, double from, double to) {
        this.playerProfile = playerProfile;
        this.from = from;
        this.to = to;
    }

    public PitStreakKillChangeEvent(PlayerProfile playerProfile, double from, double to, boolean cancelled) {
        this.playerProfile = playerProfile;
        this.from = from;
        this.to = to;
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
}

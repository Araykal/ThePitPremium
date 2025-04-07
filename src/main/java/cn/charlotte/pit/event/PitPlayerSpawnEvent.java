package cn.charlotte.pit.event;

import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/21 13:55
 */
@EqualsAndHashCode(callSuper = true)
public class PitPlayerSpawnEvent extends PitEvent {
    private final Player player;

    public PitPlayerSpawnEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }


}

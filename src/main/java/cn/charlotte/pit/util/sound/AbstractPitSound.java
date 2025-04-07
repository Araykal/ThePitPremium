package cn.charlotte.pit.util.sound;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/26 13:32
 */
public abstract class AbstractPitSound {
    private final Map<Player, Integer> playersTick;

    public AbstractPitSound() {
        this.playersTick = new HashMap<>();
    }

    public abstract String getMusicInternalName();

    public abstract void onSoundTick(Player player, int tick);

    public void tick() {
        final HashMap<Player, Integer> map = new HashMap<>(playersTick);
        for (Map.Entry<Player, Integer> entry : map.entrySet()) {
            final Player player = entry.getKey();
            if (player == null || !player.isOnline()) {
                playersTick.remove(player);
                continue;
            }
            Integer tick = entry.getValue();
            this.onSoundTick(player, tick);
            tick++;
            playersTick.put(player, tick);

        }
    }

    public void play(Player player) {
        this.playersTick.put(player, 0);
    }

    public void end(Player player) {
        this.playersTick.remove(player);
    }

}

package cn.charlotte.pit.util.sound.type;

import cn.charlotte.pit.util.sound.AbstractPitSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/8 22:06
 */
public class EventOpenSound extends AbstractPitSound {
    @Override
    public String getMusicInternalName() {
        return "event_open";
    }

    @Override
    public void onSoundTick(Player player, int tick) {
        if (tick == 0 || tick == 1 || tick == 2 || tick == 3) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 0.95F);
        } else if (tick == 4 || tick == 5 || tick == 6 || tick == 7) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1.05F);
        } else if (tick == 8 || tick == 9 || tick == 10 || tick == 11) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1.15F);
        } else {
            this.end(player);
        }
    }
}

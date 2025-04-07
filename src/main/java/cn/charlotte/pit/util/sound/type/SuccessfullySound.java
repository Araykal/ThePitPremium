package cn.charlotte.pit.util.sound.type;

import cn.charlotte.pit.util.sound.AbstractPitSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/26 13:32
 */
public class SuccessfullySound extends AbstractPitSound {
    @Override
    public String getMusicInternalName() {
        return "successfully";
    }

    @Override
    public void onSoundTick(Player player, int tick) {
        if (tick == 0 || tick == 1 || tick == 2) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 0.95F);
        } else if (tick == 4 || tick == 5 || tick == 6) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1.05F);
        } else if (tick == 8 || tick == 9 || tick == 10) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1.15F);
        } else if (tick > 10) {
            this.end(player);
        }
    }
}

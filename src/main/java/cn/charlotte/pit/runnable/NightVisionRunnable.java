package cn.charlotte.pit.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Iterator;

public class NightVisionRunnable extends BukkitRunnable {
    private Iterator<? extends Player> playerIterator = null;
    private final PotionEffect nightVisionEffect = PotionEffectType.NIGHT_VISION.createEffect(114154, 2);

    @Override
    public void run() {
        if (playerIterator == null || !playerIterator.hasNext()) {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            if (onlinePlayers.isEmpty()) {
                return;
            }
            playerIterator = onlinePlayers.iterator();
        }

        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.addPotionEffect(nightVisionEffect);
                break;
            }
        }
    }
}
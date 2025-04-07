package cn.charlotte.pit.runnable;

import cn.charlotte.pit.data.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/2 12:16
 */
public class AutoSaveRunnable extends BukkitRunnable {
    @Override
    public void run() {
        PlayerProfile.saveAll();

        final long now = System.currentTimeMillis();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("pit.admin")) return;
            final long lastActionTimestamp = PlayerProfile
                    .getPlayerProfileByUuid(player.getUniqueId())
                    .getLastActionTimestamp();

            if (now - lastActionTimestamp >= 10 * 60 * 1000) {
                player.kickPlayer("在加载您的天坑乱斗数据时出现了一个问题,您可以尝试再次进入游戏以重试.");
            }
        });
    }
}

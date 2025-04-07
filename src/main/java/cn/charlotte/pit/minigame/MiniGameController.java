package cn.charlotte.pit.minigame;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.minigame.type.FourInARow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/24 22:05
 */

public class MiniGameController extends BukkitRunnable implements Listener {
    private final List<AbstractMiniGame> miniGameInstances;
    private long tick;

    public MiniGameController() {
        this.miniGameInstances = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance());
    }

    @Override
    public void run() {
        for (AbstractMiniGame game : this.miniGameInstances) {
            if (tick % game.getLoopTick() == 0) {
                game.onTick();
            }
        }
        tick++;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ArrayList<AbstractMiniGame> list = new ArrayList<>(this.miniGameInstances);
        for (AbstractMiniGame game : list) {
            if (game instanceof FourInARow) {
                final FourInARow row = (FourInARow) game;
                if (row.getBlackPlayer().equals(player) || row.getWhitePlayer().equals(player)) {
                    row.end();
                }
            }
        }
    }

    public List<AbstractMiniGame> getMiniGameInstances() {
        return this.miniGameInstances;
    }
}

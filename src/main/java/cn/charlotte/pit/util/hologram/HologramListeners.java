package cn.charlotte.pit.util.hologram;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/13 19:35
 */
//@AutoRegister
public class HologramListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        for (Hologram h : HologramAPI.getHolograms()) {
            if (h.isSpawned()) {
                if (h.getLocation().getWorld().getName().equals(e.getTo().getWorld().getName())) {
                    try {
                        HologramAPI.spawn(h, new ArrayList<>(Collections.singletonList(p)));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        for (Hologram h : HologramAPI.getHolograms()) {
            if (h.isSpawned()) {
                if (h.getLocation().getWorld().getName().equals(p.getWorld().getName())) {
                    try {
                        HologramAPI.spawn(h, new ArrayList<>(Collections.singletonList(p)));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent e) {
        for (Hologram h : HologramAPI.getHolograms()) {
            if (h.isSpawned()) {
                if (h.getLocation().getChunk().equals(e.getChunk())) {
                    try {
                        HologramAPI.spawn(h, new ArrayList<>(h.getLocation().getWorld().getPlayers()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

}

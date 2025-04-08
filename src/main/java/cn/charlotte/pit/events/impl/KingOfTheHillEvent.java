package cn.charlotte.pit.events.impl;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.INormalEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.hologram.Hologram;
import cn.charlotte.pit.util.hologram.HologramAPI;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KingOfTheHillEvent implements IEvent, INormalEvent, Listener {

    private static final int EVENT_DURATION_MINUTES = 4;
    private static final int RADIUS = 7;

    private final Map<Block, Material> blocks = new HashMap<>();
    private final Map<Block, Byte> blockData = new HashMap<>();
    @Getter
    private static HillData hillData;
    private boolean isActive = false;

    @Override
    public String getEventInternalName() {
        return "koth";
    }

    @Override
    public String getEventName() {
        return "&b&l占山为王";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if ("koth".equals(ThePit.getInstance().getEventFactory().getActiveNormalEventName())) {
            hillData.spawnHologramsForPlayer(event.getPlayer());
        }
    }

    @Override
    public void onActive() {
        isActive = true;
        hillData = new HillData();

        Location location = ThePit.getInstance().getPitConfig().getKothLoc();
        if (location == null) {
            Bukkit.broadcastMessage(CC.translate("&c&l占山为王！ &7活动区域未设置，请联系管理员设置！"));
            deactivateEvent();
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance());
        Bukkit.broadcastMessage(CC.translate("&b&l占山为王！ &7现在你可以在指定区域内获得四倍的&b经验&7与&6硬币&7加成！"));

        spawnKothCircle(location, RADIUS, Material.GOLD_BLOCK);
        scheduleEventDeactivation(EVENT_DURATION_MINUTES);
    }

    private void scheduleEventDeactivation(int minutes) {
        Bukkit.getScheduler().runTaskLater(
                ThePit.getInstance(),
                this::deactivateEvent,
                20L * 60 * minutes
        );
    }

    private void deactivateEvent() {
        ThePit.getInstance().getEventFactory().inactiveEvent(this);
        onInactive();
    }

    @Override
    public void onInactive() {
        despawnKothCircle();
        HandlerList.unregisterAll(this);
        isActive = false;
    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        if (isActive && isRegion(event.getKiller())) {
            event.setCoins(event.getCoins() * 4);
            event.setExp(event.getExp() * 4);
        }
    }

    public void spawnKothCircle(Location center, int radius, Material material) {
        int extendedRadius = radius + 1;
        int squaredRadius = extendedRadius * extendedRadius;

        World world = center.getWorld();
        int centerX = center.getBlockX();
        int centerY = center.getBlockY();
        int centerZ = center.getBlockZ();
        for (int x = centerX - extendedRadius; x <= centerX + extendedRadius; x++) {
            for (int z = centerZ - extendedRadius; z <= centerZ + extendedRadius; z++) {
                if (Math.pow(centerX - x, 2) + Math.pow(centerZ - z, 2) <= squaredRadius) {
                    Block block = world.getBlockAt(x, centerY, z);
                    saveBlockData(block);
                    block.setType(material);
                }
            }
        }
        setupCentralBeacon(center);
        setupHolograms(center);
    }

    private void saveBlockData(Block block) {
        blocks.put(block, block.getType());
        blockData.put(block, block.getData());
    }

    private void setupCentralBeacon(Location center) {
        Block beaconBlock = center.clone().add(0, -1, 0).getBlock();
        saveBlockData(beaconBlock);
        beaconBlock.setType(Material.BEACON);
    }

    private void setupHolograms(Location center) {
        hillData.setHolograms(
                HologramAPI.createHologram(center.clone().add(0.5, 5.0, 0.5), CC.translate("&b&l占山为王")),
                HologramAPI.createHologram(center.clone().add(0.5, 4.5, 0.5), CC.translate("&e&l4x &b经验值&e/&6硬币"))
        );
        hillData.spawnAllHolograms();
    }

    public void despawnKothCircle() {
        blocks.forEach((block, type) -> block.setType(type));
        blockData.forEach((block, data) -> block.setData(data));
        hillData.despawnAllHolograms();
    }

    public boolean isRegion(Player player) {
        Location loc = player.getLocation();
        return isGoldBlock(loc.add(0, -1, 0)) || isGoldBlock(loc.add(0, -2, 0));
    }

    private boolean isGoldBlock(Location location) {
        return location.getBlock().getType() == Material.GOLD_BLOCK;
    }

    @Data
    public static class HillData {
        private Hologram firstHologram;
        private Hologram secondHologram;

        public void setHolograms(Hologram first, Hologram second) {
            this.firstHologram = first;
            this.secondHologram = second;
        }

        public void spawnAllHolograms() {
            firstHologram.spawn();
            secondHologram.spawn();
        }

        public void despawnAllHolograms() {
            if (firstHologram != null) firstHologram.deSpawn();
            if (secondHologram != null) secondHologram.deSpawn();
        }

        public void spawnHologramsForPlayer(Player player) {
            firstHologram.spawn(Collections.singletonList(player));
            secondHologram.spawn(Collections.singletonList(player));
        }
    }
}

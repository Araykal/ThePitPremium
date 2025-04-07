package cn.charlotte.pit.util.bossbar;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBar {
    private final HashMap<UUID, EntityWither> withers = new HashMap<>();
    private String title;

    public BossBar(String title) {
        this.title = title;
    }

    public void addPlayer(Player player) {
        if (withers.containsKey(player.getUniqueId())) {
            this.removePlayer(player);
        }
        EntityWither wither = new EntityWither(
                ((CraftWorld) player.getWorld()).getHandle());
        Location location = getWitherLocation(player.getLocation());
        wither.setCustomName(title);
        wither.setInvisible(true);
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        withers.put(player.getUniqueId(), wither);
    }

    public void removePlayer(Player player) {
        EntityWither wither = withers.remove(player.getUniqueId());
        if (wither == null) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void setProgress(double progress) {
        final HashMap<UUID, EntityWither> map = new HashMap<>(withers);
        for (Map.Entry<UUID, EntityWither> entry : map.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                EntityWither entityWither = entry.getValue();
                entityWither.setHealth((float) (progress * entityWither.getMaxHealth()));
                PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(
                        entityWither.getId(), entityWither.getDataWatcher(), true);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void update() {
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                EntityWither entityWither = entry.getValue();
                Location location = getWitherLocation(player.getLocation());
                entityWither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entityWither);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void update(Player player) {
        EntityWither wither = this.withers.get(player.getUniqueId());
        if (wither == null) {
            return;
        }

        Location location = getWitherLocation(player.getLocation());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(wither);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public Location getWitherLocation(Location location) {
        return location.clone().add(location.getDirection().multiply(60));
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        if (this.title.equals(title)) return;
        this.title = title;
        final HashMap<UUID, EntityWither> map = new HashMap<>(this.withers);
        for (Map.Entry<UUID, EntityWither> entry : map.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                EntityWither entityWither = entry.getValue();
                entityWither.setCustomName(title);
                PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityWither.getId(), entityWither.getDataWatcher(), true);
                ((CraftPlayer) player).getHandle().playerConnection
                        .sendPacket(packet);
            }
        }
    }

    public HashMap<UUID, EntityWither> getWithers() {
        return this.withers;
    }
}

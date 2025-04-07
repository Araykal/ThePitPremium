package cn.charlotte.pit.enchantment.runnable;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.PitConfig;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.menu.MythicWellMenu;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/11 21:10
 */
@Getter
public class AnimationRunnable extends BukkitRunnable {
    private final Map<UUID, AnimationData> animations = new ConcurrentHashMap<>();
    private final List<Location> animationLocations;

    public AnimationRunnable() {
        final PitConfig pitConfig = ThePit.getInstance().getPitConfig();
        final Location loc = pitConfig.getEnchantLocation();
        final Location center;
        if (loc == null) {
            center = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        } else {
            center = loc.clone().add(0.0, -1.0, 0.0);
        }
        //12 -2 中心
        this.animationLocations = Arrays.asList(
                center.clone().add(0, 0, -1),
                center.clone().add(1, 0, -1),
                center.clone().add(1, 0, 0),
                center.clone().add(1, 0, 1),
                center.clone().add(0, 0, 1),
                center.clone().add(-1, 0, 1),
                center.clone().add(-1, 0, 0),
                center.clone().add(-1, 0, -1)
        );

        this.runTaskTimerAsynchronously(ThePit.getInstance(), 20, 2);

    }

    @Override
    public void run() {
        synchronized (animations) {
            final HashMap<UUID, AnimationData> removeMap = new HashMap<>(animations);
            removeMap.forEach((uuid, animationData) -> {
                if (!animationData.getPlayer().isOnline() ||
                        !(Menu.currentlyOpenedMenus.get(animationData.getPlayer().getName()) instanceof MythicWellMenu)) {
                    animations.remove(uuid);
                }
            });

            for (AnimationData data : animations.values()) {
                Menu menu = Menu.currentlyOpenedMenus.get(data.getPlayer().getName());

                if (data.isFinished()) {
                    Player player = data.getPlayer();
                    PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                    String mythicColor = ItemUtil.getItemStringData(InventoryUtil.deserializeItemStack(profile.getEnchantingItem()), "mythic_color");
                    MythicColor foundColor = null;
                    for (MythicColor color : MythicColor.values()) {
                        if (color.getInternalName().equals(mythicColor)) {
                            foundColor = color;
                            break;
                        }
                    }
                    if (foundColor == null) {
                        continue;
                    }

                    for (Location location : animationLocations) {
                        player.sendBlockChange(location, Material.STAINED_GLASS, foundColor.getColorByte());
                    }
                    continue;
                }

                if (data.isStartEnchanting()) {
                    Player player = data.getPlayer();
                    PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                    String mythic_color = ItemUtil.getItemStringData(InventoryUtil.deserializeItemStack(profile.getEnchantingItem()), "mythic_color");
                    MythicColor foundColor = null;
                    for (MythicColor color : MythicColor.values()) {
                        if (color.getInternalName().equals(mythic_color)) {
                            foundColor = color;
                            break;
                        }
                    }
                    if (foundColor == null) {
                        continue;
                    }

                    if (data.animationTick == 0 || data.animationTick == 1 || data.animationTick == 4 || data.animationTick == 5) {
                        for (Location location : animationLocations) {
                            player.sendBlockChange(location, Material.STAINED_GLASS, foundColor.getColorByte());
                        }
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1.2F);
                    } else if (data.animationTick == 2 || data.animationTick == 3 || data.animationTick == 6 || data.animationTick == 7) {
                        for (Location location : animationLocations) {
                            player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 0);
                        }
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1.2F);
                    } else {
                        if (data.animationTick <= 23) {
                            int i = data.animationTick % 8;
                            Location location = animationLocations.get(i);
                            if (i == 0) {
                                player.sendBlockChange(animationLocations.get(7), Material.STAINED_GLASS, (byte) 0);
                            } else {
                                player.sendBlockChange(animationLocations.get(i - 1), Material.STAINED_GLASS, (byte) 0);
                            }
                            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 0.3F + i * 0.5F);
                            player.sendBlockChange(location, Material.STAINED_GLASS, foundColor.getColorByte());
                        } else {
                            data.finished = true;
                            menu.openMenu(player);
                            continue;
                        }
                    }


                    menu.openMenu(data.player);


                } else {
                    if (data.animationTick > 14) {
                        data.animationTick = 0;
                    }

                    if (data.animationTick % 2 != 0) {
                        data.animationTick++;
                        continue;
                    }

                    int realTick = data.animationTick / 2;

                    //refresh menu
                    menu.openMenu(data.getPlayer());

                    //refreshBlock
                    Location location = animationLocations.get(realTick);

                    //rollback
                    if (data.animationTick == 0) {
                        data.player.sendBlockChange(animationLocations.get(7), Material.STAINED_GLASS, (byte) 0);
                    } else {
                        data.player.sendBlockChange(animationLocations.get(realTick - 1), Material.STAINED_GLASS, (byte) 0);
                    }

                    //send new block
                    data.player.sendBlockChange(location, Material.STAINED_GLASS, data.color);

                }
                data.animationTick++;
            }
        }
    }

    public void sendReset(Player player) {
        for (Location location : animationLocations) {
            player.sendBlockChange(location, Material.SEA_LANTERN, (byte) 0);
        }
    }

    public void sendStart(Player player) {
        for (Location location : animationLocations) {
            player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 0);
        }
    }

    @Getter
    @Setter
    public static class AnimationData {
        private final Player player;
        private int animationTick = 0;
        private byte color = (byte) 6;
        private boolean startEnchanting = false;
        private boolean finished = false;

        public AnimationData(Player player) {
            this.player = player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnimationData data = (AnimationData) o;
            return Objects.equals(player.getUniqueId(), data.player.getUniqueId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(player.getUniqueId());
        }
    }
}

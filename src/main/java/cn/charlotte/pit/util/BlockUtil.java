package cn.charlotte.pit.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Araykal
 * @since 2025/1/22
 */
public class BlockUtil {
    public static Set<Material> blockType = new HashSet<>();

    static {
        blockType.add(Material.CACTUS);
        blockType.add(Material.ENDER_PORTAL);
    }

    public static boolean isBlockNearby(Location location, int range) {
        World world = location.getWorld();
        int baseX = location.getBlockX();
        int baseY = location.getBlockY();
        int baseZ = location.getBlockZ();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    Block block = world.getBlockAt(baseX + x, baseY + y, baseZ + z);
                    if (blockType.contains(block.getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package cn.charlotte.pit.runnable;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.random.RandomUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/2 13:21
 */
public class GoldDropRunnable extends BukkitRunnable {
    List<Item> itemGarbageList = new ArrayList<>();
    long tick;
    @Override
    public void run() {
        tick++;
        if(tick < 0){
            tick = 0;
        }
        for (int i = 0; i < 2; i++) {
            Location location = this.generateLocation();
            Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.GOLD_INGOT, 1));
            item.setMetadata("gold", new FixedMetadataValue(ThePit.getInstance(), RandomUtil.random.nextInt(3) + 3));
            itemGarbageList.add(item);
        }
        if(tick % 20 == 0){
            itemGarbageList.forEach(item -> {
                item.removeMetadata("gold", ThePit.getInstance());
                item.remove();
            });
            itemGarbageList.clear();
        }
    }


    private Location generateLocation() {
        return RandomUtil.generateRandomLocation();
    }

}

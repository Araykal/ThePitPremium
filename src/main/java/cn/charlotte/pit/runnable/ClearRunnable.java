package cn.charlotte.pit.runnable;

import cn.charlotte.pit.data.sub.DroppedEntityData;
import cn.charlotte.pit.data.sub.PlacedBlockData;
import cn.charlotte.pit.util.cooldown.Cooldown;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:12
 */
@Getter
public class ClearRunnable extends BukkitRunnable {

    private static ClearRunnable clearRunnable;
    public final List<PlacedBlockData> placedBlock;
    private final List<DroppedEntityData> entityData;

    public ClearRunnable() {
        clearRunnable = this;
        this.placedBlock = new ArrayList<>();
        this.entityData = new ArrayList<>();
    }

    public static ClearRunnable getClearRunnable() {
        return clearRunnable;
    }

    @Override
    public void run() {
        List<PlacedBlockData> shouldRemove = new ArrayList<>();

        for (PlacedBlockData data : this.placedBlock) {
            if (data.getCooldown().hasExpired()) {
                Location location = data.getLocation();
                location.getBlock().setType(Material.AIR);

                shouldRemove.add(data);
            }
        }

        this.placedBlock.removeAll(shouldRemove);


        List<DroppedEntityData> shouldRemoveEntity = new ArrayList<>();
        for (DroppedEntityData data : entityData) {
            if (data.getTimer().hasExpired()) {
                data.getEntity().remove();
                shouldRemoveEntity.add(data);
            }
        }

        entityData.removeAll(shouldRemoveEntity);
    }

    public void placeBlock(Location location) {
        this.placeBlock(location, new Cooldown(360, TimeUnit.SECONDS));
    }

    public void placeBlock(Location location, Cooldown cooldown) {
        this.placedBlock.add(new PlacedBlockData(location, cooldown));
    }
}

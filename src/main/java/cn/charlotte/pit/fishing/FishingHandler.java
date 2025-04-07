package cn.charlotte.pit.fishing;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.random.RandomUtil;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/6 20:12
 */
@AutoRegister
public class FishingHandler implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            event.setExpToDrop(0);
            event.getCaught().remove();

            Location location = event.getCaught().getLocation();

            ItemStack itemStack = this.generateItem(event.getPlayer(), location);
            EntityItem entityItem = this.generateEntity(event.getPlayer(), location, itemStack);
            CraftEntity item = entityItem.getBukkitEntity();
            item.setMetadata("gold", new FixedMetadataValue(ThePit.getInstance(), RandomUtil.random.nextInt(5) + 5));
        } else if (event.getState() == PlayerFishEvent.State.FISHING) {
            ItemStack item = event.getPlayer().getItemInHand();

        }
    }


    private EntityItem generateEntity(Player player, Location dropLocation, ItemStack item) {
        WorldServer worldServer = ((CraftWorld) dropLocation.getWorld()).getHandle();
        EntityItem entityItem = new EntityItem(worldServer, dropLocation.getX(), dropLocation.getY(), dropLocation.getZ(), CraftItemStack.asNMSCopy(item));
        worldServer.addEntity(entityItem);

        double d5 = player.getLocation().getX() - dropLocation.getX();
        double d6 = player.getLocation().getY() - dropLocation.getY();
        double d7 = player.getLocation().getZ() - dropLocation.getZ();
        double d8 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
        double d9 = 0.1D;

        entityItem.motX = d5 * d9;
        entityItem.motY = d6 * d9 + (double) MathHelper.sqrt(d8) * 0.08D;
        entityItem.motZ = d7 * d9;

        return entityItem;
    }

    private ItemStack generateItem(Player player, Location location) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        return new ItemBuilder(Material.GOLD_INGOT)
                .build();
    }

}

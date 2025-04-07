package cn.charlotte.pit.item.type;

import cn.charlotte.pit.item.AbstractPitItem;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.util.MythicUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 17:30
 */
public class MythicRepairKit extends AbstractPitItem implements Listener {

    public static ItemStack toItemStack0() {
        return new ItemBuilder(Material.SHEARS)
                .name("&d神话工匠包")
                .lore(
                        "&e特殊物品",
                        "&c无法用于交易",
                        "&7手持要修复的神话物品,在背包内右键点击神话工匠包,",
                        "&7即可恢复手上的神话物品全部生命.",
                        "&7(神话工匠包会在使用后消耗)"
                )
                .internalName("mythic_repair_kit")
                .canSaveToEnderChest(true)
                .canTrade(false)
                .shiny()
                .buildWithUnbreakable();
    }

    @Override
    public String getInternalName() {
        return "mythic_repair_kit";
    }

    @Override
    public String getItemDisplayName() {
        return "剪刀";
    }

    @Override
    public Material getItemDisplayMaterial() {
        return Material.SHEARS;
    }

    @Override
    public ItemStack toItemStack() {
        return toItemStack0();
    }

    @Override
    public void loadFromItemStack(ItemStack item) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            if ("mythic_repair_kit".equals(ItemUtil.getInternalName(item))) {
                Player player = (Player) event.getWhoClicked();
                ItemStack itemInHand = player.getItemInHand();
                if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                    return;
                }
                IMythicItem mythicItem = MythicUtil.getMythicItem(itemInHand);
                if (mythicItem == null) return;
                if (mythicItem instanceof LuckyChestplate) return;

                //if player is holding mythic item
                if (event.getClick() == ClickType.RIGHT && ItemUtil.getItemIntData(itemInHand, "maxLive") != null && ItemUtil.getItemIntData(itemInHand, "maxLive") > 0) {
                    event.setCancelled(true);
                    event.setCurrentItem(new ItemStack(Material.AIR));
                    mythicItem.loadFromItemStack(itemInHand);

                    mythicItem.setLive(mythicItem.getMaxLive());
                    player.setItemInHand(mythicItem.toItemStack());
                    player.sendMessage(CC.translate("&d&l神话工匠包! &7你手中的神话物品现已完全修复."));
                }
            }
        }
    }
}

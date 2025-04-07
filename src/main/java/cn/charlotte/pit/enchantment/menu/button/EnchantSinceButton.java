package cn.charlotte.pit.enchantment.menu.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/12 23:38
 */
@RequiredArgsConstructor
public class EnchantSinceButton extends Button {
    private final Menu menu;
    private final MythicColor color;

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        String sinceItem = profile.getEnchantingScience();
        if (sinceItem == null) return this.getNoneItem(color);
        ItemStack item = InventoryUtil.deserializeItemStack(sinceItem);
        if (item == null || item.getType() == Material.AIR) {
            return this.getNoneItem(color);
        }

        return item;
    }

    private ItemStack getNoneItem(MythicColor color) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(15)
                .name("&d放入神话之甲作为升级材料")
                .lore("&7要将此物品升级至等级 &a" + (color == MythicColor.DARK ? "II" : "III"), "&7需要放入 " + color.getChatColor() + color.getDisplayName() + "色神话之甲 &7作为升级材料.")
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        String sinceItem = profile.getEnchantingScience();
        if (sinceItem == null) return;
        ItemStack item = InventoryUtil.deserializeItemStack(sinceItem);
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        profile.setEnchantingScience(InventoryUtil.serializeItemStack(new ItemStack(Material.AIR)));
        player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 0.65F);

        this.menu.openMenu(player);
    }
}

package cn.charlotte.pit.menu.workshop.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.workshop.DyeWorkshopMenu;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WorkshopEntranceButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lines = new ArrayList<>();
        lines.add("&7在这里,你可以为神话之井上的");
        lines.add("&7的神话之甲进行染色!");
        lines.add("");
        lines.add("&c染色仅影响外观,不影响其升阶所需神话之甲颜色/改变自身颜色属性.");
        lines.add("");
        if (profile.isSupporter() || PlayerUtil.isStaff(player)) {
            if (profile.getEnchantingItem() != null && InventoryUtil.deserializeItemStack(profile.getEnchantingItem()) != null && InventoryUtil.deserializeItemStack(profile.getEnchantingItem()).getType().equals(Material.LEATHER_LEGGINGS)) {
                ItemStack itemStack = InventoryUtil.deserializeItemStack(profile.getEnchantingItem());
                String mythicColor = ItemUtil.getItemStringData(itemStack, "mythic_color");
                if (mythicColor == null || mythicColor.equalsIgnoreCase("dark")) {
                    lines.add("&c此颜色的神话之甲无法附魔!");
                } else {
                    lines.add("&e点击前往!");
                }
            } else {
                lines.add("&c请先在神话之井中放入神话之甲后重试!");
            }
        } else {
            lines.add("&c你需要购买 &e天坑乱斗会员 &c后才能使用此功能!");
        }
        return new ItemBuilder(Material.ANVIL)
                .name((profile.getPrestige() >= 2 ? "&a" : "&c") + "前往染色工坊")
                .lore(lines)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getEnchantingItem() != null && InventoryUtil.deserializeItemStack(profile.getEnchantingItem()) != null && InventoryUtil.deserializeItemStack(profile.getEnchantingItem()).getType().equals(Material.LEATHER_LEGGINGS)) {
            ItemStack itemStack = InventoryUtil.deserializeItemStack(profile.getEnchantingItem());
            String mythicColor = ItemUtil.getItemStringData(itemStack, "mythic_color");
            if (mythicColor != null && !mythicColor.equalsIgnoreCase("dark")) {
                new DyeWorkshopMenu().openMenu(player);
            }
        }
    }
}

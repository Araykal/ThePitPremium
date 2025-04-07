package cn.charlotte.pit.menu.perk;

import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:36
 */
public class UnKnowButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BEDROCK)
                .name("&c&l锁定中")
                .lore(" ", "&7&o前面的区域,以后再来探索吧!")
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

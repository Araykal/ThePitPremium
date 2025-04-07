package cn.charlotte.pit.menu.prestige.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.perk.prestige.PrestigePerkBuyMenu;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/4 20:51
 */
public class PrestigeShopButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lore = new ArrayList<>();
        lore.add("&7使用从 &b精通 &7中获取的 &e声望");
        lore.add("&7来解锁各种强力加成!");
        lore.add("");
        lore.add("&7&o这些加成不受精通重置影响.");
        lore.add(" ");
        lore.add("&7声望: &e" + profile.getRenown() + " 声望");
        lore.add(" ");
        if (profile.getPrestige() < 1) {
            lore.add("&c此内容在精通后解锁!");
        } else {
            lore.add("&e点击查看!");
        }
        return new ItemBuilder(Material.BEACON).name("&e声望商店").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getPrestige() > 0) {
            new PrestigePerkBuyMenu().openMenu(player);
        }
    }
}

package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.menu.shop.button.AbstractShopButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/13 21:06
 */
public class ObsidianStackShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "obsidian_stack";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add(" ");
        lines.add("&7&o死亡后消失");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        PerkData data = profile.getUnlockedPerkMap().get("ObsidianStackShopUnlock");
        if (data != null) {
            return new ItemBuilder(Material.OBSIDIAN)
                    .lore(lines)
                    .canTrade(true)
                    .amount(64)
                    .build();
        }
        return new ItemBuilder(Material.AIR)
                .build();
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

    @Override
    public ItemStack[] getResultItem(Player player) {
        return new ItemStack[]{
                new ItemBuilder(Material.OBSIDIAN).canTrade(true).canDrop(true).canSaveToEnderChest(true).deathDrop(true).amount(64).internalName("shopItem").build(),
        };
    }

    @Override
    public int getPrice(Player player) {
        return 280;
    }
}

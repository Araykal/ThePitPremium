package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.shop.button.AbstractShopButton;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/29 14:15
 */
public class GoldPickaxeShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "gold_pickaxe";
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

        if (PlayerUtil.isPlayerUnlockedPerk(player, "GoldPickaxeShopUnlock")) {
            return new ItemBuilder(Material.GOLD_PICKAXE)
                    .lore(lines)
                    .enchant(Enchantment.DIG_SPEED, 7)
                    .buildWithUnbreakable();
        } else {
            return new ItemBuilder(Material.AIR).build();
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

    @Override
    public ItemStack[] getResultItem(Player player) {
        return new ItemStack[]{new ItemBuilder(Material.GOLD_PICKAXE)
                .deathDrop(true)
                .canSaveToEnderChest(true)
                .canDrop(true)
                .enchant(Enchantment.DIG_SPEED, 7)
                .internalName("shopItem")
                .build()};
    }

    @Override
    public int getPrice(Player player) {
        return 50;
    }
}

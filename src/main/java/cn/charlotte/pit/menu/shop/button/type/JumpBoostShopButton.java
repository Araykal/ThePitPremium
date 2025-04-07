package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.item.type.JumpBoostPotion;
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
 * @Created_In: 2021/3/29 19:15
 */
public class JumpBoostShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "jump_boost_potion";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        ItemStack itemStack = JumpBoostPotion.toItemStack();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lines = new ArrayList<>(itemStack.getItemMeta().getLore());
        lines.add(" ");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        PerkData data = profile.getUnlockedPerkMap().get("jump_boost_potion_shop_unlock");
        if (data != null) {
            return new ItemBuilder(itemStack)
                    .lore(lines)
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
                JumpBoostPotion.toItemStack()
        };
    }

    @Override
    public int getPrice(Player player) {
        return 750;
    }
}

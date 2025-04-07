package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.ThePit;
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
 * @Creator Misoryan
 * @Date 2021/5/14 19:30
 */
public class BountySolventShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "bounty_solvent";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lines = new ArrayList<>(ThePit.getApi().getMythicItemItemStack("bounty_solvent_potion").getItemMeta().getLore());
        lines.add(" ");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        PerkData data = profile.getUnlockedPerkMap().get("bounty_solvent_shop_perk");
        if (data != null) {
            return new ItemBuilder(Material.POTION)
                    .name("&6赏金溶剂 (01:00)")
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
                ThePit.getApi().getMythicItemItemStack("bounty_solvent_potion")
        };
    }

    @Override
    public int getPrice(Player player) {
        return 575;
    }
}

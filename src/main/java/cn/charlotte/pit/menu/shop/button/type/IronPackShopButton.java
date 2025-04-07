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
 * @Created_In: 2021/1/13 20:46
 */
public class IronPackShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "iron_pack";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7包含铁胸甲,铁护腿与铁靴子.");
        lines.add(" ");
        lines.add("&7&o死亡后消失");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        PerkData data = profile.getUnlockedPerkMap().get("IronPackShopUnlock");
        if (data != null) {
            return new ItemBuilder(Material.IRON_CHESTPLATE)
                    .name("&f铁质盔甲套装")
                    .lore(lines)
                    .deathDrop(true)
                    .canTrade(true)
                    .buildWithUnbreakable();
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
                new ItemBuilder(Material.IRON_CHESTPLATE).canDrop(true).canSaveToEnderChest(true).deathDrop(true).lore("&6从商店中获得").internalName("shopItem").build(),
                new ItemBuilder(Material.IRON_LEGGINGS).canDrop(true).canSaveToEnderChest(true).deathDrop(true).lore("&6从商店中获得").internalName("shopItem").build(),
                new ItemBuilder(Material.IRON_BOOTS).canDrop(true).canSaveToEnderChest(true).deathDrop(true).lore("&6从商店中获得").internalName("shopItem").build()
        };
    }

    @Override
    public int getPrice(Player player) {
        return 200;
    }
}

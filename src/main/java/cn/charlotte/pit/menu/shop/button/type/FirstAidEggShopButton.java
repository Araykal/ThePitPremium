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
 * @Created_In: 2021/3/21 18:55
 */
public class FirstAidEggShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "first_aid_egg";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7使用后恢复自身 &c2.5❤ &7(30秒冷却)");
        lines.add("&7击杀一名玩家将会减少5秒冷却");
        lines.add(" ");
        lines.add("&7&o死亡后消失");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        PerkData data = profile.getUnlockedPerkMap().get("first_aid_egg_perk");
        if (data != null) {
            return new ItemBuilder(Material.MONSTER_EGG)
                    .durability(96)
                    .name("&c急救蛋")
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
                new ItemBuilder(Material.MONSTER_EGG)
                        .durability(96)
                        .name("&c急救蛋")
                        .deathDrop(true)
                        .canSaveToEnderChest(true)
                        .internalName("first_aid_egg")
                        .build(),
        };
    }

    @Override
    public int getPrice(Player player) {
        return 200;
    }
}

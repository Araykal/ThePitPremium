package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
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
 * @Created_In: 2021/1/13 21:15
 */
public class DiamongLeggingsShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "diamond_leggings";
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

        PerkData data = profile.getUnlockedPerkMap().get("DiamondLeggingsShopUnlock");
        if (data != null) {
            return new ItemBuilder(Material.DIAMOND_LEGGINGS)
                    .lore(lines)
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
        return new ItemStack[]{new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .deathDrop(true)
                .canSaveToEnderChest(true)
                .canDrop(true)
                .internalName("shopItem")
                .lore("&6从商店中获得")
                .buildWithUnbreakable()
        };
    }

    @Override
    public int getPrice(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTeam() == GenesisTeam.ANGEL && profile.getGenesisData().getTier() >= 3) {
            return (int) (0.35 * 1200);
        }
        return 1200;
    }
}

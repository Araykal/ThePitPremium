package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
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
 * @Created_In: 2021/1/1 18:39
 */
public class DiamondBootsShopButton extends AbstractShopButton {

    @Override
    public String getInternalName() {
        return "diamond_boots";
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

        return new ItemBuilder(Material.DIAMOND_BOOTS)
                .lore(lines)
                .buildWithUnbreakable();
    }

    @Override
    public ItemStack[] getResultItem(Player player) {
        return new ItemStack[]{new ItemBuilder(Material.DIAMOND_BOOTS)
                .deathDrop(true)
                .canSaveToEnderChest(true)
                .canDrop(true)
                .canTrade(true)
                .canTrade(true)
                .internalName("shopItem")
                .buildWithUnbreakable()
        };
    }

    @Override
    public int getPrice(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTeam() == GenesisTeam.ANGEL && profile.getGenesisData().getTier() >= 3) {
            return new Double(0.35 * 300).intValue();
        }
        return 300;
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

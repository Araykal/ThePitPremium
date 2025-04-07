package cn.charlotte.pit.menu.shop.button.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.menu.shop.button.AbstractShopButton;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/29 14:10
 */
@AutoRegister
public class CombatSpadeShopButton extends AbstractShopButton implements Listener {


    @Override
    public String getInternalName() {
        return "combat_spade";
    }

    @Override
    public ItemStack getDisplayButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7攻击穿有钻石装备的玩家时,");
        lines.add("&7此玩家每穿有一件 &b钻石装备 &7,");
        lines.add("&7战斗之铲的基础攻击力 &c+1(0.5❤) &7.");
        lines.add("&7&o实际效果请以此处商店描述为准");
        lines.add(" ");
        lines.add("&7&o死亡后消失");
        lines.add("&7价格: &6" + getDiscountPrice(player, getPrice(player)) + " 硬币");

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            lines.add("&e点击购买!");
        } else {
            lines.add("&c硬币不足!");
        }

        if (PlayerUtil.isPlayerUnlockedPerk(player, "CombatSpadeShopUnlock")) {
            return new ItemBuilder(Material.DIAMOND_SPADE)
                    .lore(lines)
                    .itemDamage(7)
                    .name("&e战斗之铲")
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
        List<String> lines = new ArrayList<>();
        lines.add("&7攻击穿有钻石装备的玩家时,");
        lines.add("&7此玩家每穿有一件 &b钻石装备 &7,");
        lines.add("&7战斗之铲的基础攻击力 &c+1(0.5❤) &7.");
        lines.add("&7&o实际效果请以商店描述为准");

        return new ItemStack[]{new ItemBuilder(Material.DIAMOND_SPADE)
                .deathDrop(true)
                .canSaveToEnderChest(true)
                .canDrop(true)
                .canTrade(false)
                .itemDamage(7)
                .name("&e战斗之铲")
                .lore(lines)
                .internalName("shopItem")
                .buildWithUnbreakable()};
    }

    @Override
    public int getPrice(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTeam() == GenesisTeam.ANGEL && profile.getGenesisData().getTier() >= 3) {
            return (int) (0.35 * 350);
        }
        return 350;
    }
}

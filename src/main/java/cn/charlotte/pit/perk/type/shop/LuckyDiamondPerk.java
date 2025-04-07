package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 19:27
 */
public class LuckyDiamondPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "LuckyDiamond";
    }

    @Override
    public String getDisplayName() {
        return "幸运钻石";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND;
    }

    @Override
    public double requireCoins() {
        return 3000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 60;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7拾取击杀掉落的装备时");
        lines.add("&7掉落的装备有 &b30% &7的几率");
        lines.add("&7变为同类型的 &b钻石装备 &7.");
        return lines;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {
        InventoryUtil.removeItemWithInternalName(player, "LuckyDiamond");
        InventoryUtil.removeItemWithInternalName(player, "lucky_diamond");
    }
}

package cn.charlotte.pit.perk.type.boost;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 21:25
 */
public class XPPrestigeBoostPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "XPPrestigeBoost";
    }

    @Override
    public String getDisplayName() {
        return "经验获取提升";
    }

    @Override
    public Material getIcon() {
        return Material.EXP_BOTTLE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    //renown = durability of icon when prestige = 0
    public double requireRenown(int level) {
        if (level == 1) {
            return 5;
        }
        return 10;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int requirePrestige() {
        return 1;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7每次升级:");
        lines.add("  &7击杀获得 &b+1 基础经验值 &7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

}
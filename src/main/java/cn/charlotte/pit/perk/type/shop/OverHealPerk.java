package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/3 21:37
 */
public class OverHealPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "OverHeal";
    }

    @Override
    public String getDisplayName() {
        return "过度医疗";
    }

    @Override
    public Material getIcon() {
        return Material.BREAD;
    }

    @Override
    public double requireCoins() {
        return 6000;
    }

    @Override
    public double requireRenown(int level) {
        return 10;
    }

    @Override
    public int requirePrestige() {
        return 1;
    }

    @Override
    public int requireLevel() {
        return 70;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7携带此天赋时击杀获得的治疗道具最大持有量提升至原来的 &a200% &7.");
        return lines;
    }
}

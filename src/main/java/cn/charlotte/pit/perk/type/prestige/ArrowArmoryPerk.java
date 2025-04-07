package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/6/4 18:48
 */
public class ArrowArmoryPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "arrow_armory_perk";
    }

    @Override
    public String getDisplayName() {
        return "箭库";
    }

    @Override
    public Material getIcon() {
        return Material.ARROW;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 5; //5
    }

    @Override
    public int requirePrestige() {
        return 4;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7每次升级:");
        lines.add("  &7出生时获得的箭矢数量 &f+8 &7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 4;
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
}

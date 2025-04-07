package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/2 17:23
 */
public class RawNumbersPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "raw_numbers_perk";
    }

    @Override
    public String getDisplayName() {
        return "精确计算";
    }

    @Override
    public Material getIcon() {
        return Material.PUMPKIN_SEEDS;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 40;
    }

    @Override
    public int requirePrestige() {
        return 13;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7攻击时显示你造成的确切伤害.");
        lines.add("&7(非普通类型的伤害显示可能不准确)");
        return lines;
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
}

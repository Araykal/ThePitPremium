package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/27 22:12
 */
public class DivineInterventionPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "divine_intervention";
    }

    @Override
    public String getDisplayName() {
        return "奇迹力场";
    }

    @Override
    public Material getIcon() {
        return Material.QUARTZ_STAIRS;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return level >= 2 ? 100 : 50;
    }

    @Override
    public int requirePrestige() {
        return 6;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7死亡时有 &d+5% &7的几率不损失背包内神话物品生命");
    }

    @Override
    public int getMaxLevel() {
        return 3;
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

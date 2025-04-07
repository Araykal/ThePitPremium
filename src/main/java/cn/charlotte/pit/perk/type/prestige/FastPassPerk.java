package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.level.LevelUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/22 18:13
 */
public class FastPassPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "FastPass";
    }

    @Override
    public String getDisplayName() {
        return "高速通道";
    }

    @Override
    public Material getIcon() {
        return Material.ACTIVATOR_RAIL;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 100;
    }

    @Override
    public int requirePrestige() {
        return 10;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7精通后立刻升级等级至 " + LevelUtil.getLevelColor(50) + "50" + " &7级.");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }
}

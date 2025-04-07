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
 * @Created_In: 2021/1/13 20:11
 */
public class ExtraPerkSlotPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "ExtractPerkSlot";
    }

    @Override
    public String getDisplayName() {
        return "额外天赋槽";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_BLOCK;
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
        return 3;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7获得一个在 " + LevelUtil.getLevelColor(100) + "100" + " &7级解锁的天赋槽.");
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

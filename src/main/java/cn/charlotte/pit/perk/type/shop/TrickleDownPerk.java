package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/2 16:58
 */
public class TrickleDownPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "trickle_down_perk";
    }

    @Override
    public String getDisplayName() {
        return "涓流";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_INGOT;
    }

    @Override
    public double requireCoins() {
        return 1000;
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
        return 40;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7捡起金锭时获得的硬币 &6+10 &7,并恢复自身 &c2❤ &7.");
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

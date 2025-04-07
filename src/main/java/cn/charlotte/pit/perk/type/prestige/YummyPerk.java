package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/1 16:51
 */
public class YummyPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "yummy_perk";
    }

    @Override
    public String getDisplayName() {
        return "美味可口";
    }

    @Override
    public Material getIcon() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 15;
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
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7食用金头或金苹果额外获得 &63 硬币&7.");
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

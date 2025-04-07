package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/25 15:23
 */
public class ScamArtistPerk extends AbstractPerk {

    @Override
    public String getInternalPerkName() {
        return "ScamArtist";
    }

    @Override
    public String getDisplayName() {
        return "商场欺诈术";
    }

    @Override
    public Material getIcon() {
        return Material.COOKED_BEEF;
    }

    @Override
    public double requireCoins() {
        return 0;
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
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7每次升级:");
        lines.add("  &7商店与神话之井的硬币花费 &6-5% &7.");
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

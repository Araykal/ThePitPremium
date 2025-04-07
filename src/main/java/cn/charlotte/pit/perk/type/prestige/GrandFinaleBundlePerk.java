package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/17 18:41
 */
public class GrandFinaleBundlePerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "grand_finale_bundle";
    }

    @Override
    public String getDisplayName() {
        return "连杀天赋组合包: 杰作";
    }

    @Override
    public Material getIcon() {
        return Material.NETHER_STAR;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 50;
    }

    @Override
    public int requirePrestige() {
        return 12;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7包括以下连杀天赋:");
        lines.add("&8- &e吸血虫"); //Leech
        lines.add("&8- &e自信一击"); //AssuredStrike
        lines.add("&8- &eRNGesus的使徒"); //Apostle to RNGesus
        lines.add(" ");
        lines.add("&7超级连杀天赋: &e杰作"); //GrandFinale
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
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

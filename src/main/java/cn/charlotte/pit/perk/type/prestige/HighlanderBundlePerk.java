package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/26 16:46
 */
public class HighlanderBundlePerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "highlander_bundle";
    }

    @Override
    public String getDisplayName() {
        return "连杀天赋组合包: 尊贵血统";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_BOOTS;
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
        return 7;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7包括以下连杀天赋:");
        lines.add("&8- &6可汗");
        lines.add("&8- &6纳米黄金工厂");
        lines.add("&8- &6凋灵工艺");
        lines.add(" ");
        lines.add("&7超级连杀天赋: &6尊贵血统");
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

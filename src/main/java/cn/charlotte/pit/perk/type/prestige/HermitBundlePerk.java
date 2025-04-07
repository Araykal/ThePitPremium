package cn.charlotte.pit.perk.type.prestige;


import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/23 18:32
 */
public class HermitBundlePerk extends AbstractPerk {

    @Override
    public String getInternalPerkName() {
        return "hermit_bundle";
    }

    @Override
    public String getDisplayName() {
        return "连杀天赋组合包: 隐士";
    }

    @Override
    public Material getIcon() {
        return Material.BED;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
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
        lines.add("&7包括以下连杀天赋:");
        lines.add("&8- &9臭气弹");
        lines.add("&8- &9保护光环");
        lines.add("&8- &9玻璃稿");
        lines.add("&8- &9冰立方");
        lines.add(" ");
        lines.add("&7超级连杀天赋: &9隐士");
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


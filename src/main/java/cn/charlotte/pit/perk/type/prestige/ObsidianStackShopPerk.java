package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/13 21:04
 */
public class ObsidianStackShopPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "ObsidianStackShopUnlock";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 一组黑曜石";
    }

    @Override
    public Material getIcon() {
        return Material.OBSIDIAN;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 5;
    }

    @Override
    public int requirePrestige() {
        return 2;
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
        lines.add("&7允许你在商店中以优惠价一次性购买一组黑曜石.");
        return lines;
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

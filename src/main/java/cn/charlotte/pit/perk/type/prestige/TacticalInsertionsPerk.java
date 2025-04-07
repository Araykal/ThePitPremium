package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/17 17:06
 */
public class TacticalInsertionsPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "tactical_insertions_perk";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 战术重部署装置";
    }

    @Override
    public Material getIcon() {
        return Material.BLAZE_ROD;
    }

    @Override //100
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 8;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在商店中购买战术重部署装置.");
        lines.add(" ");
        lines.add("&7战术重部署装置会在使用后消耗,");
        lines.add("使你下次在使用此物品时的坐标处复活.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return null;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }
}

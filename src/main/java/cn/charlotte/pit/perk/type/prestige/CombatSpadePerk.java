package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/5 0:08
 */
public class CombatSpadePerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "CombatSpadeShopUnlock";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 战斗之铲";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_SPADE;
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
        return 3;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在商店中购买战斗之铲.");
        lines.add("&7战斗之铲有着+7的基础攻击力,");
        lines.add("&7且攻击目标每穿着一件钻石装备,");
        lines.add("&7战斗之铲的基础攻击力+1.");
        return lines;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
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

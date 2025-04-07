package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/29 19:09
 */
public class JumpBoostShopPerk extends AbstractPerk {

    @Override
    public String getInternalPerkName() {
        return "jump_boost_potion_shop_unlock";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 跳跃提升药水";
    }

    @Override
    public Material getIcon() {
        return Material.POTION;
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
        return 7;
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
        lines.add("&7允许你在商店中购买跳跃提升 IV (00:30)药水.");
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

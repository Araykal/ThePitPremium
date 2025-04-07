package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/9 15:18
 */
public class TheWayPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "the_way_perk";
    }

    @Override
    public String getDisplayName() {
        return "朝圣之路";
    }

    @Override
    public Material getIcon() {
        return Material.JUNGLE_DOOR_ITEM;
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
        return 8;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7装备天赋不再受到等级要求限制. (连杀天赋除外)");
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

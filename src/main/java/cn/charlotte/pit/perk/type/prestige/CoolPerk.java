package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/12 17:41
 */
public class CoolPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "cool_perk";
    }

    @Override
    public String getDisplayName() {
        return "Cool";
    }

    @Override
    public Material getIcon() {
        return Material.BOOK_AND_QUILL;
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
        return 10;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7允许你使用指令 &b/cool &7查看");
        list.add("&7当前房间在线的精通玩家列表.");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 1;
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

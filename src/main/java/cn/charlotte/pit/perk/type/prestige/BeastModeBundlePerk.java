package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BeastModeBundlePerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "beast_mode_bundle";
    }

    @Override
    public String getDisplayName() {
        return "连杀天赋组合包: 野兽模式";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_HELMET;
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
        return 3;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7包括以下连杀天赋:");
        lines.add("&8- &a休养与恢复");
        lines.add("&8- &a坚韧肌肤"); //ToughSkin
        lines.add("&8- &a战术撤退"); //Tactical Retreat
        lines.add("&8- &a怪物"); //Monster
        lines.add(" ");
        lines.add("&7超级连杀天赋: &a野兽模式"); //BeastMode
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

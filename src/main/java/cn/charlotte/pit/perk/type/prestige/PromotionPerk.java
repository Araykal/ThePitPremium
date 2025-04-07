package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PromotionPerk extends AbstractPerk {

    @Override
    public String getInternalPerkName() {
        return "promotion";
    }

    @Override
    public String getDisplayName() {
        return "超进化";
    }

    @Override
    public Material getIcon() {
        return Material.JUNGLE_FENCE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 30;
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
        lines.add("&7使你的 &e连杀助理 &7天赋获得以下额外效果:");
        lines.add("&7死亡时若已激活一个超级连杀,");
        lines.add("&7则保护神话物品不因本次死亡失去生命.");
        return lines;
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

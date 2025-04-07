package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AssistantToTheStreakerPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "assistant_to_the_streaker";
    }

    @Override
    public String getDisplayName() {
        return "连杀助理";
    }

    @Override
    public Material getIcon() {
        return Material.BIRCH_FENCE;
    }

    @Override
    public double requireCoins() {
        return 8000;
    }

    @Override
    public double requireRenown(int level) {
        return 15;
    }

    @Override
    public int requirePrestige() {
        return 5;
    }

    @Override
    public int requireLevel() {
        return 50;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7助攻时按照造成的伤害比例,获得连杀数.");
        if (PlayerUtil.isPlayerUnlockedPerk(player, "promotion")) {
            lines.add("&7且死亡时若已激活一个超级连杀,");
            lines.add("&7则保护神话物品不因本次死亡失去生命.");
        }
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

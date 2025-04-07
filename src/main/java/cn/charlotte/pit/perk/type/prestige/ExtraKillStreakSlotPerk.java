package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/6/4 19:02
 */
public class ExtraKillStreakSlotPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "extra_kill_streak_slot_perk";
    }

    @Override
    public String getDisplayName() {
        return "额外连杀天赋槽";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_BLOCK;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 50; //50
    }

    @Override
    public int requirePrestige() {
        return 5;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7获得一个额外的连杀天赋槽. ");
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

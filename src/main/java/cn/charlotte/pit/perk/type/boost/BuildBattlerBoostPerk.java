package cn.charlotte.pit.perk.type.boost;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/11 22:11
 */
public class BuildBattlerBoostPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "BuilderBattleBoost";
    }

    @Override
    public String getDisplayName() {
        return "方块存在时间提升";
    }

    @Override
    public Material getIcon() {
        return Material.getMaterial(351);
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    //renown = durability of icon when prestige = 0
    public double requireRenown(int level) {
        return 15;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7每次升级:");
        lines.add("  &7放置的方块存在时间 &f+60% &7.");
        lines.add("  &7当前: &f+" + 60 * (profile.getChosePerk().get(-6) == null ? 0 : profile.getChosePerk().get(-6).getLevel()) + "% 方块存在时间");
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

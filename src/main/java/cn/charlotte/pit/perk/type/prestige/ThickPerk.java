package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/9 15:24
 */
public class ThickPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "thick_perk";
    }

    @Override
    public String getDisplayName() {
        return "厚重";
    }

    @Override
    public Material getIcon() {
        return Material.APPLE;
    }

    @Override
    public double requireCoins() {
        return 10000;
    }

    @Override
    public double requireRenown(int level) {
        return 45;
    }

    @Override
    public int requirePrestige() {
        return 10;
    }

    @Override
    public int requireLevel() {
        return 90;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7携带此天赋时你的生命上限 &c+2❤ &7.");
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
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().put(getInternalPerkName(), 4.0);
        player.setMaxHealth(profile.getMaxHealth());
    }

    @Override
    public void onPerkInactive(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().remove(getInternalPerkName());
        player.setMaxHealth(profile.getMaxHealth());
    }
}

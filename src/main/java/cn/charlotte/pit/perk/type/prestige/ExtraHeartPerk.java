package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/6/4 18:39
 */
public class ExtraHeartPerk extends AbstractPerk {

    @Override
    public String getInternalPerkName() {
        return "extra_heart_perm_perk";
    }

    @Override
    public String getDisplayName() {
        return "额外生命值";
    }

    @Override
    public Material getIcon() {
        return Material.APPLE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return level == 1 ? 20 : 100;//level == 1 ? 20 : 100
    }

    @Override
    public int requirePrestige() {
        return 4;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7每次升级:");
        lines.add("  &7自身生命值上限 &c+1❤ &7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 2;
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

    @Override
    public void onUnlock(Player player) {
        super.onUnlock(player);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().put(getInternalPerkName(), 2.0);
        player.setMaxHealth(profile.getMaxHealth());
    }

    @Override
    public void onUpgrade(Player player) {
        super.onUpgrade(player);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().put(getInternalPerkName(), 4.0);
        player.setMaxHealth(profile.getMaxHealth());
    }
}

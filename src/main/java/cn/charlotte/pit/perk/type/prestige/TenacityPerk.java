package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/10 15:13
 */
@Passive
public class TenacityPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "Tenacity";
    }

    @Override
    public String getDisplayName() {
        return "坚韧";
    }

    @Override
    public Material getIcon() {
        return Material.SPIDER_EYE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        switch (level) {
            case 1:
                return 10;
            case 2:
                return 50;
            default:
                return 999;
        }
    }

    @Override
    public int requirePrestige() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
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
        lines.add("  &7击杀恢复自身 &c+0.5❤ &7生命.");
        //lines.add("  &7当前: &b+" + 10 * (profile.getChosePerk().get(-1) == null ? 0 : profile.getChosePerk().get(-1).getLevel()) + "% 经验值");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        PerkData data = profile.getUnlockedPerkMap().get(getInternalPerkName());
        if (data != null) {
            PlayerUtil.heal(myself, data.getLevel());
        }
    }
}

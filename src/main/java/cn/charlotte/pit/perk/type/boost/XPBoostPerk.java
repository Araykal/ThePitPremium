package cn.charlotte.pit.perk.type.boost;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerAssist;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/7 15:20
 */
public class XPBoostPerk extends AbstractPerk implements IPlayerKilledEntity, IPlayerAssist {
    @Override
    public String getInternalPerkName() {
        return "XPBoost";
    }

    @Override
    public String getDisplayName() {
        return "经验获取提升";
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
        return 12;
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
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7每次升级:");
        lines.add("  &7击杀/助攻 获得的经验值 &b+10% &7.");
        lines.add("  &7当前: &b+" + 10 * (profile.getChosePerk().get(-1) == null ? 0 : profile.getChosePerk().get(-1).getLevel()) + "% 经验值");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue().getPerkInternalName().equals(this.getInternalPerkName())) {
                experience.set((1 + 0.1 * entry.getValue().getLevel()) * experience.get());
                break;
            }
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue().getPerkInternalName().equals(this.getInternalPerkName())) {
                experience.set((1 + 0.1 * entry.getValue().getLevel()) * experience.get());
                break;
            }
        }
    }

}

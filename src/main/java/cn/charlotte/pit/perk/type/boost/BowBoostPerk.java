package cn.charlotte.pit.perk.type.boost;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/7 18:11
 */
public class BowBoostPerk extends AbstractPerk implements IPlayerShootEntity {
    @Override
    public String getInternalPerkName() {
        return "ArrowBoost";
    }

    @Override
    public String getDisplayName() {
        return "远程伤害提升";
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
        return 11;
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
        lines.add("  &7弓箭攻击造成的伤害 &c+3% &7.");
        lines.add("  &7当前: &c+" + 3 * (profile.getChosePerk().get(-4) == null ? 0 : profile.getChosePerk().get(-4).getLevel()) + "% 弓箭伤害");
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

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue().getPerkInternalName().equals(this.getInternalPerkName())) {
                boostDamage.set(boostDamage.get() + 0.03 * entry.getValue().getLevel());
                break;
            }
        }
    }
}

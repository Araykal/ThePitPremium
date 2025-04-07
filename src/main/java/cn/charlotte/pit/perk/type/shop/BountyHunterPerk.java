package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.type.BowOnly;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.Utils;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/2 17:43
 */
public class BountyHunterPerk extends AbstractPerk implements IAttackEntity, IPlayerShootEntity, IPlayerKilledEntity {

    @Override
    public String getInternalPerkName() {
        return "BountyHunter";
    }

    @Override
    public String getDisplayName() {
        return "赏金猎人";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_LEGGINGS;
    }

    @Override
    public double requireCoins() {
        return 2000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 65;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7击杀获得的硬币 &6+4g &7.");
        lines.add("&7攻击被悬赏的玩家时,");
        lines.add("&7此玩家每有 &6&l50g &7赏金,");
        lines.add("&7你造成的伤害 &a+1% &7.");
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
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            final int level = Utils.getEnchantLevel(targetPlayer.getInventory().getLeggings(), "hunt_the_hunter");
            if (targetPlayer.getInventory().getLeggings() != null && level > 0) {
                if (level == 1) {
                    boostDamage.set(boostDamage.get() + 0.005 * (0.5 / 50) * PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId()).getBounty());
                }
                return;
            }
            boostDamage.set(boostDamage.get() + 0.01 * (0.5 / 50) * PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId()).getBounty());
        }
    }

    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        coins.set(coins.get() + 4);
    }

    @Override
    @BowOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            final int level = Utils.getEnchantLevel(targetPlayer.getInventory().getLeggings(), "hunt_the_hunter");
            if (targetPlayer.getInventory().getLeggings() != null && level > 0) {
                if (level == 1) {
                    boostDamage.set(boostDamage.get() + 0.005 * (0.5 / 50) * PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId()).getBounty());
                }
                return;
            }
            boostDamage.set(boostDamage.get() + 0.01 * (0.5 / 50) * PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId()).getBounty());
        }
    }
}

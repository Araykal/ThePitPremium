package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 21:27
 */
@WeaponOnly
@BowOnly
public class SharkEnchant extends AbstractEnchantment implements IAttackEntity {
    public static float getDistance(final Location lc1, final Location lc2) {
        return (float) Math.sqrt(Math.pow(lc1.getX() - lc2.getX(), 2.0) + Math.pow(lc1.getY() - lc2.getY(), 2.0) + Math.pow(lc1.getZ() - lc2.getZ(), 2.0));
    }

    @Override
    public String getEnchantName() {
        return "鲸鲨";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "shark";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        int boostRate = 0;
        switch (enchantLevel) {
            case 1:
                boostRate = 2;
                break;
            case 2:
                boostRate = 4;
                break;
            case 3:
                boostRate = 7;
                break;
        }
        return "&7以你为中心12格内,每有玩家生命值低于 &c6❤ &7,你造成的伤害 &c+" + boostRate + "% &7(最高30%)";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        double boostCount = 0;
        for (Player p : Bukkit.getOnlinePlayers()
        ) {
            if (attacker.getWorld().getName().equals(p.getWorld().getName())) {
                if (getDistance(p.getLocation(), attacker.getLocation()) <= 12) {
                    boostCount++;
                }
            }
        }
        double boostRate = 0;
        switch (enchantLevel) {
            case 1:
                boostRate = 2;
                break;
            case 2:
                boostRate = 4;
                break;
            case 3:
                boostRate = 7;
                break;
        }
        boostRate = boostRate * boostCount * 0.01;
        boostDamage.set(boostDamage.get() + Math.min(0.30, boostRate));
    }
}

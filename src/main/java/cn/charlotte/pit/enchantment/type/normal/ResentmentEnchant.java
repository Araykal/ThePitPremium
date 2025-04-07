package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 21:19
 */
@WeaponOnly
@BowOnly
public class ResentmentEnchant extends AbstractEnchantment implements IAttackEntity {
    @Override
    public String getEnchantName() {
        return "积怨";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "resentment";
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
                boostRate = 3;
                break;
            case 3:
                boostRate = 5;
        }
        return "&7生命值每低于最大生命值 &c1❤ &7,你的攻击力 &c+" + boostRate + "% &7(最高30%)";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        double boostRate = 0;
        switch (enchantLevel) {
            case 1:
                boostRate = 2;
                break;
            case 2:
                boostRate = 3;
                break;
            case 3:
                boostRate = 5;
        }
        boostRate = boostRate * Math.round(attacker.getMaxHealth() - attacker.getHealth());
        if (boostRate >= 30 * 2) {
            boostRate = 30 * 2;
        }
        boostDamage.set(boostDamage.get() + 0.01 * 0.5 * boostRate);
    }
}

package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
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
 * @Author: EmptyIrony
 * @Date: 2020/12/29 23:07
 */

@WeaponOnly
@BowOnly
public class ReaperEnchant extends AbstractEnchantment implements IAttackEntity {
    @Override
    public String getEnchantName() {
        return "收割者";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "reaper";
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
        int i = 5 + 5 * enchantLevel;
        return "&7攻击生命值低于最大生命值 &c50% &7的敌人造成伤害 &c+" + i + "%";
    }


    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getHealth() <= (targetPlayer.getMaxHealth() / 2)) {
            int damageBoost = 5 + 5 * enchantLevel;
            boostDamage.set(boostDamage.get() + damageBoost * 0.01);
        }
    }
}

package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/17 18:18
 */
@WeaponOnly
@BowOnly
public class AntiMythicismEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "反神话力场";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "anti_mythicism";
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
        return "&7攻击穿着 &6神话之甲 &7的玩家造成的伤害 &c+" + (4 * enchantLevel) + "%";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player victim = (Player) target;
        if (victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.LEATHER_LEGGINGS)) {
            boostDamage.set(boostDamage.get() + (0.04 * enchantLevel));
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player victim = (Player) target;
        if (victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.LEATHER_LEGGINGS)) {
            boostDamage.set(boostDamage.get() + (0.04 * enchantLevel));
        }
    }
}

package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/27 14:34
 */
@WeaponOnly
public class SpeedyHitEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "迅捷打击";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "SpeedyHit";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return new Cooldown(5, TimeUnit.SECONDS);
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7攻击玩家时获得 &b速度 I &7(00:0" + (3 + enchantLevel * 2) + ")" +
                "/s&7此附魔每 &f5 &7秒只能触发一次.";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        attacker.removePotionEffect(PotionEffectType.SPEED);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * (3 + enchantLevel * 2), 0), false);
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        attacker.removePotionEffect(PotionEffectType.SPEED);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * (3 + enchantLevel * 2), 0), false);
    }
}

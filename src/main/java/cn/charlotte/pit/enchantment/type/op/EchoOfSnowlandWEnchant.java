package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/19 14:57
 */
@WeaponOnly
public class EchoOfSnowlandWEnchant extends AbstractEnchantment implements IAttackEntity {
    @Override
    public String getEnchantName() {
        return "雪原的回响";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "Echo_Of_Snowland_Weapon";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.OP;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7每 &e3 &7次攻击玩家时对目标&7施加以下效果:/s  &f▶ &c缓慢 II &7(00:10)/s  &f▶ &c虚弱 I &7(00:10)/s&7攻击带有 &c缓慢 &7与 &c虚弱 &7效果的玩家/s&7造成的伤害 &c+120% &7且额外造成 &c1❤ &f真实&7伤害";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeAttack() % 3 == 0) {
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1), true);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 0), true);

            boostDamage.set(boostDamage.get() + 1.2);
            finalDamage.set(finalDamage.get() + 2);
        }
    }
}

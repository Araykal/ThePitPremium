package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/19 15:04
 */
@ArmorOnly
public class EchoOfSnowlandPEnchant extends AbstractEnchantment implements IPlayerDamaged {
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
        return "Echo_Of_Snowl and_Pants";
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
        return "&7受到带有 &c缓慢 &7及 &c虚弱 &7效果的玩家攻击时/s&7受到的普通伤害 &a-30% &7且免疫其造成的&f真实&7伤害.";
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) attacker;
        if (targetPlayer.getActivePotionEffects().contains(PotionEffectType.WEAKNESS) && targetPlayer.getActivePotionEffects().contains(PotionEffectType.SLOW)) {
            boostDamage.set(boostDamage.get() - 0.3);
            finalDamage.set(0);
        }
    }
}

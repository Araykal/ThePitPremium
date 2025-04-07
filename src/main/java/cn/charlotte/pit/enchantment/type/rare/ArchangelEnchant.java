package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 14:14
 */
@BowOnly
public class ArchangelEnchant extends AbstractEnchantment implements IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "大天使";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 4;
    }

    @Override
    public String getNbtName() {
        return "archangel";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NOSTALGIA_RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }


    @Override
    public String getUsefulnessLore(int enchantLevel) {
        double recoverHealth = 0.5 * enchantLevel;
        return "&7手持时被弓箭击中恢复自身 &c" + recoverHealth + "❤";
    }


    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker instanceof Arrow) {
            double newHeal = myself.getHealth() + 0.5 * enchantLevel;
            myself.setHealth(Math.min(newHeal, myself.getMaxHealth()));
        }
    }
}

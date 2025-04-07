package cn.charlotte.pit.enchantment.type.genesis;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/16 19:53
 */
public class GuardianEnchant extends AbstractEnchantment implements IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "守卫";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "guardian_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.GENESIS;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    public int getProtectionValue(int enchantLevel) {
        switch (enchantLevel) {
            case 2:
                return 12;
            case 3:
                return 15;
            default:
                return 10;
        }
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7受到的伤害 &9-" + getProtectionValue(enchantLevel) + "%";
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        boostDamage.getAndAdd(-0.01 * getProtectionValue(enchantLevel));
    }
}

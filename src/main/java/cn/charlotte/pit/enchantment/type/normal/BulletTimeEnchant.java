package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.NotPlayerOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/18 19:30
 */
@WeaponOnly
public class BulletTimeEnchant extends AbstractEnchantment implements IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "子弹时间";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "BulletTime";
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
        return "&7使用剑格挡时免疫弓箭伤害" + (enchantLevel > 1 ? "/s且格挡弓箭时恢复自身 &c" + (0.5 * enchantLevel) + "❤" : "");
    }

    @Override
    @NotPlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker instanceof Arrow && myself.isBlocking()) {
            cancel.set(true);
            myself.getLocation().getWorld().playEffect(myself.getLocation(), Effect.SMOKE, 1, 1);
            myself.playSound(myself.getLocation(), Sound.ZOMBIE_WOODBREAK, 2f, 2f);
            if (enchantLevel > 1) {
                PlayerUtil.heal(myself, enchantLevel);
            }
        }
    }
}

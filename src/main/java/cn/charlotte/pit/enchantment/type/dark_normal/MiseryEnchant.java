package cn.charlotte.pit.enchantment.type.dark_normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/15 19:51
 */
@ArmorOnly
public class MiseryEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "苦难";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "misery_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.DARK_NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7攻击穿有 &6神话之甲 &7的玩家时造成额外 &c0.5❤ &7的&f真实&7伤害,但每次使用扣除自身 &c0.3❤ &7生命值.";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            PlayerUtil.damage(attacker, targetPlayer, PlayerUtil.DamageType.TRUE, 1D, true);
            PlayerUtil.damage(targetPlayer, PlayerUtil.DamageType.TRUE, 0.6D, false);
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            PlayerUtil.damage(attacker, targetPlayer, PlayerUtil.DamageType.TRUE, 1D, true);
            PlayerUtil.damage(targetPlayer, PlayerUtil.DamageType.TRUE, 0.6D, false);
        }
    }
}

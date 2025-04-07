package cn.charlotte.pit.enchantment.type.dark_normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/15 20:02
 */
@ArmorOnly
public class SpiteEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "怨恨";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "spite_enchant";
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
        return "&7攻击穿戴 &6皮革装备 &7的玩家造成的伤害 &c+20% &7,但同时你受到来自其的伤害 &c+5%";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            boostDamage.getAndAdd(0.2);
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            boostDamage.getAndAdd(0.2);
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) attacker;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            boostDamage.getAndAdd(0.05);
        }
    }
}

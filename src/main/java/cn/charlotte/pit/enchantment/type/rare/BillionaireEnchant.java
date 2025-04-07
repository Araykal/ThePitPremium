package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/27 14:39
 */
@WeaponOnly
public class BillionaireEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity {

    @Override
    public String getEnchantName() {
        return "亿万富翁";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "billionaire";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7攻击造成的伤害 &c+" + (35 * enchantLevel) + "% /s&7但每次使用扣除自身 &6" + (enchantLevel * 100 + (enchantLevel >= 3 ? 50 : 0)) + " 硬币";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());

        if (profile.getCoins() >= enchantLevel * 100) {
            boostDamage.getAndAdd(enchantLevel * 0.35);
            profile.setCoins(profile.getCoins() - enchantLevel * 100 - (enchantLevel >= 3 ? 50 : 0));
        }
    }

    @Override
    @cn.charlotte.pit.parm.type.BowOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());

        if (profile.getCoins() >= enchantLevel * 100) {
            boostDamage.getAndAdd(enchantLevel * 0.35);
            profile.setCoins(profile.getCoins() - enchantLevel * 100 - (enchantLevel >= 3 ? 50 : 0));
        }
    }
}

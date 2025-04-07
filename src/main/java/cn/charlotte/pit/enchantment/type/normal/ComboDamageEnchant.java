package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
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
 * @Created_In: 2021/1/18 17:33
 */
@WeaponOnly
@BowOnly
public class ComboDamageEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {
    @Override
    public String getEnchantName() {
        return "强力击";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "Combo_Damage";
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
        return "&7每 &e" + (enchantLevel == 1 ? 4 : 3) + " &7次击中造成的伤害 &c+" + (enchantLevel * 10 + 10) + "%";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % (enchantLevel == 1 ? 4 : 3) == 0) {
            boostDamage.set(boostDamage.get() + (enchantLevel * 0.1 + 0.1));
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getBowHit() % (enchantLevel == 1 ? 4 : 3) == 0) {
            boostDamage.set(boostDamage.get() + (enchantLevel * 0.1 + 0.1));
        }
    }

    @Override
    public String getText(int level, Player player) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        return (hit % (level == 1 ? 4 : 3) == 0 ? "&a&l✔" : "&e&l" + ((level == 1 ? 4 : 3) - hit % (level == 1 ? 4 : 3)));
    }
}

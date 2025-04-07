package cn.charlotte.pit.enchantment.type.dark_rare;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
@ArmorOnly
@AutoRegister
public class ComboDazzlingGoldEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {
    @Override
    public String getEnchantName() {
        return "强力击: 耀金";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "dazzlinggold_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.DARK_RARE;
    }

    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7每 &e5 &7次攻击目标时, 将立刻恢复 &c1.0❤ &7生命值并获得 &b速度 II &f(00:05)";
    }

    public String getText(int a, Player player) {
        a = player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW
                ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit()
                : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit();
        return a % 5 == 0 ? "&a&l✔" : new StringBuilder().insert(0, "&e&l").append(5 - a % 5).toString();
    }

    @PlayerOnly
    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % 5 == 0) {
            PlayerUtil.heal(attacker, 2.0);
            attacker.removePotionEffect(PotionEffectType.SPEED);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
        }
    }

    @PlayerOnly
    @BowOnly

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % 5 == 0) {
            PlayerUtil.heal(attacker, 2.0);
            attacker.removePotionEffect(PotionEffectType.SPEED);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
        }

    }
}

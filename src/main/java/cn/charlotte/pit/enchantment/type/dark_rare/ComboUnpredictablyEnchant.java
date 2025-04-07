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

public class ComboUnpredictablyEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {
    @Override
    public String getEnchantName() {
        return "强力击: 紊乱";
    }


    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "unpredictably_enchant";
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
        return "&7每 &e5 &7次攻击对目标玩家施加 &c缓慢 II &f(00:07) &7与 &c虚弱 IV &f(00:07) &7效果.";
    }

    @Override

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
            Player targetPlayer = (Player)target;
            targetPlayer.removePotionEffect(PotionEffectType.SLOW);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 1, true));
            targetPlayer.removePotionEffect(PotionEffectType.WEAKNESS);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 280, 3), true);
        }

    }
    @PlayerOnly
    @BowOnly

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % 5 == 0) {
            Player targetPlayer = (Player)target;
            targetPlayer.removePotionEffect(PotionEffectType.SLOW);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 1, true));
            targetPlayer.removePotionEffect(PotionEffectType.WEAKNESS);
            targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 280, 3), true);
        }

    }
}

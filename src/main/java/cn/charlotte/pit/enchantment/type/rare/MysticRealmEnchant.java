package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

@ArmorOnly
public class MysticRealmEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, ITickTask {
    @Override
    public String getEnchantName() {
        return "神秘领域";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "mysticrealm_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return (new StringBuilder()).insert(0, "&7穿戴时每 &e5 &7秒对以你为中心 &b").append(enchantLevel * 3).append(" &7格内所有/s玩家施加 &8缓慢 I &f(00:04) &7效果, 同时, 使你攻击/s带有 &8缓慢 &7效果的玩家造成的伤害 &c+").append(enchantLevel * 4).append("%").toString();
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (((Player)target).hasPotionEffect(PotionEffectType.SLOW))
            boostDamage.getAndAdd(enchantLevel * 0.04D);
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (((Player)target).hasPotionEffect(PotionEffectType.SLOW))
            boostDamage.getAndAdd(enchantLevel * 0.04D);
    }

    public void handle(int enchantLevel, Player player) {
        if (player.getWorld().getNearbyEntities(player.getLocation(), enchantLevel * 3, enchantLevel * 3, enchantLevel * 3) != null) {
            for (Entity r : player.getWorld().getNearbyEntities(player.getLocation(), enchantLevel * 3, enchantLevel * 3, enchantLevel * 3)) {
                if (!(r instanceof Player)) continue;
                Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                    ((Player)r).removePotionEffect(PotionEffectType.SLOW);
                    ((Player)r).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
                });
            }
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 100;
    }
}

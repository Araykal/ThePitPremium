package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/26 13:36
 */
public class CounterOffensiveEnchant extends AbstractEnchantment implements IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "反恐";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "counter_offensive_enchant";
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
        return "&7每被同一名玩家攻击 &e" + (5 - enchantLevel) + " &7次,自身获得 &b速度 II &f(" + TimeUtil.millisToTimer((2L * enchantLevel + 1) * 1000) + ")";
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) attacker;
        if (!myself.hasMetadata("counter_enchant_" + targetPlayer.getUniqueId())) {
            myself.setMetadata("counter_enchant_" + targetPlayer.getUniqueId(), new FixedMetadataValue(ThePit.getInstance(), 1));
        } else {
            int count = myself.getMetadata("counter_enchant_" + targetPlayer.getUniqueId()).get(0).asInt();
            if (count + 1 >= 5 - enchantLevel) {
                count = -1;
                myself.removePotionEffect(PotionEffectType.SPEED);
                myself.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * (2 * enchantLevel + 1), 1, true));
            }
            myself.setMetadata("counter_enchant_" + targetPlayer.getUniqueId(), new FixedMetadataValue(ThePit.getInstance(), count + 1));
        }
    }
}

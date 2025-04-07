package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/9 21:57
 */
@ArmorOnly
public class CriticallyFunkyEnchant extends AbstractEnchantment implements IPlayerDamaged, IAttackEntity, IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "致命节奏";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "critically_funky_enchant";
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
        return "&7受到玩家的暴击伤害 &9-" + (5 + enchantLevel * 15) + "%" + (enchantLevel >= 2 ?
                ("/s&7并使你下次攻击造成的伤害 &c+" + (enchantLevel * 15 - 15) + "% &7(不可叠加)") : "");
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.hasMetadata("funky_strike_hit")) {
            int level = attacker.getMetadata("funky_strike_hit").get(0).asInt();
            attacker.removeMetadata("funky_strike_hit", ThePit.getInstance());
            boostDamage.getAndAdd(0.15 * level - 0.15);
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player attackerPlayer = (Player) attacker;
        if (PlayerUtil.isCritical(attackerPlayer)) {
            boostDamage.getAndAdd(-0.05 - 0.15 * enchantLevel);
            if (enchantLevel >= 2) {
                myself.setMetadata("funky_strike_hit", new FixedMetadataValue(ThePit.getInstance(), enchantLevel));
            }
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.hasMetadata("funky_strike_hit")) {
            int level = attacker.getMetadata("funky_strike_hit").get(0).asInt();
            attacker.removeMetadata("funky_strike_hit", ThePit.getInstance());
            boostDamage.getAndAdd(0.15 * level - 0.15);
        }
    }
}

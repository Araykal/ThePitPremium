package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.rank.RankUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/23 13:40
 */
@BowOnly
public class MixedCombatEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {

    private static final HashMap<UUID, UUID> aim = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "灵活战术";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "mixed_combat_enchant";
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
        return "&7弓箭命中玩家时锁定该玩家,"
                + "/s&7近战命中锁定目标时造成的伤害 &c+" + (enchantLevel * 10) + "% &7并解除锁定状态";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (aim.get(attacker.getUniqueId()) != null) {
            Player targetPlayer = (Player) target;
            if (aim.get(attacker.getUniqueId()).equals(targetPlayer.getUniqueId())) {
                boostDamage.getAndAdd(0.1 * enchantLevel);
                aim.remove(attacker.getUniqueId());
            }
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        aim.put(attacker.getUniqueId(), targetPlayer.getUniqueId());
    }

    @Override
    public String getText(int level, Player player) {
        return aim.get(player.getUniqueId()) == null ? "&a&l✔" : "&e&l" + CC.stripColor(RankUtil.getPlayerColoredName(aim.get(player.getUniqueId())));
    }
}

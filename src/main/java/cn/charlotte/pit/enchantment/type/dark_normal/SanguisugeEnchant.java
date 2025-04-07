package cn.charlotte.pit.enchantment.type.dark_normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
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

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/15 20:05
 */
@ArmorOnly
public class SanguisugeEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "血";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "sanguisuge_enchant";
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
        return "&7攻击穿戴 &6神话之甲 &7的玩家时恢复 &c0.5❤ &7(2秒冷却),自身装备天赋 &c吸血鬼 &7时回复量翻倍.";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
                cooldown.put(attacker.getUniqueId(), new Cooldown(2, TimeUnit.SECONDS));
                PlayerUtil.heal(attacker, PlayerUtil.isPlayerChosePerk(attacker, "Vampire") ? 2 : 1);
            }
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && "mythic_leggings".equals(ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings()))) {
            if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
                cooldown.put(attacker.getUniqueId(), new Cooldown(2, TimeUnit.SECONDS));
                PlayerUtil.heal(attacker, PlayerUtil.isPlayerChosePerk(attacker, "Vampire") ? 2 : 1);
            }
        }
    }

    @Override
    public String getText(int level, Player player) {
        return getCooldownActionText(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)));
    }
}

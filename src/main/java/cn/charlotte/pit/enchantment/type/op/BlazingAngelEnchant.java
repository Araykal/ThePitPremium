package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/9 19:14
 */
@WeaponOnly
@BowOnly
public class BlazingAngelEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    public static int getCooldownInt(int enchantLevel) {
        switch (enchantLevel) {
            case 2:
                return 8;
            case 3:
                return 5;
            case 4:
                return 3;
            default:
                return 10;
        }
    }

    @Override
    public String getEnchantName() {
        return "炽天使";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "blazing_angel_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NOSTALGIA_RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7击中玩家时点燃玩家 &c3 &7秒 (" + getCooldownInt(enchantLevel) + "秒冷却)";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            targetPlayer.setFireTicks(60);
            cooldown.put(attacker.getUniqueId(), new Cooldown(getCooldownInt(enchantLevel), TimeUnit.SECONDS));
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            targetPlayer.setFireTicks(60);
            cooldown.put(attacker.getUniqueId(), new Cooldown(getCooldownInt(enchantLevel), TimeUnit.SECONDS));
        }
    }

    @Override
    public String getText(int level, Player player) {
        return (cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).getRemaining()).replace(" ", ""));
    }
}

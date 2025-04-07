package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/25 22:19
 */
@ArmorOnly
public class PeroxideEnchant extends AbstractEnchantment implements IPlayerDamaged, IActionDisplayEnchant {

    public static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "过氧化物";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "Peroxide";
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
        return "&7受到攻击时恢复自身 &c" + getHeal(enchantLevel) + "❤ &7生命值 &7(1.5秒冷却)";
    }
    private double getHeal(int enchantLevel) {
        switch (enchantLevel) {
            case 1:
                return 0.5;
            case 2:
                return 0.7;
            case 3:
                return 1.0;
            default:
                return 0;
        }
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        UUID uuid = myself.getUniqueId();
        if (!cooldown.containsKey(uuid)) {
            cooldown.put(uuid, new Cooldown(0));
        }
        if (cooldown.get(uuid).hasExpired()) {
            cooldown.put(uuid, new Cooldown(1500, TimeUnit.MILLISECONDS));
            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                PlayerUtil.heal(myself, getHeal(enchantLevel) * 2);
            }, 2);
        }
    }


    @Override
    public String getText(int level, Player player) {
        return this.getCooldownActionText(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)));
    }
}

package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
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
 * @Created_In: 2021/3/23 19:21
 */
@ArmorOnly
@WeaponOnly
@BowOnly
public class StrikeGoldEnchant extends AbstractEnchantment implements IAttackEntity, IActionDisplayEnchant, IPlayerShootEntity {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "致富之路";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "strike_gold_enchant";
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
        return "&7击中玩家时获得 &6" + enchantLevel + " 硬币 &7(1秒冷却)";
    }

    @Override
    public String getText(int level, Player player) {
        return (cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).getRemaining()).replace(" ", ""));
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            cooldown.put(attacker.getUniqueId(), new Cooldown(1, TimeUnit.SECONDS));
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
            profile.setCoins(profile.getCoins() + enchantLevel);
        }
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            cooldown.put(attacker.getUniqueId(), new Cooldown(1, TimeUnit.SECONDS));
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
            profile.setCoins(profile.getCoins() + enchantLevel);
        }
    }
}

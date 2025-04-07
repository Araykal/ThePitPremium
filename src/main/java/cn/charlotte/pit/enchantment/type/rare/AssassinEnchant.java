package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/25 21:49
 */
@ArmorOnly
public class AssassinEnchant extends AbstractEnchantment implements IPlayerDamaged, IActionDisplayEnchant {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "暗影步伐";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "assassin_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    public int getCooldownInt(int enchantLevel) {
        switch (enchantLevel) {
            case 2:
                return 5;
            case 3:
                return 3;
            default:
                return 10;
        }
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) { //10 5 3
        return "&7蹲下状态时受到攻击会将你传送至攻击者处 (" + getCooldownInt(enchantLevel) + "秒冷却)";
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (myself == attacker) return;

        cooldown.putIfAbsent(myself.getUniqueId(), new Cooldown(0));
        if (myself.isSneaking() && cooldown.get(myself.getUniqueId()).hasExpired()) {
            Location location = attacker.getLocation();
            Location newLoc = location.add(attacker.getLocation().getDirection().clone().multiply(-1.5));

            myself.teleport(newLoc);
            cooldown.put(myself.getUniqueId(), new Cooldown(getCooldownInt(enchantLevel), TimeUnit.SECONDS));
        }
    }

    @Override
    public String getText(int level, Player player) {
        return cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.get(player.getUniqueId()).getRemaining()).replace(" ", "");
    }
}

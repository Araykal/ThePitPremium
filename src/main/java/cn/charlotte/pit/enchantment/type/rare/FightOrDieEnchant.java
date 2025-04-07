package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/17 21:58
 */
@ArmorOnly
public class FightOrDieEnchant extends AbstractEnchantment implements IPlayerDamaged, IActionDisplayEnchant {
    private final Map<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "破釜沉舟";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "FightOrDie";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return new Cooldown(10, TimeUnit.SECONDS);
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7生命值低于 &c3❤ &7时受到伤害时获得以下效果:/s  &f▶ &3抗性提升 " + RomanUtil.convert(enchantLevel) + " &7(00:03)/s&7此附魔每 &f10 &7秒只能触发一次.";
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        cooldown.putIfAbsent(myself.getUniqueId(), new Cooldown(0));
        if (myself.getHealth() <= 6 && cooldown.get(myself.getUniqueId()).hasExpired()) {
            cooldown.put(attacker.getUniqueId(), getCooldown());
            myself.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            myself.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, enchantLevel - 1), true);
        }
    }

    @Override
    public String getText(int level, Player player) {
        return getCooldownActionText(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)));
    }
}

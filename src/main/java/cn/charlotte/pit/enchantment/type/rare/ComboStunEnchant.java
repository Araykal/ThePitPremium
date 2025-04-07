package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/27 17:01
 */
@WeaponOnly
public class ComboStunEnchant extends AbstractEnchantment implements IAttackEntity, IActionDisplayEnchant {

    private final DecimalFormat numFormat = new DecimalFormat("0.0");
    private final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "强力击: 失衡";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "combo_stun_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7每 &e5 &7次攻击对敌人施加 &4失衡 &7效果 (持续" + numFormat.format(0.4 * enchantLevel) + "秒) (8秒冷却)"
                + "/s&7效果 &4失衡 &7: 失明,移动速度与攻击力极大幅度降低";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player victim = (Player) target;
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired() && PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % 5 == 0) {
            cooldown.put(attacker.getUniqueId(), new Cooldown(8, TimeUnit.SECONDS));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8 * enchantLevel, 20));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8 * enchantLevel, 20));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 8 * enchantLevel, 20));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 8 * enchantLevel, 20));
        }
    }

    @Override
    public String getText(int level, Player player) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        int require = 5;
        if (cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired()) {
            return (hit % require == 0 ? "&a&l✔" : "&e&l" + (require - hit % require));
        } else {
            return getCooldownActionText(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)));
        }
    }
}

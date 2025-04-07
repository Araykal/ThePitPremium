package cn.charlotte.pit.enchantment.type.normal;

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

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @Author: Misoryan
 * @Created_In: 2021/2/8 13:37
 */
@WeaponOnly
public class ComboSwiftEnchant extends AbstractEnchantment implements IAttackEntity, IActionDisplayEnchant {
    @Override
    public String getEnchantName() {
        return "强力击: 加速";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "combo_swift_enchant";
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
        return "&7每 &e" + (enchantLevel >= 2 ? 3 : 4) + " &7次攻击获得 &b速度 " + (enchantLevel >= 2 ? "II" : "I") + " &f(00:0" + (enchantLevel + 2) + ")";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % (enchantLevel >= 2 ? 3 : 4) == 0) {
            attacker.removePotionEffect(PotionEffectType.SPEED);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * (enchantLevel + 2), (enchantLevel >= 2 ? 1 : 0)), true);
        }
    }

    @Override
    public String getText(int level, Player player) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        int require = level >= 2 ? 3 : 4;
        return (hit % require == 0 ? "&a&l✔" : "&e&l" + (require - hit % require));
    }
}

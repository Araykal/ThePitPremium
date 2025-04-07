package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 17:38
 */
@WeaponOnly
@BowOnly
public class ComboHealEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity, IActionDisplayEnchant {

    private final DecimalFormat numFormat = new DecimalFormat("0.0");

    @Override
    public String getEnchantName() {
        return "强力击: 治疗";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "vampire";
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
        return "&7每 &e4 &7次击中恢复自身 &c" + numFormat.format(enchantLevel * 0.4) + "❤ &7与 &6" + numFormat.format(enchantLevel * 0.4) + "❤ &7(不可叠加)";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit() % 4 == 0) {
            float heart = (((CraftPlayer) attacker).getHandle()).getAbsorptionHearts();
            float heal = new Double(enchantLevel * 0.8).floatValue();
            if (heart <= heal) {
                (((CraftPlayer) attacker).getHandle()).setAbsorptionHearts(Math.min(heart + heal, heal));
            }
            PlayerUtil.heal(attacker, 0.8 * enchantLevel);
        }
    }

    @Override
    @cn.charlotte.pit.parm.type.BowOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getBowHit() % 4 == 0) {
            float heart = (((CraftPlayer) attacker).getHandle()).getAbsorptionHearts();
            float heal = new Double(enchantLevel * 0.8).floatValue();
            if (heart <= heal) {
                (((CraftPlayer) attacker).getHandle()).setAbsorptionHearts(Math.min(heart + heal, heal));
            }
            PlayerUtil.heal(attacker, 0.8 * enchantLevel);
        }
    }

    @Override
    public String getText(int level, Player player) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        return (hit % 4 == 0 ? "&a&l✔" : "&e&l" + (4 - hit % 4));
    }
}

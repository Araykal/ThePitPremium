package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/29 20:20
 */
@ArmorOnly
public class AbsorptionEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "生命吸收";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "absorption";
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
        return "&7击杀玩家获得可吸收 &6" + (enchantLevel * 0.5 + (enchantLevel >= 3 ? 0.5 : 0)) + "❤ &7的伤害吸收效果 (可叠加,最高&6" + (enchantLevel + 3) + "❤&7)";
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        float heart = (((CraftPlayer) myself).getHandle()).getAbsorptionHearts();
        if (heart > 0) {
            (((CraftPlayer) myself).getHandle()).setAbsorptionHearts(Math.min(heart + (enchantLevel + (enchantLevel >= 3 ? 1 : 0)), (2 * enchantLevel + 6)));
        }
    }

}

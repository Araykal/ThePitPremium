package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/18 19:25
 */
@WeaponOnly
public class GutsEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "胆量";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "Guts";
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
        return "&7击杀额外恢复自身 &c" + (enchantLevel >= 3 ? 1 : 0.25 * enchantLevel) + "❤";
    }

    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerUtil.heal(myself, (enchantLevel >= 3 ? 2 : 0.5 * enchantLevel));
    }
}

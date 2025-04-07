package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.RodOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/7 1:16
 */
@RodOnly
public class LureEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "饵钓";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "lure";
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
        return "&7使你钓鱼速度提升 &c" + enchantLevel * 50 + "%";
    }
}

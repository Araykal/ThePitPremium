package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/23 19:33
 */
@WeaponOnly
public class BruiserEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "斗士";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "bruiser_enchant";
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
        return "&7格挡时降低受到的普通类型初始伤害 &c" + (0.5 * enchantLevel + (enchantLevel >= 3 ? 0.5 : 0) + "❤");
    }

    //effect are done in combat listener (because it don't edit final damage
}

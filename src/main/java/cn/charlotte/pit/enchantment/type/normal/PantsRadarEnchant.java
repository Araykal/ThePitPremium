package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/25 14:53
 */
@ArmorOnly
@WeaponOnly
@BowOnly
public class PantsRadarEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "神话雷达";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "pants_radar";
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
        return "&7手持或装备此物品时,战斗获得神话物品的概率提升至原来的 &d" + (100 + 30 * enchantLevel) + "%";
    }
}

package cn.charlotte.pit.enchantment.type.sewer_normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/16 21:43
 */
public class AegisEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "宙斯之盾";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "aegis_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.SEWER_NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7每 &e9 &7秒获得一层护盾 (可以抵消1次玩家伤害) (最高1层)";
    }
}

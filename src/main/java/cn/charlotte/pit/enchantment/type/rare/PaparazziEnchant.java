package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/26 13:43
 */
@ArmorOnly
public class PaparazziEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "Paparazzi";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "paparazzi_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.AUCTION_LIMITED_RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7参与大型事件的奖励 &e+" + (enchantLevel * 50) + "% &7(声望数向下取整)."
                + "/s&7每次触发此效果扣除此神话物品 &c2 &7点生命.";
    }
}

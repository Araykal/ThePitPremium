package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/25 14:41
 */
@ArmorOnly
public class PurpleGoldEnchant extends AbstractEnchantment {
    @Override
    public String getEnchantName() {
        return "紫金";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "purple_gold";
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
        return "&7破坏黑曜石时获得 &6+" + (enchantLevel * 3 + (3 - enchantLevel)) + " 硬币" + (enchantLevel >= 2 ?
                "/s&7并为自身施加 &c生命恢复 III &f(" + TimeUtil.millisToTimer(enchantLevel * 1000L) + "&f)" : "");
    }
}

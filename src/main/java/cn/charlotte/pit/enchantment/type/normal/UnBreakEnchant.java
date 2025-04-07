package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.RodOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IItemDamage;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:42
 * 4
 */

//耐久设定只存在于钓鱼竿中
@RodOnly
public class UnBreakEnchant extends AbstractEnchantment implements IItemDamage {
    @Override
    public String getEnchantName() {
        return "耐久";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "unbreak";
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
        double chance = this.getSuccessChance(enchantLevel) * 100;

        return "&7使用此物品有 &a" + chance + "% &7的几率不消耗耐久值";
    }

    private double getSuccessChance(int enchantLevel) {
        double chance = 0;
        switch (enchantLevel) {
            case 1: {
                chance = 0.5;
                break;
            }
            case 2: {
                chance = 0.66;
                break;
            }
            case 3: {
                chance = 0.75;
                break;
            }
            default:
        }

        return chance;
    }


    @Override
    public void handleItemDamaged(int enchantLevel, ItemStack itemStack, Player myself, AtomicBoolean cancel) {
        if (!this.isItemHasEnchant(itemStack)) {
            return;
        }
        if (enchantLevel == -1) {
            return;
        }
        double chance = this.getSuccessChance(enchantLevel);

        boolean success = RandomUtil.hasSuccessfullyByChance(chance);
        if (success) {
            cancel.set(true);
        }
    }
}

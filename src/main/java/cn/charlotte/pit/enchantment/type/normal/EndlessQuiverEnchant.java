package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/30 15:24
 */
@BowOnly
public class EndlessQuiverEnchant extends AbstractEnchantment implements IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "箭矢回收";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "endless_quiver_enchant";
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
        int arrow;
        switch (enchantLevel) {
            default:
                arrow = 1;
                break;
            case 2:
                arrow = 3;
                break;
            case 3:
                arrow = 8;
                break;
        }
        return "&7弓箭命中获得 &f+" + arrow + " 支箭";
    }

    @Override
    @cn.charlotte.pit.parm.type.BowOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        int arrow;
        switch (enchantLevel) {
            default:
                arrow = 1;
                break;
            case 2:
                arrow = 3;
                break;
            case 3:
                arrow = 8;
                break;
        }
        ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW).internalName("default_arrow").defaultItem().canDrop(false).canSaveToEnderChest(false);
        attacker.getInventory().addItem(arrowBuilder.amount(arrow).build());
    }
}

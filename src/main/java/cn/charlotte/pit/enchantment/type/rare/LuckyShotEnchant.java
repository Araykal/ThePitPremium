package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/29 21:03
 */
@BowOnly
public class LuckyShotEnchant extends AbstractEnchantment implements IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "幸运一击: 箭矢";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "lucky_shot_bow";
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
        int chance;
        switch (enchantLevel) {
            default:
                chance = 2;
                break;
            case 2:
                chance = 5;
                break;
            case 3:
                chance = 10;
                break;
        }
        return "&7弓箭射击时有 &e" + chance + "% &7的概率造成伤害 &c+300%";
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        double chance;
        switch (enchantLevel) {
            default:
                chance = 0.02;
                break;
            case 2:
                chance = 0.05;
                break;
            case 3:
                chance = 0.1;
                break;
        }
        if (RandomUtil.hasSuccessfullyByChance(chance)) {
            boostDamage.getAndAdd(3);
        }
    }
}

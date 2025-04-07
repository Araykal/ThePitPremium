package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/8 20:19
 */
@ArmorOnly
public class NotGladiatorEnchant extends AbstractEnchantment implements IPlayerDamaged {
    @Override
    public String getEnchantName() {
        return "'不是' 角斗士";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "not_gladiator_enchant";
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
        return "&7在以你为中心的 &b12 &7格范围内,"
                + "/s&7每存在一名除你以外的玩家,"
                + "/s&7你受到的伤害 &9-" + (0.5 + enchantLevel * 0.5) + "% &7(最高叠加10层,叠加低于3层时无效果) .";
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        double boost = PlayerUtil.getNearbyPlayers(myself.getLocation(), 12).size();

        int sybilLevel = Utils.getEnchantLevel(myself.getInventory().getLeggings(), "sybil");
        if (sybilLevel > 0) {
            boost += sybilLevel + 1;
        }

        if (boost > 10) {
            boost = 10;
        }
        if (boost < 3) {
            boost = 0;
        }
        boostDamage.set(boostDamage.get() - 0.01 * (0.5 + enchantLevel * 0.5) * boost);
    }
}

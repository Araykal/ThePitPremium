package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/8 20:36
 */
@ArmorOnly
@WeaponOnly
@BowOnly
public class PitMBAEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "天坑MBA";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "pit_mba_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.AUCTION_LIMITED;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        int coinBonus = enchantLevel * 20 - 10;
        int expBonus = enchantLevel * 10;
        return "&7当周围15格内有至少5名玩家时,"
                + "/s&7击杀获得硬币 &6+" + coinBonus + "% &7,获得经验值 &b+" + expBonus + "%";
    }


    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        double boost = PlayerUtil.getNearbyPlayers(myself.getLocation(), 15).size();

        int sybilLevel = Utils.getEnchantLevel(myself.getInventory().getLeggings(), "sybil");
        if (sybilLevel > 0) {
            boost += sybilLevel + 1;
        }

        if (boost >= 5) {
            int coinBonus = enchantLevel * 20 - 10;
            int expBonus = enchantLevel * 10;
            coins.getAndAdd(coinBonus * 0.01 * coins.get());
            experience.getAndAdd(expBonus * 0.01 * experience.get());
        }
    }
}

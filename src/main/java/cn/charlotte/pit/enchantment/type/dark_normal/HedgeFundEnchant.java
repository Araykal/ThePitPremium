package cn.charlotte.pit.enchantment.type.dark_normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/15 20:40
 */
@ArmorOnly
public class HedgeFundEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "避险基金";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "hedge_fund_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.DARK_NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7击杀获得 &6+6 硬币 &7."
                + "/s&7击杀穿着 &eI+ 阶 &7的 &d神话之甲 &7的玩家额外获得 &666 硬币 &7.";
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        coins.getAndAdd(6);
        Player targetPlayer = (Player) target;
        if (targetPlayer.getInventory().getLeggings() != null && targetPlayer.getInventory().getLeggings().getType() != Material.AIR) {
            String internalName = ItemUtil.getInternalName(targetPlayer.getInventory().getLeggings());
            boolean isMythicItem = internalName != null &&
                    ThePit.getInstance()
                            .getItemFactor()
                            .getItemMap()
                            .containsKey(internalName);
            if (isMythicItem) {
                Integer tier = ItemUtil.getItemIntData(targetPlayer.getInventory().getLeggings(), "tier");
                if (tier >= 1) {
                    coins.getAndAdd(66);
                }
            }
        }
    }
}

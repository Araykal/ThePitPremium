package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/31 21:30
 */
@ArmorOnly
public class SnowballsEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "扔雪球!";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "snowballs_enchant";
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
        return "&7击杀玩家获得 &f" + (enchantLevel >= 3 ? 16 : 8) + " * 雪球 &7(最大持有数" + (enchantLevel * 8 + 8) + ")";
        // + "/s&7雪球击中玩家时施加 &8缓慢 I &7(00:0" + (2 + enchantLevel) + ") 效果."
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        int amount = enchantLevel >= 3 ? 16 : 8;
        ItemBuilder snowBallBuilder = new ItemBuilder(Material.SNOW_BALL).canDrop(false).canSaveToEnderChest(false).deathDrop(true).canTrade(false).removeOnJoin(true).internalName("snoballs_enchantment_item");
        if (InventoryUtil.getAmountOfItem(myself, snowBallBuilder.build()) + amount > (enchantLevel * 8 + 8)) {
            amount = (enchantLevel * 8 + 8) - InventoryUtil.getAmountOfItem(myself, snowBallBuilder.build());
        }
        if (amount <= 0) {
            return;
        }
        myself.getInventory().addItem(snowBallBuilder.amount(amount).build());
    }

}

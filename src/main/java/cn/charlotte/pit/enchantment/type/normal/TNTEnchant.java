package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.IPlayerRespawn;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/28 10:30
 */
@ArmorOnly
public class TNTEnchant extends AbstractEnchantment implements IPlayerKilledEntity, IPlayerRespawn {
    private ItemBuilder getTNTBuilder() {
        return new ItemBuilder(Material.TNT)
                .internalName("tnt_enchant_item")
                .canDrop(false)
                .canSaveToEnderChest(false)
                .removeOnJoin(true)
                .deathDrop(true)
                .canTrade(false)
                .name("&cTNT")
                .lore(Arrays.asList("&7附魔物品", "&c放置时如没有装备对应附魔物品则TNT不会造成伤害!"));
    }

    @Override
    public String getEnchantName() {
        return "TNT";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "tnt_enchant";
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
        return "&7每次死亡后复活立刻获得 &f" + enchantLevel + " * &cTNT"
                + "/s&7击杀玩家时获得 &f" + (enchantLevel >= 3 ? 2 : 1) + " * &cTNT &7(持有上限9)"
                + "/s&7TNT放置后立刻被点燃并在1.5秒后爆炸,对周围3格内的所有敌人造成 &c" + (0.5 + enchantLevel * 0.5) + "❤ &7普通伤害."
                + "/s&7(TNT爆炸时如自身未装备此附魔,则TNT不会造成伤害)";
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        myself.getInventory().addItem(getTNTBuilder().amount(enchantLevel >= 3 ? 2 : 1).build());
        int amount = InventoryUtil.getAmountOfItem(myself, "tnt_enchant_item");
        if (amount > 9) {
            InventoryUtil.removeItem(myself, "tnt_enchant_item", amount - 9);
        }
    }

    @Override
    public void handleRespawn(int enchantLevel, Player myself) {
        myself.getInventory().addItem(getTNTBuilder().amount(enchantLevel).build());
    }
}

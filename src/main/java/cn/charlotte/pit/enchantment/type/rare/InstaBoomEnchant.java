package cn.charlotte.pit.enchantment.type.rare;

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
 * @Created_In: 2021/2/28 13:01
 */
@ArmorOnly
public class InstaBoomEnchant extends AbstractEnchantment implements IPlayerKilledEntity, IPlayerRespawn {

    private ItemBuilder getTNTBuilder() {
        return new ItemBuilder(Material.TNT)
                .internalName("insta_boom_tnt_enchant")
                .removeOnJoin(true)
                .deathDrop(true)
                .name("&c瞬爆TNT")
                .lore(Arrays.asList("&7附魔物品", "&c放置时如没有装备对应附魔物品则TNT不会造成伤害!"));
    }

    @Override
    public String getEnchantName() {
        return "TNT: 瞬爆";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "insta_boom_tnt_enchant";
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
        return "&7每次死亡后复活立刻获得 &f" + (enchantLevel >= 3 ? 2 : 1) + " * &c瞬爆TNT"
                + "/s&7击杀玩家时获得 &f" + (enchantLevel >= 3 ? 2 : 1) + " * &c瞬爆TNT &7(持有上限5)"
                + "/s&7瞬爆TNT放置后立刻被点燃并立刻爆炸,对周围4格内的所有敌人造成 &c" + (enchantLevel * 0.25) + "❤ &7普通伤害."
                + "/s&7(瞬爆TNT爆炸时如自身未装备此附魔,则TNT不会造成伤害)";
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        myself.getInventory().addItem(getTNTBuilder().amount(enchantLevel >= 3 ? 2 : 1).build());
        if (InventoryUtil.getAmountOfItem(myself, "insta_boom_tnt_enchant") > 5) {
            InventoryUtil.removeItem(myself, "insta_boom_tnt_enchant", InventoryUtil.getAmountOfItem(myself, "insta_boom_tnt_enchant") - 5);
        }
    }

    @Override
    public void handleRespawn(int enchantLevel, Player myself) {
        myself.getInventory().addItem(getTNTBuilder().amount(enchantLevel >= 3 ? 2 : 1).build());
    }
}

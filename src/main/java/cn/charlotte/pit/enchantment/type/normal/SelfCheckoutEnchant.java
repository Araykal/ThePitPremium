package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/26 13:28
 */
@ArmorOnly
public class SelfCheckoutEnchant extends AbstractEnchantment implements ITickTask {
    @Override
    public String getEnchantName() {
        return "自助结账";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "self_checkout_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    public int getCheckoutAmount(int enchantLevel) {
        switch (enchantLevel) {
            case 2:
                return 3000;
            case 3:
                return 5000;
            default:
                return 2000;
        }
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7自身被悬赏赏金达到 &65000g &7时,清空自身赏金并获得 &6" + getCheckoutAmount(enchantLevel) + "g"
                + "/s&7每次触发此效果扣除此神话物品 &c1 &7点生命.";
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getBounty() >= 5000) {
            //consume life start
            ItemStack leggings = player.getInventory().getLeggings();
            if ("mythic_leggings".equals(ItemUtil.getInternalName(leggings))) {
                MythicLeggingsItem mythicLeggings = new MythicLeggingsItem();
                mythicLeggings.loadFromItemStack(leggings);
                if (mythicLeggings.isEnchanted()) {
                    if (mythicLeggings.getMaxLive() > 0 && mythicLeggings.getLive() <= 1) {
                        player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    } else {
                        mythicLeggings.setLive(mythicLeggings.getLive() - 1);
                        player.getInventory().setLeggings(mythicLeggings.toItemStack());
                    }
                }
            }
            //consume life end
            profile.setBounty(0);
            profile.setCoins(profile.getCoins() + getCheckoutAmount(enchantLevel));
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }
}

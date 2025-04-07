package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerRespawn;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/24 18:58
 */
@ArmorOnly
public class RespawnAbsorptionEnchant extends AbstractEnchantment implements IPlayerRespawn {
    @Override
    public String getEnchantName() {
        return "复生: 生命吸收";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "respawn_absorption";
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
        return "&7每次死亡后复活立刻获得 &6" + (enchantLevel * 5) + "❤ 伤害吸收";
    }

    @Override
    public void handleRespawn(int enchantLevel, Player myself) {
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            ((CraftPlayer) myself).getHandle().setAbsorptionHearts(enchantLevel * 10);
        }, 10L);
    }
}

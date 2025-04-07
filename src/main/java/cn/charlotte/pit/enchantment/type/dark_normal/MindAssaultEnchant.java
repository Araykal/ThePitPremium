package cn.charlotte.pit.enchantment.type.dark_normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/15 20:14
 */

@AutoRegister
@ArmorOnly
public class MindAssaultEnchant extends AbstractEnchantment implements Listener {
    @Override
    public String getEnchantName() {
        return "精神攻击";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "mind_assault_enchant";
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
        return "&7你造成的伤害 &9-60% &7,同时周围11格内每有一名穿着 &6神话之甲 &7的玩家,你攻击的基础伤害 &c+0.75❤ &7(上限&c8❤&7)";
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Player damager;
        if (damagerEntity instanceof Player) {
            damager = ((Player) damagerEntity);
        } else if (damagerEntity instanceof Projectile && ((Projectile) damagerEntity).getShooter() instanceof Player) {
            damager = (Player) (((Projectile) damagerEntity).getShooter());
        } else {
            return;
        }

        final int level = Utils.getEnchantLevel(damager.getInventory().getLeggings(), "mind_assault_enchant");
        if (level < 1) return;

        event.setDamage(event.getDamage() * 0.4);
        final long size = PlayerUtil.getNearbyPlayers(damager.getLocation(), 11.0)
                .stream()
                .filter(entity -> "mythic_leggings".equals(ItemUtil.getInternalName(entity.getInventory().getLeggings())))
                .count();
        event.setDamage(event.getDamage() + Math.min((size - 1) * 1.5, 16.0));
    }

}

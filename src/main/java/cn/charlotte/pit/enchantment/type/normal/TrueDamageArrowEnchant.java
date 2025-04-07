package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/30 15:33
 */
@BowOnly
@AutoRegister
public class TrueDamageArrowEnchant extends AbstractEnchantment implements Listener {
    final DecimalFormat format = new DecimalFormat("0");

    public static double getTrueDamageRate(int enchantLevel) {
        return (0.15 + enchantLevel * 0.1) / (1 - (0.15 + enchantLevel * 0.1));
    }

    @Override
    public String getEnchantName() {
        return "真实一击: 箭矢";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "true_damage_arrow";
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

        return "&7造成的弓箭伤害降低至原来的 &9" + (100 - 15 - enchantLevel * 10) + "% &7,"
                + "/s&7弓箭命中额外造成相当于伤害量 &c" + format.format(100 * getTrueDamageRate(enchantLevel)) + "% &7的&f真实&7伤害";
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            if (event.getDamager().hasMetadata("true_shot")) {
                final Player player = (Player) ((Projectile) event.getDamager()).getShooter();
                if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
                final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
                if (itemInHand == null) return;
                final int level = this.getItemEnchantLevel(itemInHand);
                if (level == -1) {
                    return;
                }
                event.setDamage((1 - 0.15 - level * 0.1) * event.getDamage());
                Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                    final int noDamageTicks = player.getNoDamageTicks();
                    player.setNoDamageTicks(0);
                    PlayerUtil.damage(player, (Player) event.getEntity(), PlayerUtil.DamageType.TRUE, getTrueDamageRate(level) * event.getFinalDamage(), true);
                    player.setNoDamageTicks(noDamageTicks);
                }, 1L);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level <= 0) {
            return;
        }
        if (itemInHand.getType() == Material.BOW) {
            event.getProjectile().setMetadata("true_shot", new FixedMetadataValue(ThePit.getInstance(), true));
        }
    }
}

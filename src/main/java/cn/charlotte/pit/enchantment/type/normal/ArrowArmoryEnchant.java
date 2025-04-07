package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/24 19:57
 */
@AutoRegister
@BowOnly
public class ArrowArmoryEnchant extends AbstractEnchantment implements Listener {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "箭库";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "arrow_armory_enchant";
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
        return "&7弓箭射出时额外消耗 &f" + getExtraArrowRequirement(enchantLevel) + " &7支弓箭 (如可用,0.5秒冷却)"
                + "/s&7并使当次攻击造成的伤害 &c+" + getBoostDamage(enchantLevel) + "%";
    }

    public int getExtraArrowRequirement(int enchantLevel) {
        switch (enchantLevel) {
            case 1:
                return 2;
            case 2:
                return 4;
            default:
                return 7;
        }
    }

    public int getBoostDamage(int enchantLevel) {
        switch (enchantLevel) {
            case 1:
                return 12;
            case 2:
                return 25;
            default:
                return 60;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBowShot(EntityShootBowEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        if (PlayerUtil.isVenom(player)) return;
        if (PlayerUtil.isEquippingSomber(player)) return;
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level == -1) {
            return;
        }
        ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW).internalName("default_arrow").defaultItem().canDrop(false).canSaveToEnderChest(false);
        if (!cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired()) {
            return;
        }
        boolean success = InventoryUtil.removeItem(player, arrowBuilder.build(), getExtraArrowRequirement(level));
        if (success) {
            event.getProjectile().setMetadata("arrow_armory", new FixedMetadataValue(ThePit.getInstance(), getBoostDamage(level)));
            cooldown.put(player.getUniqueId(), new Cooldown(500, TimeUnit.MILLISECONDS));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            if (event.getDamager().hasMetadata("arrow_armory")) {
                Player shooter = (Player) ((Projectile) event.getDamager()).getShooter();
                if (PlayerUtil.isVenom(shooter)) return;
                event.setDamage((1 + 0.01 * event.getDamager().getMetadata("arrow_armory").get(0).asInt()) * event.getDamage());
            }
        }
    }
}

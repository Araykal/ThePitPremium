package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.buff.BuffData;
import cn.charlotte.pit.buff.impl.HealPoisonDeBuff;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.*;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/26 11:12
 */
@ArmorOnly
@AutoRegister
public class TestEnchant extends AbstractEnchantment implements ITickTask, IAttackEntity, IPlayerBeKilledByEntity, IPlayerShootEntity, IPlayerKilledEntity, IPlayerAssist, IPlayerDamaged, IPlayerRespawn, Listener {
    private static final HealPoisonDeBuff healPoisonDeBuff = new HealPoisonDeBuff();

    @Override
    public String getEnchantName() {
        return "测试";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "test_enchantment";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.OP;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7description goes here";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        attacker.sendMessage("handleAttackEntity");
        if (PlayerUtil.isCritical(attacker)) {
            attacker.sendMessage("handleCriticalHit");
        }
    }

    @Override
    public void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience) {
        myself.sendMessage("handlePlayerAssist");
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        myself.sendMessage("handlePlayerDamaged");
        if (attacker instanceof Arrow) {
            myself.sendMessage("ArrowAttack Handled");
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        myself.sendMessage("handlePlayerKilled");
        Player targetPlayer = (Player) target;
        for (ItemStack itemStack : targetPlayer.getInventory().getArmorContents()) {
            myself.sendMessage(itemStack.getType().name());
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        attacker.sendMessage("handleShootEntity " + System.currentTimeMillis());
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level == -1) {
            return;
        }
        player.sendMessage("You're healing! Amount:" + event.getAmount() + " Reason:" + event.getRegainReason().name());
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level == -1) {
            return;
        }
        CC.boardCast("1");
        event.getProjectile().setMetadata("test_bow", new FixedMetadataValue(ThePit.getInstance(), true));
    }

    @EventHandler
    public void onBowHit(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("test_bow") && event.getEntity().getShooter() != null) {
            CC.boardCast("2");
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                player.sendMessage("handleTestBowHit");
                player.sendMessage("" + System.currentTimeMillis());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            if (event.getDamager().hasMetadata("test_bow")) {
                CC.boardCast("handleTestBowHitEntity");
            }
        }
    }

    @Override
    public void handleRespawn(int enchantLevel, Player myself) {
        myself.sendMessage("IPlayerRespawn");
        myself.sendMessage("test metadata detect: " + myself.hasMetadata("test_metadata"));
    }

    @Override
    public void handlePlayerBeKilledByEntity(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        myself.sendMessage("IPlayerBeKilledByEntity");
        if (myself.hasMetadata("test_metadata")) {
            myself.removeMetadata("test_metadata", ThePit.getInstance());
            myself.sendMessage("removing test metadata");
        } else {
            myself.setMetadata("test_metadata", new FixedMetadataValue(ThePit.getInstance(), true));
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        BuffData.Buff buffData = healPoisonDeBuff.getPlayerBuffData(player);
        if (RandomUtil.hasSuccessfullyByChance(0.5)) {
            try {
                healPoisonDeBuff.stackBuff(player, 60 * 1000L);
                player.sendMessage("stacking buff!");
            } catch (Exception e) {
                CC.printError(player, e);
            }
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }
}

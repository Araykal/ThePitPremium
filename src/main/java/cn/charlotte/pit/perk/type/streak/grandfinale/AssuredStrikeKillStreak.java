package cn.charlotte.pit.perk.type.streak.grandfinale;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@AutoRegister
public class AssuredStrikeKillStreak extends AbstractPerk implements Listener, IAttackEntity, IPlayerShootEntity {

    @Override
    public String getInternalPerkName() {
        return "assured_strike_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "自信一击";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public double requireCoins() {
        return 10000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 90;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c7 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7下次攻击造成的伤害 &c+35% &7并为自身添加 &b速度 I &f(00:20)");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.KILL_STREAK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onStreak(PitStreakKillChangeEvent event) {
        int streak = 7;
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        //trigger check
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            myself.setMetadata("assured_strike", new FixedMetadataValue(ThePit.getInstance(), true));
        }
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.hasMetadata("assured_strike")) {
            attacker.removeMetadata("assured_strike", ThePit.getInstance());
            boostDamage.getAndAdd(0.35);
            attacker.removePotionEffect(PotionEffectType.SPEED);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 0), true);
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.hasMetadata("assured_strike")) {
            attacker.removeMetadata("assured_strike", ThePit.getInstance());
            boostDamage.getAndAdd(0.35);
            attacker.removePotionEffect(PotionEffectType.SPEED);
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 0), true);
        }
    }
}

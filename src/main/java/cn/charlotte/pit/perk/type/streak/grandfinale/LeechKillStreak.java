package cn.charlotte.pit.perk.type.streak.grandfinale;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class LeechKillStreak extends AbstractPerk implements Listener {

    @Override
    public String getInternalPerkName() {
        return "leech_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "吸血虫";
    }

    @Override
    public Material getIcon() {
        return Material.FERMENTED_SPIDER_EYE;
    }

    @Override
    public double requireCoins() {
        return 6000;
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
        return 70;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c3 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7下次攻击恢复 &c伤害量的 20% + 0.5❤ &7生命 (不可叠加)");
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
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        //trigger check (every X streak)
        int streak = 3;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            myself.setMetadata("leech_hit", new FixedMetadataValue(ThePit.getInstance(), true));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        Player attacker = null;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        }
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            attacker = (Player) ((Projectile) event.getDamager()).getShooter();
        }
        if (attacker != null && attacker.hasMetadata("leech_hit")) {
            attacker.removeMetadata("leech_hit", ThePit.getInstance());
            PlayerUtil.heal(attacker, 1);
            PlayerUtil.heal(attacker, 0.2 * event.getFinalDamage());
        }
    }
}

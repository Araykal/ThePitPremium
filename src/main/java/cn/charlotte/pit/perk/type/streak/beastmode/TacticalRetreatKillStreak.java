package cn.charlotte.pit.perk.type.streak.beastmode;

import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/4/26 18:39
 */
@AutoRegister
public class TacticalRetreatKillStreak extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "tactical_retreat";
    }

    @Override
    public String getDisplayName() {
        return "战术撤退";
    }

    @Override
    public Material getIcon() {
        return Material.BREAD;
    }

    @Override
    public double requireCoins() {
        return 5000;
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
        return 50;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c7 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7自身获得 &4虚弱 IV &f(00:05)");
        list.add("  &a▶ &7自身获得 &c生命恢复 IV &f(00:05)");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return null;
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
        int streak = 7;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            myself.removePotionEffect(PotionEffectType.WEAKNESS);
            myself.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 3), false);
            myself.removePotionEffect(PotionEffectType.REGENERATION);
            myself.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 3), false);
        }
    }
}

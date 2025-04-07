package cn.charlotte.pit.perk.type.streak.beastmode;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Araykal
 * @since 2025/1/16
 */
public class MonsterKillStreak extends AbstractPerk implements Listener {

    public static final HashMap<UUID, Integer> times = new HashMap<>();

    @Override
    public String getInternalPerkName() {
        return "monster";
    }

    @Override
    public String getDisplayName() {
        return "怪物";
    }

    @Override
    public Material getIcon() {
        return Material.LEASH;
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
        return 40;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c25 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7提升血量上限 &c1❤ &7(最高提升2次)");
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

    @EventHandler(ignoreCancelled = true)
    public void onStreak(PitStreakKillChangeEvent event) {
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (myself == null || !myself.isOnline() || myself.getUniqueId() == null) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        //trigger check (every X streak)
        int streak = 25;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            if (times.get(myself.getUniqueId()) < 2) {
                myself.setMaxHealth(myself.getMaxHealth() + 2);
                times.put(myself.getUniqueId(), times.get(myself.getUniqueId()) + 1);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        times.put(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player myself = event.getPlayer();
        times.put(myself.getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player myself = event.getEntity();
        times.put(myself.getUniqueId(), 0);
    }
}

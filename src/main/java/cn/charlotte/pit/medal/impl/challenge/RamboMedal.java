package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:58
 */
@AutoRegister
public class RamboMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "RAMBO_STREAK";
    }

    @Override
    public String getDisplayName(int level) {
        return "猛汉";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在装备天赋[猛汉]的情况下，连杀数达到10";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 1;
    }

    @Override
    public int getRarity(int level) {
        return 1;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onStreak(PitStreakKillChangeEvent event) {
        if (event.getFrom() < 10 && event.getTo() >= 10 && PlayerUtil.isPlayerChosePerk(Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid()), "rambo")) {
            addProgress(event.getPlayerProfile(), 1);
        }
    }
}

package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:51
 */
@AutoRegister
public class StrengthPerkMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "STRENGTH_KILL";
    }

    @Override
    public String getDisplayName(int level) {
        return "亢奋状态";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在天赋[力量]的最大增益下击杀一名玩家";
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

    @EventHandler
    public void onKill(PitKillEvent event) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getKiller().getUniqueId());
        if (profile.getStrengthNum() >= 5 && !profile.getStrengthTimer().hasExpired()) {
            addProgress(profile, 1);
        }
    }
}

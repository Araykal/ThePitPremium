package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitPlayerUnlockPerkEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 22:37
 */
@AutoRegister
public class FastPassMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "FAST_PASS_UNLOCK";
    }

    @Override
    public String getDisplayName(int level) {
        return "高速通道";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "解锁精通天赋[高速通道]";
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
        return 2;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {
        if (profile.getUnlockedPerkMap().get("FastPass") != null) {
            setProgress(profile, 1);
        }
    }

    @EventHandler
    public void onPerkUnlock(PitPlayerUnlockPerkEvent event) {
        if (event.getPerk().getInternalPerkName().equalsIgnoreCase("FastPass")) {
            setProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

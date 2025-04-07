package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitQuestCompleteEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/9 22:52
 */
@AutoRegister
public class FastContractMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "FAST_COMPLETE_CONTRACT";
    }

    @Override
    public String getDisplayName(int level) {
        return "高效行动";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在60秒内完成一个挑战任务";
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
        return 5;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {

    }

    @EventHandler
    public void onQuestComplete(PitQuestCompleteEvent event) {
        if (System.currentTimeMillis() - event.getQuestData().getStartTime() <= 60 * 1000) {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

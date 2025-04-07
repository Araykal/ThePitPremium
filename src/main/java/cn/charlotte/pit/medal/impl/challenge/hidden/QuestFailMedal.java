package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitQuestInactiveEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 9:48
 */
@AutoRegister
public class QuestFailMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "QUEST_FAILED";
    }

    @Override
    public String getDisplayName(int level) {
        return "功亏一篑";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "放弃一个仅差一击杀便达到任务要求的挑战任务";
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
        return true;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {

    }

    @EventHandler
    public void onQuestInactive(PitQuestInactiveEvent event) {
        if (event.getQuestData().getTotal() - event.getQuestData().getCurrent() == 1) {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

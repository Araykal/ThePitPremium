package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitQuestCompleteEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 8:31
 */
@AutoRegister
public class QuestDeadlineMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "FINISH_QUEST_BEFORE_DDL";
    }

    @Override
    public String getDisplayName(int level) {
        return "死线边缘";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在单次挑战任务超时失败前10秒内，完成任务击杀数要求";
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
        return true;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {

    }

    @EventHandler
    public void onQuestComplete(PitQuestCompleteEvent event) {
        if (event.getQuestData().getEndTime() - System.currentTimeMillis() <= 10 * 1000) {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

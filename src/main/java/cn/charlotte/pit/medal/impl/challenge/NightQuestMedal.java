package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.event.PitQuestInactiveEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:22
 */
@AutoRegister
public class NightQuestMedal extends AbstractMedal implements Listener {
    private static boolean isNightQuest(PlayerProfile profile, QuestData quest) {
        return profile.isNightQuestEnable() && TimeUtil.getMinecraftTick(quest.getStartTime()) > 12000;
    }

    @Override
    public String getInternalName() {
        return "COMPLETE_NIGHT_QUEST";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"夜幕\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "完成一次夜幕任务";
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

    }

    @EventHandler
    public void onQuestInactive(PitQuestInactiveEvent event) {
        if (event.getQuestData().getCurrent() >= event.getQuestData().getTotal() && isNightQuest(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), event.getQuestData())) {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

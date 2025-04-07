package cn.charlotte.pit.medal.impl.tier;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitQuestInactiveEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 0:09
 */
@AutoRegister
public class QuestMedal extends AbstractMedal implements Listener {
    private final DecimalFormat df = new DecimalFormat(",###,###,###,###");

    @Override
    public String getInternalName() {
        return "STATUS_CONTRACTS";
    }

    @Override
    public String getDisplayName(int level) {
        return "契约者";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "累计挑战任务完成数达到" + df.format(getProgressRequirement(level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getProgressRequirement(int level) {
        switch (level) {
            case 1:
                return 5;
            case 2:
                return 25;
            case 3:
                return 100;
            case 4:
                return 250;
            case 5:
                return 500;
            default:
                return -1;
        }
    }

    @Override
    public int getRarity(int level) {
        return level;
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
        if (event.getQuestData().getCurrent() >= event.getQuestData().getTotal()) {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 14:18
 */
public class RemedyMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "OLD_PLAYER";
    }

    @Override
    public String getDisplayName(int level) {
        return "归档";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "成功领取3.0版本更新补偿礼包";
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
        if (profile.getRemedyExp() <= 0 && !profile.getRemedyDate().equalsIgnoreCase("none")) {
            setProgress(profile, 1);
        }
    }
}

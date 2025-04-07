package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/9 22:42
 */

public class HundredLevelMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "HUNDRED_LEVEL";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"三位数\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "等级达到100级";
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
        if (profile.getLevel() >= 100) {
            setProgress(profile, 1);
        }
    }
}

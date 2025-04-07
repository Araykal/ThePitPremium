package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:19
 */
public class QuickMathsMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "QUICK_MATHS";
    }

    @Override
    public String getDisplayName(int level) {
        return "算术好手";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在事件[算术]中,在2.5秒内正确回答速算提问并获得奖励";
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
}

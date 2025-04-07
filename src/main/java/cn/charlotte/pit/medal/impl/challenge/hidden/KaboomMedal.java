package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 9:52
 */
public class KaboomMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "KABOOM_FROM_STAFF";
    }

    @Override
    public String getDisplayName(int level) {
        return "超级闪电";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "被Kaboom闪电击飞";
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
}

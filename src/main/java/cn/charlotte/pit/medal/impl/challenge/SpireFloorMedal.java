package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:31
 */
public class SpireFloorMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "SPIRE_HIGHEST_FLOOR";
    }

    @Override
    public String getDisplayName(int level) {
        return "尖塔之巅";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在事件[尖塔夺魁]中，登上第九层";
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

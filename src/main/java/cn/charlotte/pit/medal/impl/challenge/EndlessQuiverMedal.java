package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 8:22
 */
public class EndlessQuiverMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "ENDLESS_QUIVER";
    }

    @Override
    public String getDisplayName(int level) {
        return "无尽箭袋";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "使用天赋[无尽箭袋]，获得1,500支箭矢";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 1500;
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

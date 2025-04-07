package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 17:32
 */
public class TrickleDownMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "TRICKLE_DOWN";
    }

    @Override
    public String getDisplayName(int level) {
        return "黄金时代";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "通过使用天赋[涓流]，累计额外获得1,000硬币";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 1000;
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

    }
}

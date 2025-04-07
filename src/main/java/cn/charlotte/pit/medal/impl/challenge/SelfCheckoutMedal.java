package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:18
 */
public class SelfCheckoutMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "SELF_CHECKOUT";
    }

    @Override
    public String getDisplayName(int level) {
        return "自助结账";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "使用附魔[自助结账]清空自己的赏金并获得硬币";
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
        return 3;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {

    }
}

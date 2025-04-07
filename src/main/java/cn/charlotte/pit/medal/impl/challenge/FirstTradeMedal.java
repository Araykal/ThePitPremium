package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:43
 */
public class FirstTradeMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "FIRST_TRADE";
    }

    @Override
    public String getDisplayName(int level) {
        return "第一桶金";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "完成一次交易";
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

    }
}

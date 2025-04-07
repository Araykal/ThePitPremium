package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/9 23:02
 */
public class MaxBountyHunterMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "MAX_BOUNTY_HUNTER";
    }

    @Override
    public String getDisplayName(int level) {
        return "赏金猎人";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "击杀一名被5,000及以上硬币悬赏的玩家";
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

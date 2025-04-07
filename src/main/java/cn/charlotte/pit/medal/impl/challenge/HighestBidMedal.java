package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:56
 */
public class HighestBidMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "AUCTION_HIGHEST_BID";
    }

    @Override
    public String getDisplayName(int level) {
        return "拿下";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在事件[拍卖]中，成功拍下一件物品";
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

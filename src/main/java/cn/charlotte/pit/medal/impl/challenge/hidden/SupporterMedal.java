package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 8:35
 */
public class SupporterMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "SUPPORTER_BUYER";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"感谢你游玩我们的游戏\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "购买[天坑会员]";
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
        if (profile.isSupporter()) {
            setProgress(profile, 1);
        }
    }
}

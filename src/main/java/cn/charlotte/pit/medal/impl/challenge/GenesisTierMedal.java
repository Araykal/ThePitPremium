package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 18:13
 */
public class GenesisTierMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "GENESIS_TIER_VII";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"光暗交替\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在限时活动[光暗派系]中，累计提升2次第VII阶段里程碑加成";
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
        if (profile.getGenesisData().getBoostTier() >= 2) {
            addProgress(profile, 1);
        }
    }
}

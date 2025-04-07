package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 17:35
 */
public class RagePitTopMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "RAGE_PIT_TOP";
    }

    @Override
    public String getDisplayName(int level) {
        return "疯狂点击";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在单次参与人数超过50人的事件[疯狂天坑]中，取得前三名";
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

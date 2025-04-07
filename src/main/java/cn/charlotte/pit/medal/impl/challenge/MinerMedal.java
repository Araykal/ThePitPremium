package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 17:39
 */
public class MinerMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "COBBLESTONE_COLLECTOR";
    }

    @Override
    public String getDisplayName(int level) {
        return "何以击石";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "背包内圆石的数量达到64";
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

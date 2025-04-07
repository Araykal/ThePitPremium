package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 7:49
 */
public class VampireMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "VAMPIRE_HEAL";
    }

    @Override
    public String getDisplayName(int level) {
        return "吸血鬼";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "通过使用天赋[吸血鬼]累计回复自身15,000点生命值";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 15000;
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

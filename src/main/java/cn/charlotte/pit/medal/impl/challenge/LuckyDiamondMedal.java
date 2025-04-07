package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 17:28
 */
public class LuckyDiamondMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "LUCKY_DIAMOND";
    }

    @Override
    public String getDisplayName(int level) {
        return "幸运钻石!";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "通过使用天赋[幸运钻石]，累计获得50件钻石盔甲";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 50;
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

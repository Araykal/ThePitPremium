package cn.charlotte.pit.medal.impl.tier;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.util.chat.RomanUtil;

/**
 * @Creator Misoryan
 * @Date 2021/6/8 20:21
 */
public class PrestigeMedal extends AbstractMedal {

    @Override
    public String getInternalName() {
        return "STATUS_PRESTIGE";
    }

    @Override
    public String getDisplayName(int level) {
        return "千锤百炼";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "达到精通" + RomanUtil.convert(getProgressRequirement(level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getProgressRequirement(int level) {
        switch (level) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 10;
            case 5:
                return 15;
            default:
                return -1;
        }
    }

    @Override
    public int getRarity(int level) {
        return level;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {
        setProgress(profile, profile.getPrestige());
    }
}

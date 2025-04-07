package cn.charlotte.pit.medal.impl.tier;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

import java.text.DecimalFormat;

/**
 * @Creator Misoryan
 * @Date 2021/6/8 21:19
 */
public class KillMedal extends AbstractMedal {
    private final DecimalFormat df = new DecimalFormat(",###,###,###,###");

    @Override
    public String getInternalName() {
        return "STATUS_KILL";
    }

    @Override
    public String getDisplayName(int level) {
        return "战无休止";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "累计击杀数达到" + df.format(getProgressRequirement(level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getProgressRequirement(int level) {
        switch (level) {
            case 1:
                return 200;
            case 2:
                return 1000;
            case 3:
                return 5000;
            case 4:
                return 25000;
            case 5:
                return 100000;
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
        setProgress(profile, profile.getKills());
    }
}

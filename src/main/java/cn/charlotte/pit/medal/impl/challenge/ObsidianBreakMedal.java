package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:00
 */
public class ObsidianBreakMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "BREAK_HUNDRED_OBSIDIAN";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"紫金\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "破坏100次黑曜石";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 100;
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

package cn.charlotte.pit.medal.impl.challenge.hidden;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 8:04
 */
public class RuleReaderMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "RULE_BOOK_READER";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"读一本书\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "打开两次[天坑乱斗总规则]";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getProgressRequirement(int level) {
        return 2;
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

    }
}

package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:51
 */
public class PizzaEventMedal extends AbstractMedal {
    @Override
    public String getInternalName() {
        return "PIZZA_ORDER";
    }

    @Override
    public String getDisplayName(int level) {
        return "使命必达";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在单次事件[天坑外卖]中，成功交付至少35个汉堡";
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

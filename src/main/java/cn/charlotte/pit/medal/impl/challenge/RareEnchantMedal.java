package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.Listener;


/**
 * @Creator Misoryan
 * @Date 2021/6/10 19:22
 */
@AutoRegister
public class RareEnchantMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "GET_RARE_ENCHANT";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"稀世珍宝\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "在一次[神话之井]附魔中，获得带有稀有附魔的神话物品";
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

package cn.charlotte.pit.medal.impl.tier;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitGainCoinsEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 19:01
 */
@AutoRegister
public class CoinsMedal extends AbstractMedal implements Listener {
    private final DecimalFormat df = new DecimalFormat(",###,###,###,###");

    @Override
    public String getInternalName() {
        return "STATUS_COINS";
    }

    @Override
    public String getDisplayName(int level) {
        return "探金之人";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "累计获得硬币数达到" + df.format(getProgressRequirement(level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getProgressRequirement(int level) {
        switch (level) {
            case 1:
                return 10000;
            case 2:
                return 100000;
            case 3:
                return 1000000;
            case 4:
                return 10000000;
            case 5:
                return 3000000;
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

    }

    @EventHandler
    public void onGrindCoins(PitGainCoinsEvent event) {
        addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), (int) event.getCoins());
    }
}

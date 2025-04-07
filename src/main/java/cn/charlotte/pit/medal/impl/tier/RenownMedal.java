package cn.charlotte.pit.medal.impl.tier;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitGainRenownEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

/**
 * @Creator Misoryan
 * @Date 2021/6/11 0:05
 */
@AutoRegister
public class RenownMedal extends AbstractMedal implements Listener {

    private final DecimalFormat df = new DecimalFormat(",###,###,###,###");

    @Override
    public String getInternalName() {
        return "STATUS_RENOWN";
    }

    @Override
    public String getDisplayName(int level) {
        return "\"声望\"";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "累计获得声望数达到" + df.format(getProgressRequirement(level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getProgressRequirement(int level) {
        switch (level) {
            case 1:
                return 10;
            case 2:
                return 50;
            case 3:
                return 200;
            case 4:
                return 500;
            case 5:
                return 2000;
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
    public void onGainRenown(PitGainRenownEvent event) {
        try {
            addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), event.getRenown());
        } catch (Exception ignore) {

        }
    }
}

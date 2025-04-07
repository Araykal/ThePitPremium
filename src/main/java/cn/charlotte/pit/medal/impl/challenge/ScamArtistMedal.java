package cn.charlotte.pit.medal.impl.challenge;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitPlayerUnlockPerkEvent;
import cn.charlotte.pit.event.PitPlayerUpgradePerkEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 23:22
 */
@AutoRegister
public class ScamArtistMedal extends AbstractMedal implements Listener {
    @Override
    public String getInternalName() {
        return "SCAM_ARTIST_UNLOCK";
    }

    @Override
    public String getDisplayName(int level) {
        return "商场欺诈术";
    }

    @Override
    public String getRequirementDescription(int level) {
        return "解锁精通天赋[商场欺诈术]";
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
        return 1;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void handleProfileLoaded(PlayerProfile profile) {
        if (PlayerUtil.isPlayerUnlockedPerk(Bukkit.getPlayer(profile.getPlayerUuid()), "ScamArtist")) {
            setProgress(profile, 1);
        }
    }

    @EventHandler
    public void onPerkUnlock(PitPlayerUnlockPerkEvent event) {
        if (event.getPerk().getInternalPerkName().equals("ScamArtist")) {
            setProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }

    @EventHandler
    public void onPerkUpgrade(PitPlayerUpgradePerkEvent event) {
        if (event.getPerk().getInternalPerkName().equals("ScamArtist")) {
            setProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }
    }
}

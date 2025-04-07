package cn.charlotte.pit.medal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.MedalData;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.RomanUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @Creator Misoryan
 * @Date 2021/6/8 20:14
 */
public abstract class AbstractMedal {

    public abstract String getInternalName();

    public abstract String getDisplayName(int level);

    public abstract String getRequirementDescription(int level);

    public abstract int getMaxLevel();

    public abstract int getProgressRequirement(int level);

    public abstract int getRarity(int level);

    public abstract boolean isHidden();

    public void setProgress(PlayerProfile profile, int progress) {
        MedalData medalData = profile.getMedalData();
        for (int i = 1; i <= getMaxLevel(); i++) {
            medalData.getMedalStatus(getInternalName(), i).setProgress(Math.min(progress, getProgressRequirement(i)));
            if (!medalData.getMedalStatus(getInternalName(), i).isUnlocked() && progress >= getProgressRequirement(i)) {
                medalData.getMedalStatus(getInternalName(), i).setUnlocked(true);
                medalData.getMedalStatus(getInternalName(), i).setFinishedTime(System.currentTimeMillis());
                Player player = Bukkit.getPlayer(profile.getPlayerUuid());
                if (player != null && player.isOnline()) {
                    player.sendMessage(CC.translate("&e&l成就解锁! &7你解锁了成就 &e" + getDisplayName(i) + (getMaxLevel() > 1 ? " " + RomanUtil.convert(i) : "") + " &7."));
                }
            }
        }
    }

    public void addProgress(PlayerProfile profile, int progress) {
        int newProgress = profile.getMedalData().getMedalStatus(getInternalName(), profile.getMedalData().getMedalLevel(getInternalName())).getProgress() + progress;
        setProgress(profile, newProgress);
    }

    public abstract void handleProfileLoaded(PlayerProfile profile);


}

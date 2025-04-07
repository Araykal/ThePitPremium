package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.quest.AbstractQuest;
import cn.charlotte.pit.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/21 14:50
 */
public class KeepSilence extends AbstractQuest {
    @Override
    public String getQuestInternalName() {
        return "KeepSilence";
    }

    @Override
    public String getQuestDisplayName() {
        return "缄默不言";
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinLevel() {
        return 2;
    }

    @Override
    public List<String> getQuestDescription(int level, boolean isNightQuest) {
        return Arrays.asList(
                "&f禁止在天赋栏装备&c天赋&f与&c连杀天赋");
    }

    @Override
    public long getDuration(int level) {
        return 5 * 60 * 1000;
    }

    @Override
    public int getTotal(int level) {
        return 10;
    }

    @Override
    public void onActive(Player player, int level) {
        super.onActive(player, level);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        player.sendMessage(CC.translate("&c已自动为你重置所有天赋栏."));
        for (PerkData perk : profile.getChosePerk().values()) {
            for (AbstractPerk abstractPerk : ThePit.getInstance()
                    .getPerkFactory()
                    .getPerks()) {
                if (abstractPerk.getInternalPerkName().equalsIgnoreCase(perk.getPerkInternalName())) {
                    abstractPerk.onPerkInactive(player);
                }
            }
        }
        for (int i = 1; i <= 8; i++) {
            if (i != 5) {
                profile.getChosePerk().remove(i);
            }
        }
    }

}

package cn.charlotte.pit.quest;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 20:52
 */
public abstract class AbstractQuest {
    private static final Random random = new Random();

    public abstract String getQuestInternalName();

    public abstract String getQuestDisplayName();

    public abstract int getMaxLevel();

    public abstract int getMinLevel();

    public abstract List<String> getQuestDescription(int level, boolean isNightQuest);

    public abstract long getDuration(int level);

    public abstract int getTotal(int level);

    public void onActive(Player player, int level) {
        long now = System.currentTimeMillis();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        QuestData questData = new QuestData();
        questData.setInternalName(this.getQuestInternalName());
        questData.setLevel(level);
        questData.setCurrent(0);
        questData.setStartTime(now);
        //replace the end time if it is a night quest
        if (profile.isNightQuestEnable() && TimeUtil.getMinecraftTick(now) > 12000) {
            questData.setEndTime(now + 36 * 60 * 1000 - now % (36 * 60 * 1000));
        } else {
            questData.setEndTime(now + this.getDuration(level));
        }
        questData.setTotal(getTotal(level));

        profile.setCurrentQuest(questData);
    }

    public void onInactive(Player player, int level) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        QuestData currentQuest = profile.getCurrentQuest();
        profile.setCurrentQuest(null);
        profile.setLastQuest(currentQuest);

        List<AbstractQuest> quests = ThePit.getInstance()
                .getQuestFactory()
                .getQuests();

        List<AbstractQuest> list = new ArrayList<>(quests);
        Collections.shuffle(list);

        profile.getCurrentQuestList().clear();

        for (int i = 0; i < 3; i++) {
            AbstractQuest quest = list.get(i);
            int questLevel = random.nextInt(quest.getMaxLevel() - quest.getMinLevel() + 1) + quest.getMinLevel();
            profile.getCurrentQuestList().add(quest.getQuestInternalName() + ":" + questLevel);
        }
    }

    public void onComplete(Player player, QuestData questData) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        int level = questData.getLevel();
        if (profile.isNightQuestEnable() && TimeUtil.getMinecraftTick(questData.getStartTime()) > 12000) {
            profile.setExperience(profile.getExperience() + level * 250);
            player.sendMessage(CC.translate("&7奖励已交付:"));
            player.sendMessage(CC.translate(" &8+ &b" + (level * 250) + " &7天坑乱斗经验值"));
            if (level > 1) {
                player.getInventory().addItem(ThePit.api.generateItem("ChunkOfVileItem"));
                profile.getQuestLimit().setTimes(profile.getQuestLimit().getTimes() + 1);
                player.sendMessage(CC.translate(" &8+ &51 暗聚块"));
            }
        } else {
            profile.setCoins(profile.getCoins() + level * 100);
            profile.grindCoins(level * 100);
            profile.setExperience(profile.getExperience() + level * 40);
            profile.setActionBounty(profile.getActionBounty() + 5 * level);


            player.sendMessage(CC.translate("&7奖励已交付:"));
            player.sendMessage(CC.translate(" &8+ &6" + (level * 100) + " &7硬币"));
            player.sendMessage(CC.translate(" &8+ &e" + (level * 5) + " &7行动赏金"));
            player.sendMessage(CC.translate(" &8+ &b" + (level * 40) + " &7天坑乱斗经验值"));
        }
        profile.applyExperienceToPlayer(player);

        this.onInactive(player, level);

        new BukkitRunnable() {
            int tick = 0;
            boolean shouldUp = false;

            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 0.6F + (tick * 0.05F));
                if (shouldUp) {
                    tick++;
                }
                if (!shouldUp) {
                    shouldUp = true;
                }
                if (tick >= 4) {
                    cancel();
                }
            }
        }.runTaskTimer(ThePit.getInstance(), 4, 4);
    }

    public enum QuestType {
        ALL,
        DAY_ONLY,
        NIGHT_ONLY
    }
}

package cn.charlotte.pit.menu.quest.main.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.event.PitQuestInactiveEvent;
import cn.charlotte.pit.quest.AbstractQuest;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 16:55
 */
public class QuestButton extends Button {
    private final AbstractQuest quest;
    private final int level;

    public QuestButton(AbstractQuest quest, int level) {
        this.quest = quest;
        this.level = level;
    }

    private static boolean isNightQuest(PlayerProfile profile, QuestData quest) {
        return profile.isNightQuestEnable() && TimeUtil.getMinecraftTick(quest.getStartTime()) > 12000;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.BOOK)
                .name("&a挑战任务: " + quest.getQuestDisplayName() + " " + RomanUtil.convert(level)).amount(level);

        List<String> lore = new ArrayList<>();
        lore.add("&8每日挑战");
        lore.add(" ");

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        int perkLevel = PlayerUtil.getPlayerUnlockedPerkLevel(player, "contractor_perk");
        if (profile.getCurrentQuest() != null) {
            QuestData currentQuest = profile.getCurrentQuest();
            if (currentQuest.getInternalName().equals(quest.getQuestInternalName()) && level == currentQuest.getLevel()) {
                if (currentQuest.getCurrent() >= currentQuest.getTotal()) {
                    lore.add("&7任务完成! 奖励如下:");
                    if (isNightQuest(profile, currentQuest)) {
                        lore.add(" &8+ &b" + (level * 250) + " &7天坑乱斗经验值");
                        if (level > 1) {
                            lore.add(" &8+ &51 暗聚块");
                        }
                    } else {
                        lore.add(" &8+ &6" + (level * 100) + " &7硬币");
                        lore.add(" &8+ &e" + (level * 5) + " &7行动赏金");
                        lore.add(" &8+ &b" + (level * 40) + " &7天坑乱斗经验值");
                    }
                    lore.add(" ");
                    lore.add("&a点击领取你的奖励!");
                } else {
                    addQuestTrack(profile, lore, currentQuest);
                    if (currentQuest.getEndTime() >= System.currentTimeMillis()) {
                        lore.add("&7剩余时间: &a" + TimeUtil.millisToTimer(currentQuest.getEndTime() - System.currentTimeMillis()));
                    } else {
                        lore.add("&c&l您已超时，无法继续该任务!");
                    }
                    if (isNightQuest(profile, currentQuest)) {
                        addNightReward(lore);
                    } else {
                        addReward(lore);
                    }
                    lore.add("&c点击放弃此任务及其奖励!");
                }
                builder.shiny();
            } else {
                lore.add("&7任务内容:");
                lore.add("  &7于&c附加条件&7下在规定时间内击杀指定数量玩家.");
                lore.add("&7附加条件:");
                for (String desc : quest.getQuestDescription(level, isNightQuest(profile, currentQuest))) {
                    lore.add("  " + desc);
                }
                lore.add(" ");
                lore.add("&7击杀要求: &a" + quest.getTotal(level) + " 击杀数");
                if (isNightQuest(profile, currentQuest)) {
                    addNightTimeLore(lore);
                    addNightReward(lore);
                } else {
                    addTimeLore(lore);
                    addReward(lore);
                }
                lore.add("&c你有其他正在进行中的任务!");
            }
        } else {
            lore.add("&7任务内容:");
            lore.add("  &7于&c附加条件&7下在规定时间内击杀指定数量玩家.");
            lore.add("&7附加条件:");
            for (String desc : quest.getQuestDescription(level, profile.isNightQuestEnable() && TimeUtil.getMinecraftTick() > 12000)) {
                lore.add("  " + desc);
            }
            lore.add(" ");
            lore.add("&7击杀要求: &a" + quest.getTotal(level) + " 不同玩家击杀");
            if (profile.isNightQuestEnable() && TimeUtil.getMinecraftTick() > 12000) {
                addNightTimeLore(lore);
                addNightReward(lore);
            } else {
                addTimeLore(lore);
                addReward(lore);
            }
            lore.add("&7今日剩余可完成任务数: &e" + (10 + perkLevel * 2 - profile.getQuestLimit().getTimes()));
            lore.add("&7每获得一个 &5暗聚块 &7,额外消耗一次可完成任务次数.");
            lore.add("");
            if (profile.getLastQuest() != null) {
                long cooldown = profile.getLastQuest().getStartTime() + (5 * 60 * 1000L);
                if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTier() >= 2) {
                    cooldown -= 60 * 1000L;
                }
                long now = System.currentTimeMillis();

                if (now < cooldown) {
                    builder = new ItemBuilder(Material.BARRIER)
                            .name("&c挑战任务: ???");
                    lore.clear();
                    lore.add("&8每日挑战");
                    lore.add(" ");
                    lore.add("&c新的任务还在路上!");
                    lore.add("&c要开始一个新的任务,请等待.");
                    lore.add("&c剩余时间: &6" + TimeUtil.millisToTimer(cooldown - now));
                } else {
                    lore.add("&a点击接取此任务!");
                }
            } else {
                lore.add("&a点击接取此任务!");
            }
        }


        if (profile.getQuestLimit().getTimes() >= 10 + 2 * perkLevel) {
            builder = new ItemBuilder(Material.BARRIER)
                    .name("&c挑战任务: ???");
            lore.clear();
            lore.add("&8每日挑战");
            lore.add(" ");
            lore.add("&c新的任务还在路上!");
            lore.add("&c要开始一个新的任务,请等待.");
            lore.add("&c剩余时间: &6" + TimeUtil.millisToTimer(TimeUtil.getMillisecondNextEarlyMorning()));
        }
        return builder.lore(lore).build();
    }

    private void addQuestTrack(PlayerProfile profile, List<String> lore, QuestData currentQuest) {

        lore.add("&7任务内容:");
        lore.add("  &7于&c附加条件&7下在规定时间内击杀指定数量玩家.");
        lore.add("&7附加条件:");
        for (String desc : quest.getQuestDescription(level, isNightQuest(profile, currentQuest))) {
            lore.add("  " + desc);
        }
        lore.add(" ");
        lore.add("&7任务进度: &a" + currentQuest.getCurrent() + "/" + currentQuest.getTotal() + " 击杀数");
    }

    private void addTimeLore(List<String> lore) {
        lore.add("&7限定时间: &a" + TimeUtil.millisToTimer(quest.getDuration(level)));
    }

    private void addNightTimeLore(List<String> lore) {
        lore.add("&7限定时间: &a在日出(" + (TimeUtil.millisToRoundedTime(36 * 60 * 1000 - (System.currentTimeMillis() % (36 * 60 * 1000)))) + "后)前完成");
    }

    private void addReward(List<String> lore) {
        lore.add(" ");
        lore.add("&7任务奖励:");
        lore.add(" &8+ &6" + (level * 100) + " &7硬币");
        lore.add(" &8+ &e" + (level * 5) + " &7行动赏金");
        lore.add(" &8+ &b" + (level * 40) + " &7天坑乱斗经验值");
        lore.add(" ");
    }

    private void addNightReward(List<String> lore) {
        lore.add(" ");
        lore.add("&7任务奖励:");
        lore.add(" &8+ &b" + (level * 250) + " &7天坑乱斗经验值");
        if (level > 1) {
            lore.add(" &8+ &51 暗聚块");
        }
        lore.add(" ");
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        int perkLevel = PlayerUtil.getPlayerUnlockedPerkLevel(player, "contractor_perk");

        if (profile.getQuestLimit().getTimes() >= 10 + 2 * perkLevel) {
            return;
        }
        QuestData currentQuest = profile.getCurrentQuest();
        QuestData lastQuest = profile.getLastQuest();
        if (currentQuest != null) {
            if (currentQuest.getInternalName().equals(quest.getQuestInternalName()) && level == currentQuest.getLevel()) {
                new PitQuestInactiveEvent(player, currentQuest).callEvent();
                if (currentQuest.getCurrent() >= currentQuest.getTotal()) {
                    quest.onComplete(player, currentQuest);
                    profile.getQuestLimit().setTimes(profile.getQuestLimit().getTimes() + 1);
                } else {
                    quest.onInactive(player, level);
                }
            } else {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            }
        } else {
            if (lastQuest != null) {
                long cooldown = profile.getLastQuest().getStartTime() + (5 * 60 * 1000L);
                if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTier() >= 2) {
                    cooldown -= 60 * 1000L;
                }
                long now = System.currentTimeMillis();

                if (now < cooldown) {
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                } else {
                    quest.onActive(player, level);
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.5F);
                }
            } else {
                quest.onActive(player, level);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.5F);
            }
        }


    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

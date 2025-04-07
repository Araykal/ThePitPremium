package cn.charlotte.pit.menu.quest.main;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.medal.MedalEntranceButton;
import cn.charlotte.pit.menu.quest.main.button.QuestButton;
import cn.charlotte.pit.menu.quest.main.button.QuestIntroduceButton;
import cn.charlotte.pit.menu.quest.main.button.QuestSanctuaryButton;
import cn.charlotte.pit.quest.AbstractQuest;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 16:52
 */
public class QuestMenu extends Menu {
    private static final int[] slots = {29, 31, 33};
    private final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(PATTEN_DEFAULT_YMD);

    @Override
    public String getTitle(Player player) {
        return "任务 & 挑战";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (true) {

            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

            long now = System.currentTimeMillis();
            long date = profile.getQuestLimit().getLastRefresh();
            //获取今天的日期
            String nowDay = dateFormat.format(now);
            //对比的时间
            String day = dateFormat.format(date);

            //daily reset
            if (!day.equals(nowDay)) {
                profile.getQuestLimit().setLastRefresh(now);
                profile.getQuestLimit().setTimes(0);
            }

            buttons.put(4, new QuestIntroduceButton());

            buttons.put(22, new QuestSanctuaryButton());

            if (profile.getCurrentQuest() != null) {
                AbstractQuest abstractQuest = ThePit.getInstance()
                        .getQuestFactory()
                        .getQuestMap()
                        .get(profile.getCurrentQuest().getInternalName());
                if (abstractQuest != null) {
                    buttons.put(31, new QuestButton(abstractQuest, profile.getCurrentQuest().getLevel()));
                }
            } else {
                int i = 0;
                for (String internal : profile.getCurrentQuestList()) {
                    String[] split = internal.split(":");
                    String questName = split[0];
                    int level = Integer.parseInt(split[1]);
                    AbstractQuest abstractQuest = ThePit.getInstance()
                            .getQuestFactory()
                            .getQuestMap()
                            .get(questName);
                    buttons.put(slots[i], new QuestButton(abstractQuest, level));
                    i++;
                }
            }

            //buttons.put(26, new QuestCancelButton());
            buttons.put(36, new MedalEntranceButton());
            if (PlayerUtil.isPlayerUnlockedPerk(player, "heresy_perk")) {
                buttons.put(44, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.COAL)
                                .name("&a邪术")
                                .lore(
                                        "&7你已获得了对抗神话附魔师的力量.",
                                        "",
                                        "&e点击查看此界面!"
                                )
                                .build();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                        ThePit.api.openMenu(player, "heresy");
                    }
                });
            }
        } else {
            buttons.put(13, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.ANVIL)
                            .name("&c此玩法正在开发中!")
                            .lore("&7你暂时无法访问此内容,", "&7此玩法会在晚些时候开放.")
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

                }
            });
        }
        return buttons;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        //return 5 * 9;
        return 5 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }
}

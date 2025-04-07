package cn.charlotte.pit.menu.medal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import cn.charlotte.pit.util.menu.menus.PagedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 9:25
 */
public class MedalMainMenu extends Menu {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm");
    private static final DecimalFormat df = new DecimalFormat(",###,###,###,###");

    public static Button getBackButton() {
        return new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("&a返回")
                        .lore("&7天坑乱斗成就菜单")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                new MedalMainMenu().openMenu(player);
            }
        };
    }

    public static List<Button> getMedalButtons(PlayerProfile profile, List<AbstractMedal> medals) {
        List<Button> unlockedButtons = new ArrayList<>();
        List<Button> lockedButtons = new ArrayList<>();
        for (AbstractMedal medal : medals) {
            for (int i = 1; i <= medal.getMaxLevel(); i++) {
                ItemBuilder builder = new ItemBuilder(Material.AIR);
                List<String> lines = new ArrayList<>();
                for (String s : medal.getRequirementDescription(i).split("/s")) {
                    lines.add("&f" + s);
                }
                lines.add("");
                if (profile.getMedalData().getMedalStatus(medal.getInternalName(), i).isUnlocked()) {
                    builder.material(Material.DIAMOND);
                    builder.amount(medal.getRarity(i) * 5);
                    if (medal.isHidden()) {
                        builder.shiny();
                    }
                    builder.name("&a" + medal.getDisplayName(i) + (medal.getMaxLevel() > 1 ? " " + RomanUtil.convert(i) : ""));
                    if (medal.getProgressRequirement(i) > 1) {
                        lines.add("&7进度: &a完成! &7(&a" + df.format(profile.getMedalData().getMedalStatus(medal.getInternalName(), i).getProgress()) + "&7)");
                    }
                    lines.add("&7完成于 " + dateFormat.format(profile.getMedalData().getMedalStatus(medal.getInternalName(), i).getFinishedTime()) + " 。");
                    lines.add("");
                    lines.add("&a成就已解锁!");
                    unlockedButtons.add(new DisplayButton(builder.lore(lines).build(), true));
                } else if (!medal.isHidden()) {
                    builder.material(Material.COAL);
                    builder.amount(medal.getRarity(i) * 5);
                    builder.name("&c" + medal.getDisplayName(i) + (medal.getMaxLevel() > 1 ? " " + RomanUtil.convert(i) : ""));
                    if (medal.getProgressRequirement(i) > 1) {
                        lines.add("&7进度: &a" + df.format(profile.getMedalData().getMedalStatus(medal.getInternalName(), i).getProgress()) + "&7/&a" + df.format(medal.getProgressRequirement(i)));
                        lines.add("");
                    }
                    lines.add("&c成就尚未解锁!");
                    lockedButtons.add(new DisplayButton(builder.lore(lines).build(), true));
                }
            }
        }
        unlockedButtons.addAll(lockedButtons);
        return unlockedButtons;
    }

    @Override
    public String getTitle(Player player) {
        return "天坑乱斗成就";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.DIAMOND);
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                builder.name("&a挑战成就");
                List<String> lines = new ArrayList<>();
                lines.add("&8天坑乱斗");
                int unlockedMedals = 0;
                int totalMedals = 0;
                int unlockedHiddenMedals = 0;
                for (AbstractMedal medal : ThePit.getInstance().getMedalFactory().getMedals()) {
                    if (medal.getMaxLevel() == 1) {
                        if (!medal.isHidden()) {
                            totalMedals++;
                        }
                        if (profile.getMedalData().getUnlockedMedals().containsKey(medal.getInternalName() + "#1")) {
                            if (medal.isHidden()) {
                                unlockedHiddenMedals++;
                            } else {
                                unlockedMedals++;
                            }
                        }
                    }
                }
                lines.add("");
                lines.add("&7已解锁: &b" + unlockedMedals + "&7/&b" + totalMedals);
                lines.add("&7隐藏成就解锁数: &b" + unlockedHiddenMedals);
                lines.add("");
                lines.add("&7挑战成就可一次性完成。");
                lines.add("");
                lines.add("&e点击查看成就!");
                builder.lore(lines);
                return builder.build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                List<AbstractMedal> medals = new ArrayList<>(ThePit.getInstance().getMedalFactory().getMedals());
                medals.removeIf(medal -> medal.getMaxLevel() > 1);
                Map<Integer, Button> button = new HashMap<>();
                button.put(49, getBackButton());
                new PagedMenu("天坑乱斗挑战成就", getMedalButtons(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()), medals), button).openMenu(player);
            }
        });
        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.DIAMOND);
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                builder.name("&a分级成就");
                List<String> lines = new ArrayList<>();
                lines.add("&8天坑乱斗");
                int unlockedMedals = 0;
                int totalMedals = 0;
                int unlockedHiddenMedals = 0;
                for (AbstractMedal medal : ThePit.getInstance().getMedalFactory().getMedals()) {
                    if (medal.getMaxLevel() > 1) {
                        for (int i = 1; i <= medal.getMaxLevel(); i++) {
                            totalMedals++;
                            if (profile.getMedalData().getUnlockedMedals().containsKey(medal.getInternalName() + "#" + i)) {
                                unlockedMedals++;
                            }
                        }
                    }
                }
                lines.add("");
                lines.add("&7已解锁: &b" + unlockedMedals + "&7/&b" + totalMedals);
                lines.add("");
                lines.add("&7分级成就含有不同阶段。");
                lines.add("");
                lines.add("&e点击查看成就!");
                builder.lore(lines);
                return builder.build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                List<AbstractMedal> medals = new ArrayList<>(ThePit.getInstance().getMedalFactory().getMedals());
                medals.removeIf(medal -> medal.getMaxLevel() <= 1);
                Map<Integer, Button> button = new HashMap<>();
                button.put(49, getBackButton());
                new PagedMenu("天坑乱斗分级成就", getMedalButtons(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()), medals), button).openMenu(player);
            }
        });
        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}

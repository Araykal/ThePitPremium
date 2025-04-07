package cn.charlotte.pit.menu.mail;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.mail.Mail;
import cn.charlotte.pit.menu.mail.button.MailClaimButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/16 23:35
 */
public class MailMenu extends Menu {
    private static final int[] slot = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private int currentPage = 0;
    private int totalPage = 0;

    @Override
    public String getTitle(Player player) {
        final List<Mail> mails = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                .getMailData()
                .getMails();
        totalPage = mails.size() / 28;

        return "邮件 (第" + (currentPage + 1) + "/" + (totalPage + 1) + "页) ";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        final List<Mail> mails = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                .getMailData()
                .getMails()
                .stream()
                .sorted(Comparator.comparingLong(mail -> (-mail.getExpireTime()) + (mail.isClaimed() ? +1000000000000L : 0)))
                .collect(Collectors.toList());

        for (int i = 0; i < 28; i++) {
            final int index = i + currentPage * 28;
            if (index >= mails.size()) {
                break;
            }
            final Mail mail = mails.get(index);

            buttonMap.put(slot[i], new MailClaimButton(this, mail));
            buttonMap.put(48, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.ARROW)
                            .name("&f上一页")
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                    currentPage--;
                    if (currentPage < 0) {
                        currentPage = 0;
                    }
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });
            buttonMap.put(50, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.ARROW)
                            .name("&f下一页")
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                    currentPage++;
                    if (currentPage >= totalPage) {
                        currentPage = totalPage;
                    }
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });
        }

        return buttonMap;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }
}

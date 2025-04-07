package cn.charlotte.pit.menu.mail.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.mail.Mail;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/25 21:23
 */
public class MailClaimButton extends Button {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private final Menu mainMenu;
    private final Mail mail;

    public MailClaimButton(Menu mainMenu, Mail mail) {
        this.mainMenu = mainMenu;
        this.mail = mail;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        final ItemBuilder builder = new ItemBuilder(mail.isClaimed() ? Material.HOPPER_MINECART : Material.STORAGE_MINECART);
        builder.name(CC.translate(mail.getTitle() == null ? "无标题" : mail.getTitle() + (mail.isClaimed() ? " &7#已读" : "")));
        List<String> lore = new ArrayList<>();
        lore.add("");
        if (mail.getCoins() != 0) {
            lore.add("&6" + mail.getCoins() + " 硬币");
        }
        if (mail.getExp() != 0) {
            lore.add("&b" + mail.getExp() + " 经验值");
        }
        if (mail.getRenown() != 0) {
            lore.add("&e" + mail.getRenown() + " 声望");
        }
        if (mail.getItem().getContents() != null && mail.getItem().getContents().length > 0) {
            for (ItemStack content : mail.getItem().getContents()) {
                if (content != null) {
                    lore.add("&7" + content.getAmount() + "x &f" + (content.getItemMeta().getDisplayName() == null ? content.getType().name() : content.getItemMeta().getDisplayName()));
                }
            }
        }
        lore.add("&8&l&m" + CC.stripColor(CC.MENU_BAR));
        if (mail.getContent() != null) {
            lore.add("&7邮件信息: ");
            lore.addAll(Arrays.asList(mail.getContent().split("\\\\n")));
            lore.add("&8&l&m" + CC.stripColor(CC.MENU_BAR));
        }
        lore.add("&7邮件到期时间: ");
        final long now = System.currentTimeMillis();
        if (mail.getExpireTime() > now) {
            lore.add("&e" + TimeUtil.millisToRoundedTime(mail.getExpireTime() - now) + "后");
        } else {
            lore.add("&c邮件已过期" + (mail.isClaimed() ? ",无法领取邮件内物品" : ""));
        }
        lore.add("&8&l&m" + CC.stripColor(CC.MENU_BAR));


        if (!mail.isClaimed() && mail.getExpireTime() > now) {
            lore.add("");
            lore.add("&e点击领取此邮件内全部物品!");
            builder.shiny();
        } else {
            lore.add("");
            lore.add("&e右键点击以删除该邮件!");
        }

        builder.lore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (clickType == ClickType.RIGHT && (mail.isClaimed() || mail.getExpireTime() < System.currentTimeMillis())) {
            try {
                final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

                profile.getMailData().getMails().removeIf(mailObj -> mailObj.getUuid().equals(mail.getUuid()));
                Bukkit.getScheduler().runTaskAsynchronously(ThePit.getInstance(), () -> {
                    profile.getMailData().save();
                });

                mainMenu.setClosedByMenu(true);
                mainMenu.openMenu(player);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            } catch (Exception e) {
                CC.printErrorWithCode(player, e);
            }
            return;
        }
        if (clickType != ClickType.LEFT) return;
        if (mail.isClaimed()) {
            player.sendMessage(CC.translate("&c此邮件已被领取!"));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            return;
        }
        if (mail.getExpireTime() < System.currentTimeMillis()) {
            player.sendMessage(CC.translate("&c此邮件已过期!"));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            return;
        }

        final int mailSlots = InventoryUtil.getInventoryFilledSlots(mail.getItem().getContents());
        if (mailSlots > 0) {
            final int slots = InventoryUtil.getInventoryEmptySlots(player.getInventory().getContents());
            if (slots < mailSlots) {
                player.sendMessage(CC.translate("&c你的背包空间不足,无法领取此邮件!"));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                return;
            }
        }

        mail.setClaimed(true);

        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        mail.setClaimed(true);
        Bukkit.getScheduler()
                .runTaskAsynchronously(ThePit.getInstance(), () -> {
                    try {
                        player.sendMessage(CC.translate("&7正在领取邮件..."));
                        profile.getMailData().save();

                        profile.grindCoins(mail.getCoins());
                        profile.setCoins(profile.getCoins() + mail.getCoins());
                        profile.setExperience(profile.getExperience() + mail.getExp());
                        profile.setRenown(profile.getRenown() + mail.getRenown());
                        profile.applyExperienceToPlayer(player);
                        for (ItemStack content : mail.getItem().getContents()) {
                            if (content != null) {
                                player.getInventory().addItem(content);
                            }
                        }
                        mainMenu.setClosedByMenu(true);
                        mainMenu.openMenu(player);

                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                        player.sendMessage(CC.translate("&a领取成功!"));
                    } catch (Exception e) {
                        CC.printErrorWithCode(player, e);
                    }
                });
    }
}

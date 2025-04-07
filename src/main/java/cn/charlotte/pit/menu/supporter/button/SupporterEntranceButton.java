package cn.charlotte.pit.menu.supporter.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.api.PointsAPI;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.impl.PlayerPointsAPIImpl;
import cn.charlotte.pit.medal.impl.challenge.hidden.SupporterMedal;
import cn.charlotte.pit.menu.supporter.SupporterMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.menus.ConfirmMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/4 10:17
 */
public class SupporterEntranceButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        final int price = NewConfiguration.INSTANCE.getVipPrice();

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("&7展示您对天坑乱斗游戏的支持,");
        lines.add("&7并解锁专属的会员权益!");
        lines.add("");
        lines.add("&7会员权益:");
        lines.add("&e◼ &7可开关的 &e✬ &7会员标志显示");
        lines.add("&e◼ &7/events 查看范围两天内的&d事件&7排布表");
        lines.add("&e◼ &7/show 展示手中物品给房间内全部玩家");
        lines.add("&e◼ &7将 &c神&6话&e之&a甲 &7以专属染料进行染色");
        lines.add("&e◼ &7可自选的隐藏自身档案 (无法被他人/view查看)");
        lines.add("");
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        try {

            PointsAPI pointsAPI = ThePit.getInstance().getPointsAPI();
            if (pointsAPI == null) {
                pointsAPI = PlayerPointsAPIImpl.INSTANCE;
            }

            if (profile.isSupporter()) {
                lines.add("&a&l感谢您的购买!");
            } else {
                lines.add("&7价格: &d" + price + " " + NewConfiguration.INSTANCE.getPriceName()); //remember to edit the price
                lines.add("&7你目前持有: &d" + pointsAPI.getPoints(player) + " " + NewConfiguration.INSTANCE.getPriceName());
                lines.add(" ");
                lines.add("&8当会员权益增加新内容时,");
                lines.add("&8已购买的用户会自动获得新的会员权益.");
            }
            lines.add(" ");
            if (profile.isSupporter()) {
                lines.add("&e点击调整相关设定!");
            } else {
                lines.add((pointsAPI.hasPoints(player, price) ? "&a点击购买!" : "&c您没有足够的" + NewConfiguration.INSTANCE.getPriceName() + "!")); //check if player have enough points
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ItemBuilder(Material.BEACON)
                .name("&e天坑乱斗会员")
                .lore(lines)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        final int price = NewConfiguration.INSTANCE.getVipPrice();

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.isSupporter()) {
            new SupporterMenu().openMenu(player);
        } else {
            try {

                final PointsAPI implemenPointsAPI = ThePit.getInstance().getPointsAPI();
                final PointsAPI pointsAPI;
                if (implemenPointsAPI == null) {
                    pointsAPI = PlayerPointsAPIImpl.INSTANCE;
                } else {
                    pointsAPI = implemenPointsAPI;
                }

                boolean haveEnoughPoints = pointsAPI.hasPoints(player, price);

                if (!haveEnoughPoints) { //do nothing if player have not enough points
                    return;
                }
                new ConfirmMenu("确认购买: 天坑乱斗会员 (" + price + " " + NewConfiguration.INSTANCE.getPriceName() + ")", confirm -> {
                    if (confirm) {
                        //remove player's points
                        pointsAPI.costPoints(player, price);

                        profile.setSupporter(true);
                        CC.boardCast("&6&l惊了! " + profile.getFormattedNameWithRoman() + " &7购买了 &e天坑会员 &7! &a十分感谢!");
                        new SupporterMedal().setProgress(profile, 1);
                    }
                }, true, 5).openMenu(player);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(CC.translate("&c获取" + NewConfiguration.INSTANCE.getPriceName() + "信息失败,请重试!"));
            }
        }
    }
}

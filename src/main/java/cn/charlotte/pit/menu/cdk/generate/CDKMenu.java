package cn.charlotte.pit.menu.cdk.generate;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.CDKData;
import cn.charlotte.pit.data.FixedRewardData;
import cn.charlotte.pit.listener.MailSendListener;
import cn.charlotte.pit.menu.cdk.generate.button.CDKEditButton;
import cn.charlotte.pit.menu.cdk.generate.button.CDKEditItemsButton;
import cn.charlotte.pit.menu.cdk.generate.button.CDKEditStringButton;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.ChatComponentBuilder;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.time.TimeUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/17 21:44
 */
public class CDKMenu extends Menu {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    private final CDKData data = new CDKData();

    @Override
    public String getTitle(Player player) {
        return "CDK生成";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>(3 * 9);

        buttonMap.put(10, new CDKEditButton(new ItemBuilder(Material.GOLD_BLOCK)
                .name("&e声望")
                .lore(Arrays.asList(" ", "&7当前: &e" + data.getRenown() + " 声望", "&e点击编辑声望数量!"))
                .build(),
                this,
                data::setRenown));
        buttonMap.put(11, new CDKEditButton(new ItemBuilder(Material.GOLD_INGOT)
                .name("&6硬币")
                .lore(Arrays.asList(" ", "&7当前: &6" + data.getCoins() + " 硬币", "&e点击编辑硬币数量!"))
                .build(),
                this,
                data::setCoins));
        buttonMap.put(12, new CDKEditButton(new ItemBuilder(Material.EXP_BOTTLE)
                .name("&b经验值")
                .lore(Arrays.asList(" ", "&7当前: &b" + data.getExp() + " 经验值", "&e点击编辑经验值数量!"))
                .build(),
                this,
                data::setExp));

        buttonMap.put(13, new CDKEditItemsButton(new ItemBuilder(Material.DIAMOND_SWORD)
                .name("&a物品")
                .lore(Arrays.asList(" ", "&7当前: &a" + InventoryUtil.getInventoryFilledSlots(data.getItem().getContents()) + " 物品", "&e点击编辑物品!"))
                .build(),
                this,
                new ArrayList<>(Arrays.asList(data.getItem().getContents())),
                element -> {
                    data.getItem().setContents(element.toArray(new ItemStack[36]));
                }
        ));


        buttonMap.put(35, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NAME_TAG).name("生成").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                Bukkit.getScheduler().runTaskAsynchronously(ThePit.getInstance(), () -> {
                    if (data.getCdk() == null) {
                        data.setCdk(UUID.randomUUID().toString().replace("-", ""));
                    }

                    player.sendMessage(CC.translate("CDK信息: "));
                    player.sendMessage(CC.translate("经验: " + data.getExp() + " 金币: " + data.getCoins() + " 声望: " + data.getRenown()));
                    player.sendMessage(CC.translate("限制等级: " + data.getLimitLevel()));
                    player.sendMessage(CC.translate("限制权限: " + data.getLimitPermission()));
                    player.sendMessage(CC.translate("物品: " + InventoryUtil.getInventoryFilledSlots(data.getItem().getContents())));
                    final BaseComponent[] chat = new ChatComponentBuilder(CC.translate("&e[复制CDK]"))
                            .setCurrentClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, data.getCdk()))
                            .create();

                    player.spigot().sendMessage(chat);

                    data.active();
                    player.closeInventory();

                });
            }
        });

        buttonMap.put(14, new CDKEditStringButton(
                new ItemBuilder(Material.SIGN).name("&c有效时间").lore("&7过期时间: " + format.format(data.getExpireTime())).build(),
                this,
                element -> {
                    data.setExpireTime(System.currentTimeMillis() + TimeUtil.parseTime(element));
                }
        ));

        buttonMap.put(15, new CDKEditStringButton(
                new ItemBuilder(Material.SIGN).name("&c领取权限").lore("&7当前限制权限: " + data.getLimitPermission()).build(),
                this,
                data::setLimitPermission
        ));

        buttonMap.put(16, new CDKEditButton(
                new ItemBuilder(Material.SIGN).name("&c限制等级").lore("&7当前限制等级: " + data.getLimitLevel()).build(),
                this,
                data::setLimitLevel
        ));

        buttonMap.put(19, new CDKEditButton(
                new ItemBuilder(Material.BOOK).name("&c限制领取次数").lore("&7当前限制次数: " + data.getLimitClaimed()).build(),
                this,
                data::setLimitClaimed
        ));
        buttonMap.put(20, new CDKEditButton(
                new ItemBuilder(Material.DIAMOND_BLOCK).name("&c限制精通等级").lore("&7当前限制精通等级: " + data.getLimitPrestige()).build(),
                this,
                data::setLimitPrestige
        ));
        buttonMap.put(34, new CDKEditStringButton(new ItemBuilder(Material.NAME_TAG)
                .name("&a自定义CDK")
                .lore(data.getCdk() == null ? "&7随机生成" : "&7当前CDK: &e" + data.getCdk())
                .build(),
                this,
                data::setCdk
        ));

        buttonMap.put(33, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.COMMAND_MINECART).name("&a作为邮件发送").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                FixedRewardData rewardData = new FixedRewardData();
                rewardData.setData(data);
                MailSendListener.getEditing().put(player.getUniqueId(), rewardData);

                player.sendMessage(CC.translate("&a请输入邮件标题"));
            }
        });

        return buttonMap;
    }

    @Override
    public int getSize() {
        return 4 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public CDKData getData() {
        return this.data;
    }
}

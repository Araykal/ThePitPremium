package cn.charlotte.pit.command;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.CDKData;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.mail.Mail;
import cn.charlotte.pit.data.sub.KillRecap;
import cn.charlotte.pit.data.sub.OfferData;
import cn.charlotte.pit.data.temp.TradeRequest;
import cn.charlotte.pit.event.PitPlayerSpawnEvent;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.game.runnable.TradeMonitorRunnable;
import cn.charlotte.pit.map.kingsquests.ui.CakeBakeUI;
import cn.charlotte.pit.map.kingsquests.ui.KingQuestsUI;
import cn.charlotte.pit.menu.event.previewer.EventPreviewerMenu;
import cn.charlotte.pit.menu.offer.OfferMenu;
import cn.charlotte.pit.menu.option.PlayerOptionMenu;
import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.menu.trade.TradeMenu;
import cn.charlotte.pit.menu.viewer.StatusViewerMenu;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.ChatComponentBuilder;
import cn.charlotte.pit.util.command.Command;
import cn.charlotte.pit.util.command.param.Parameter;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.rank.RankUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.upperlevel.spigot.book.BookUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 16:46
 */
public class PitCommand {
    private final Random random = new Random();
    private final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
    private final DecimalFormat numFormat = new DecimalFormat("0.00");
    private final Map<UUID, Cooldown> COOLDOWN_SHOW = new HashMap<>();

    @Command(
            names = {
                    "option",
                    "options",
                    "opt",
                    "setting",
                    "settings"
            }
    )
    public void openOption(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getPrestige() > 0 || profile.getLevel() >= 5) {
            new PlayerOptionMenu().openMenu(player);
        } else {
            player.sendMessage(CC.translate("&c&l等级不足! &7此指令在 " + LevelUtil.getLevelTag(profile.getPrestige(), 5) + " &7时解锁."));
        }
    }

    private final Cache<UUID, Long> viewCooldown = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    @Command(
            names = "view"
    )
    public void viewProfile(Player player, @Parameter(name = "查询目标") String id) {
        final Long present = viewCooldown.getIfPresent(player.getUniqueId());
        if (present != null) {
            player.sendMessage(CC.translate("&c冷却中..."));
            return;
        }

        viewCooldown.put(player.getUniqueId(), System.currentTimeMillis());

        try {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if (profile.getLevel() < 70 && profile.getPrestige() < 1) {
                player.sendMessage(CC.translate("&c&l等级不足! &7此指令在 " + LevelUtil.getLevelTag(profile.getPrestige(), 70) + " &7时解锁."));
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously(ThePit.getInstance(), () -> {
                PlayerProfile targetProfile = PlayerProfile.getOrLoadPlayerProfileByName(id);
                if (targetProfile == null) {
                    player.sendMessage(CC.translate("&c此玩家的档案不存在,请检查输入是否有误."));
                    return;
                }
                Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> new StatusViewerMenu(targetProfile).openMenu(player));
            });

        } catch (Exception e) {
            if (player.hasPermission("pit.admin")) {
                CC.printError(player, e);
            }
        }
    }

    @Command(
            names = "events"
    )
    public void previewEvents(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isSupporter() && !PlayerUtil.isStaff(player)) {
            player.sendMessage(CC.translate("&c你需要购买 &e天坑乱斗会员 &c才可以使用此指令!"));
            return;
        }
        new EventPreviewerMenu().openMenu(player);
    }

    @Command(
            names = "show"
    )
    public void show(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isSupporter() && !PlayerUtil.isStaff(player)) {
            player.sendMessage(CC.translate("&c你需要购买 &e天坑乱斗会员 &c才可以使用此指令!"));
            return;
        }
        COOLDOWN_SHOW.putIfAbsent(player.getUniqueId(), new Cooldown(0));
        if (!COOLDOWN_SHOW.get(player.getUniqueId()).hasExpired() && !player.hasPermission("thepit.admin")) {
            player.sendMessage(CC.translate("此指令仍在冷却中: " + TimeUtil.millisToTimer(COOLDOWN_SHOW.get(player.getUniqueId()).getRemaining())));
            return;
        }
        COOLDOWN_SHOW.put(player.getUniqueId(), new Cooldown(60, TimeUnit.SECONDS));
        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
            player.sendMessage(CC.translate("&c请先手持要展示的物品!"));
            return;
        }
        if (player.getItemInHand().getItemMeta().getDisplayName() == null && !player.hasPermission("pit.admin")) {
            player.sendMessage(CC.translate("&c此物品无法被用于展示!"));
            return;
        }
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(player.getItemInHand());
        NBTTagCompound tag = new NBTTagCompound();
        nms.save(tag);
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(tag.toString())
        };
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(new ChatComponentBuilder(CC.translate("&a&l物品展示! &7" + profile.getFormattedName() + " &7正在展示物品: &f" + (player.getItemInHand().getItemMeta().getDisplayName() == null ? player.getItemInHand().getType().name() : player.getItemInHand().getItemMeta().getDisplayName()) + " &e[查看]"))
                    .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents)).create());
        }
    }

    @Command(
            names = "tradeLimits"
    )
    public void sendTradeLimits(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        long now = System.currentTimeMillis();
        long date = profile.getTradeLimit().getLastRefresh();
        //获取今天的日期
        String nowDay = dateFormat.format(now);
        //对比的时间
        String day = dateFormat.format(date);

        //daily reset
        if (!day.equals(nowDay) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 4) {
            profile.getTradeLimit().setLastRefresh(now);
            profile.getTradeLimit().setAmount(0);
            profile.getTradeLimit().setTimes(0);
        }
        player.sendMessage(CC.translate("&6&l每日交易限制! &7(每日4:00 AM重置)"));
        player.sendMessage(CC.translate("&7每日硬币交易上限: " + (profile.getTradeLimit().getAmount() >= 50000 ? "&c" : "&a") + profile.getTradeLimit().getAmount() + "&7/&650000g"));
        player.sendMessage(CC.translate("&7每日交易次数上限: &e" + profile.getTradeLimit().getTimes() + "/25"));
    }

    @Command(
            names = "trade"
    )
    public void onRequestTrade(Player player, @Parameter(name = "玩家") Player target) {
        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.translate("&c你无法选择此玩家进行交易!"));
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PlayerProfile targetProfile = PlayerProfile.getPlayerProfileByUuid(target.getUniqueId());
        if (!profile.getCombatTimer().hasExpired()) {
            player.sendMessage(CC.translate("&c你无法在战斗中使用此功能!"));
            return;
        }

        // 当前时间
        long now = System.currentTimeMillis();
        long date = profile.getTradeLimit().getLastRefresh();
        //获取今天的日期
        String nowDay = dateFormat.format(now);
        //对比的时间
        String day = dateFormat.format(date);

        //daily reset
        if (!day.equals(nowDay) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 4) {
            profile.getTradeLimit().setLastRefresh(now);
            profile.getTradeLimit().setAmount(0);
            profile.getTradeLimit().setTimes(0);
        }

        if (profile.getTradeLimit().getTimes() >= 25) {
            player.sendMessage(CC.translate("&c你今天的交易次数已经达到上限! (25/25)"));
            player.sendMessage(CC.translate("&c使用 &e/tradeLimits &c查看你的今日交易上限情况."));
            return;
        }

        if (!player.getName().equalsIgnoreCase(player.getDisplayName())) {
            player.sendMessage(CC.translate("&c你无法在匿名模式下使用交易功能!"));
            return;
        }

        if (profile.getLevel() < 60) {
            player.sendMessage(CC.translate("&c&l等级不足! &7此指令在 " + LevelUtil.getLevelTag(profile.getPrestige(), 60) + " &7时解锁."));
            return;
        }

        for (TradeRequest tradeRequest : TradeMonitorRunnable.getTradeRequests()) {
            if (tradeRequest.getTarget().equals(player) && tradeRequest.getPlayer().equals(target)) {
                TradeManager tradeManager = new TradeManager(player, target);
                new TradeMenu(tradeManager).openMenu(player);
                new TradeMenu(tradeManager).openMenu(target);
                return;
            }
            if (tradeRequest.getTarget().equals(target) && tradeRequest.getPlayer().equals(player)) {
                player.sendMessage(CC.translate("&c你已经发送过请求了,请等待对方接受!"));
                return;
            }
        }

        TradeMonitorRunnable.getTradeRequests().add(new TradeRequest(player, target));

        if (!targetProfile.getPlayerOption().isTradeNotify() && !player.hasPermission(PlayerUtil.getStaffPermission())) {
            player.sendMessage(CC.translate("&c对方在游戏选项之后中设置了不接受交易请求,因此无法查看你的请求提示."));
            player.sendMessage(CC.translate("&c但对方仍可以通过使用 &e/trade " + player.getName() + " &c以同意你的请求."));
            return;
        } else {
            player.sendMessage(CC.translate("&a&l交易请求发送! &7成功向 " + LevelUtil.getLevelTag(targetProfile.getPrestige(), targetProfile.getLevel()) + " " + RankUtil.getPlayerColoredName(target.getUniqueId()) + " &7发送了交易请求!"));
        }

        target.spigot()
                .sendMessage(new ChatComponentBuilder(CC.translate("&6&l交易请求! " + LevelUtil.getLevelTag(profile.getPrestige(), profile.getLevel()) + " " + RankUtil.getPlayerColoredName(player.getUniqueId()) + " &7向你发送了交易请求,请"))
                        .append(new ChatComponentBuilder(CC.translate(" &6&o点击这里")).setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentBuilder(CC.translate("&6点击以接受")).create())).setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade " + player.getName())).create())
                        .append(CC.translate(" &r&7以接受交易请求."))
                        .create());
    }

    @Command(
            names = {
                    "respawn",
                    "spawn",
                    "home",
                    "back"
            }
    )
    public void onSpawn(Player player) {
        if (player.hasMetadata("backing")) {
            player.sendMessage(CC.translate("&c&l已有一个计划中的回城..."));
            return;
        }

        if (!PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                .getCombatTimer()
                .hasExpired()) {
            player.sendMessage(CC.translate("&c&l您无法在战斗中传送！"));
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(CC.translate("&c&l您无法在当前状态下传送！"));
            return;
        }

        //player.sendMessage(CC.translate("&c&l即将传送,请保持脱战状态并不要移动位置..."));
        player.setMetadata("backing", new FixedMetadataValue(ThePit.getInstance(), true));

        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            if (player.isOnline()) {
                if (player.hasMetadata("backing")) {
                    Location location = ThePit.getInstance().getPitConfig()
                            .getSpawnLocations()
                            .get(random.nextInt(ThePit.getInstance().getPitConfig().getSpawnLocations().size()));

                    player.removeMetadata("backing", ThePit.getInstance());

                    player.teleport(location);

                    for (ItemStack item : player.getInventory()) {
                        if (ItemUtil.isRemovedOnJoin(item)) {
                            player.getInventory().remove(item);
                        }
                    }

                    if (ItemUtil.isRemovedOnJoin(player.getInventory().getHelmet())) {
                        player.getInventory().setHelmet(new ItemStack(Material.AIR));
                    }

                    if (ItemUtil.isRemovedOnJoin(player.getInventory().getChestplate())) {
                        player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    }

                    if (ItemUtil.isRemovedOnJoin(player.getInventory().getLeggings())) {
                        player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    }

                    if (ItemUtil.isRemovedOnJoin(player.getInventory().getBoots())) {
                        player.getInventory().setBoots(new ItemStack(Material.AIR));
                    }

                    PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                    profile.setStreakKills(0);
                    profile.setInArena(false);

                    new PitPlayerSpawnEvent(player).callEvent();

                    PlayerUtil.clearPlayer(player, true, false);
                }
            }
        }, 1);
    }

    @Command(
            names = "iKnowIGotWiped"
    )
    public void onKnow(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getWipedData() != null) {
            profile.getWipedData().setKnow(true);
        }
    }


    @Command(
            names = "killRecap"
    )
    public void onCheckKillRecap(Player player) {

        try {
            KillRecap killRecap = KillRecap.recapMap.get(player.getUniqueId());
            if (killRecap == null || killRecap.getKiller() == null || killRecap.getAssistData() == null) {
                player.sendMessage(CC.translate("&c&l错误! &7未找到有效的近期死亡回放数据,抱歉!"));
                return;
            }

            List<BaseComponent[]> pages = new LinkedList<>();
            BookUtil.PageBuilder pageBuilder = new BookUtil.PageBuilder();

            pageBuilder
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate("&c&l死亡回放"))
                                    .build()
                    )
                    .newLine()
                    .newLine()
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate("&0此系统仍在开发中,"))
                                    .build()
                    )
                    .newLine()
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate("&0展示数据不保证100%准确!"))
                                    .build()
                    )
                    .newLine()
                    .newLine()
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate("&0你: " + RankUtil.getPlayerRealColoredName(player.getUniqueId())))
                                    .build()
                    )
                    .newLine()
                    .newLine();

            List<AbstractPerk> collect = ThePit.getInstance()
                    .getPerkFactory()
                    .getPerks()
                    .stream()
                    .filter(abstractPerk -> killRecap.getPerk().contains(abstractPerk.getInternalPerkName()))
                    .collect(Collectors.toList());


            StringBuilder killerHover = new StringBuilder();
            PlayerProfile killerProfile = PlayerProfile.getPlayerProfileByUuid(killRecap.getKiller());
            killerHover.append(killerProfile.getFormattedName())
                    .append("\n");

            for (AbstractPerk perk : collect) {
                killerHover.append(CC.translate("&e"))
                        .append(perk.getDisplayName())
                        .append("\n");
            }

            pageBuilder.add(BookUtil.TextBuilder
                    .of(CC.translate("&0击杀者:"))
                    .build());

            pageBuilder.newLine()
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate(RankUtil.getPlayerColoredName(killRecap.getKiller())))
                                    .onHover(BookUtil.HoverAction.showText(new ChatComponentBuilder(
                                            killerHover.toString()
                                    ).create()))
                                    .build()
                    )
                    .newLine();


            StringBuilder killerCoin = new StringBuilder();
            killerCoin.append(CC.translate("&f基础奖励: &6+" + numFormat.format(killRecap.getBaseCoin())))
                    .append("\n");
            if (killRecap.getNotStreakCoin() > 0) {
                killerCoin.append(CC.translate("&f首杀奖励: &6+" + numFormat.format(killRecap.getNotStreakCoin())))
                        .append("\n");
            }
            if (killRecap.getLevelDisparityCoin() > 0) {
                killerCoin.append(CC.translate("&f等级/装备差距: &6+" + numFormat.format(killRecap.getLevelDisparityCoin())))
                        .append("\n");
            }
            if (killRecap.getOtherCoin() > 0) {
                killerCoin.append(CC.translate("&f其他奖励: &6+" + numFormat.format(killRecap.getOtherCoin())))
                        .append("\n");
            }

            StringBuilder killerExp = new StringBuilder();
            killerExp.append(CC.translate("&f基础奖励: &b+" + numFormat.format(killRecap.getBaseExp())))
                    .append("\n");
            if (killRecap.getNotStreakExp() > 0) {
                killerExp.append(CC.translate("&f首杀奖励: &b+" + numFormat.format(killRecap.getNotStreakExp())))
                        .append("\n");
            }
            if (killRecap.getLevelDisparityExp() > 0) {
                killerExp.append(CC.translate("&f等级/装备差距: &b+" + numFormat.format(killRecap.getLevelDisparityExp())))
                        .append("\n");
            }
            if (killRecap.getOtherExp() > 0) {
                killerExp.append(CC.translate("&f其他奖励: &b+" + numFormat.format(killRecap.getOtherExp())))
                        .append("\n");
            }
            pageBuilder.add(
                            BookUtil.TextBuilder
                                    .of(CC.translate("&6+" + numFormat.format(killRecap.getTotalCoin()) + "硬币"))
                                    .onHover(BookUtil.HoverAction.showText(new ChatComponentBuilder(killerCoin.toString()).create()))
                                    .build()
                    )
                    .add(
                            BookUtil.TextBuilder
                                    .of(CC.translate(" &b+" + numFormat.format(killRecap.getTotalExp()) + "经验值"))
                                    .onHover(BookUtil.HoverAction.showText(new ChatComponentBuilder(killerExp.toString()).create()))
                                    .build()
                    )
                    .build();

            pages.add(pageBuilder.build());

            int i = 1;
            pageBuilder = new BookUtil.PageBuilder().add(
                    BookUtil.TextBuilder
                            .of(CC.translate("&0助攻 (" + killRecap.getAssistData().size() + "):"))
                            .build()).newLine();
            for (KillRecap.AssistData assistData : killRecap.getAssistData()) {
                StringBuilder assistCoin = new StringBuilder();
                assistCoin.append(CC.translate("&f基础奖励: &6+" + numFormat.format(killRecap.getBaseCoin() * assistData.getPercentage())))
                        .append("\n");
                if (assistData.getStreakCoin() > 0) {
                    assistCoin.append(CC.translate("&f连杀奖励: &6+" + numFormat.format(assistData.getStreakCoin())))
                            .append("\n");
                }
                if (assistData.getLevelDisparityCoin() > 0) {
                    assistCoin.append(CC.translate("&f等级/装备差距: &6+" + numFormat.format(assistData.getLevelDisparityCoin())))
                            .append("\n");
                }
                StringBuilder assistExp = new StringBuilder();
                assistExp.append(CC.translate("&f基础奖励: &b+" + numFormat.format(killRecap.getBaseExp() * assistData.getPercentage())))
                        .append("\n");
                if (assistData.getStreakExp() > 0) {
                    assistExp.append(CC.translate("&f连杀奖励: &b+" + numFormat.format(assistData.getStreakExp())))
                            .append("\n");
                }
                if (assistData.getLevelDisparityExp() > 0) {
                    assistExp.append(CC.translate("&f等级/装备差距: &b+" + numFormat.format(assistData.getLevelDisparityExp())))
                            .append("\n");
                }
                pageBuilder.add(
                                BookUtil.TextBuilder
                                        .of(CC.translate("&0" + numFormat.format(assistData.getPercentage() * 100) + "% " + assistData.getDisplayName()))
                                        .build()
                        )
                        .newLine();
                pageBuilder.add(
                        BookUtil.TextBuilder
                                .of(CC.translate("&6+" + numFormat.format(assistData.getTotalCoin()) + "硬币"))
                                .onHover(BookUtil.HoverAction.showText(assistCoin.toString()))
                                .build()
                );
                pageBuilder.add(
                                BookUtil.TextBuilder
                                        .of(CC.translate(" &b+" + numFormat.format(assistData.getTotalExp()) + "经验值"))
                                        .onHover(BookUtil.HoverAction.showText(assistExp.toString()))
                                        .build()
                        )
                        .newLine()
                        .newLine();
                i++;
                if (i >= 5) {
                    pages.add(pageBuilder.build());
                    pageBuilder = new BookUtil.PageBuilder();
                    i = 0;
                }
            }
            if (i > 0) {
                pages.add(pageBuilder.build());
            }
            i = 1;
            pageBuilder = new BookUtil.PageBuilder().add(
                    BookUtil.TextBuilder
                            .of(CC.translate("&0&l伤害日志 (" + killRecap.getDamageLogs().size() + "):"))
                            .build()).newLine();
            for (KillRecap.DamageData damageData : killRecap.getDamageLogs()) {
                BookUtil.TextBuilder builder = BookUtil.TextBuilder
                        .of(CC.translate("&7" + ((killRecap.getCompleteTime() - damageData.getTimer().getStart()) / 1000) + "秒前  &c" + numFormat.format(damageData.getDamage()) + " " + (damageData.isMelee() ? "近战" : "远程")));
                if (damageData.getUsedItem() != null && damageData.getUsedItem().getType() != Material.AIR) {
                    builder.onHover(BookUtil.HoverAction.showItem(damageData.getUsedItem()));
                }
                pageBuilder.add(
                        builder.build()
                ).newLine();
                String damageHover = CC.translate((damageData.isAttack() ? "&c攻击" : "&c受到攻击")) +
                        "\n" +
                        CC.translate("&7攻击后" + (damageData.isAttack() ? "此玩家" : "自身") + "剩余血量: &c" + numFormat.format(damageData.getAfterHealth()) +
                                "\n" +
                                CC.translate("&7附魔/天赋 附加伤害: &c" + numFormat.format(damageData.getBoostDamage())));
                pageBuilder.add(
                        BookUtil.TextBuilder
                                .of(CC.translate((damageData.isAttack() ? "&0⚔ " : "&0☬ ") + damageData.getDisplayName()))
                                .onHover(BookUtil.HoverAction.showText(damageHover))
                                .build()
                ).newLine().newLine();
                i++;
                if (i >= 5) {
                    pages.add(pageBuilder.build());
                    pageBuilder = new BookUtil.PageBuilder();
                    i = 0;
                }
            }
            if (i > 0) {
                pages.add(pageBuilder.build());
            }
            BookUtil.openPlayer(player,
                    BookUtil.writtenBook()
                            .title(CC.translate("&c$killRecap #" + player.getName()))
                            .author("Amadeus Ai")
                            .pages(pages)
                            .build()
            );
        } catch (Exception e) {
            CC.printError(player, e);
        }
        //todo: book
    }


    @Command(
            names = "cdk"
    )
    public void cdk(Player player, @Parameter(name = "cdk") String cdk) {
        final CDKData data = CDKData.getCachedCDK().get(cdk);
        if (data == null) {
            player.sendMessage(CC.translate("&c错误的CDK,请检查大小写是否一致!"));
            return;
        }

        if (data.getLimitPermission() != null) {
            if (!player.hasPermission(data.getLimitPermission())) {
                player.sendMessage(CC.translate("&c你没有领取这个CDK的权限!"));
                return;
            }
        }
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        if (data.getLimitPrestige() <= 0 && profile.getPrestige() <= 0 && profile.getLevel() < data.getLimitLevel()) {
            player.sendMessage(CC.translate("&c你不满足领取条件!"));
            return;
        }
        if (data.getLimitPrestige() > 0 && profile.getPrestige() < data.getLimitPrestige()) {
            player.sendMessage(CC.translate("&c精通等级不满足要求哦,快去升级吧!"));
            return;
        }
        if (data.getLimitPrestige() > 0 && data.getLimitLevel() > 0 && profile.getPrestige() == data.getLimitPrestige() && profile.getLevel() < data.getLimitLevel()) {
            player.sendMessage("§c似乎还差一点等级就能领取到了...加油!");
            return;
        }

        final long now = System.currentTimeMillis();
        if (now > data.getExpireTime()) {
            player.sendMessage(CC.translate("&c该CDK已过期!"));
            return;
        }

        if (CDKData.isLoading()) {
            player.sendMessage(CC.translate("&c系统繁忙，请稍后再试!"));
            return;
        }

        if (data.getLimitClaimed() != -1 && data.getClaimedPlayers().size() >= data.getLimitClaimed()) {
            player.sendMessage(CC.translate("&c领取已达上限,下次快一点哦!"));
            return;
        }

        final boolean added = profile.getUsedCdk().add(cdk);
        if (added) {
            final Mail mail = new Mail();
            mail.setExpireTime(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);
            mail.setCoins(data.getCoins());
            mail.setExp(data.getExp());
            mail.setRenown(data.getRenown());
            mail.setItem(data.getItem());
            mail.setSendTime(System.currentTimeMillis());
            mail.setTitle("&e【奖励】兑换码兑换奖励");
            mail.setContent("&f亲爱的玩家: 请查收通过兑换码获得的奖励");

            data.getClaimedPlayers().add(player.getName());
            data.active();

            profile.getMailData().sendMail(mail);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 0.8F);
            player.sendMessage(CC.translate("&a领取成功! 请在邮件NPC处领取奖励!"));
        } else {
            player.sendMessage(CC.translate("&c你已经领取过这个CDK了!"));
        }
    }

    @Command(
            names = "viewOffer"
    )
    public void viewOffer(Player player, @Parameter(name = "玩家") Player target) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PlayerProfile targetProfile = PlayerProfile.getOrLoadPlayerProfileByUuid(target.getUniqueId());
        if (targetProfile.getOfferData().getBuyer() == null || !targetProfile.getOfferData().getBuyer().equals(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c你没有来自此玩家的交易报价!"));
            return;
        }

        if (targetProfile.getOfferData().hasUnclaimedOffer()) {
            player.sendMessage(CC.translate("&c此交易报价已过期!"));
            return;
        }

        if (!profile.getCombatTimer().hasExpired()) {
            player.sendMessage(CC.translate("&c你无法在战斗中使用此功能!"));
            return;
        }

        if (profile.getTradeLimit().getTimes() >= 25) {
            player.sendMessage(CC.translate("&c你今天的交易次数已经达到上限!"));
            player.sendMessage(CC.translate("&c使用 &e/tradeLimits &c查看你的今日交易上限情况."));
            return;
        }

        if (profile.getTradeLimit().getAmount() + targetProfile.getOfferData().getPrice() >= 50000) {
            player.sendMessage(CC.translate("&c对方的开价加上今日已交易量已超过交易上限,因此你无法接受此交易报价."));
            player.sendMessage(CC.translate("&c使用 &e/tradeLimits &c查看你的今日交易上限情况."));
            return;
        }

        if (!player.getName().equalsIgnoreCase(player.getDisplayName())) {
            player.sendMessage(CC.translate("&c你无法在匿名模式下使用交易功能!"));
            return;
        }

        if (profile.getLevel() < 60) {
            player.sendMessage(CC.translate("&c&l等级不足! &7此指令在 " + LevelUtil.getLevelTag(profile.getPrestige(), 60) + " &7时解锁."));
            return;
        }

        new OfferMenu(target).openMenu(player);
    }

    @Command(
            names = "offer"
    )
    public void offerItem(Player player, @Parameter(name = "玩家", defaultValue = "#null") String targetPlayer, @Parameter(name = "出价", defaultValue = "#null") String price) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getOfferData().hasActiveOffer()) {
            player.sendMessage(CC.translate("&c你当前有一个正在进行中的交易报价!"));
            return;
        } else if (profile.getOfferData().hasUnclaimedOffer()) {
            if (InventoryUtil.isInvFull(player)) {
                player.sendMessage(CC.translate("&c你有一个未结算的交易报价,请将背包腾出空间后重试!"));
            } else {
                InventoryUtil.addInvReverse(player.getInventory(), profile.getOfferData().getItemStack());
                profile.setOfferData(new OfferData());
                player.sendMessage(CC.translate("&c你有一个未结算的交易报价,相关物品已退还到你的背包.要发起一个新的交易报价,请再次输入此指令."));
            }
            return;
        }
        if (targetPlayer.equals("#null") || price.equals("#null")) {
            player.sendMessage(CC.translate("&c&l错误的使用方法! &7请手持要出售的物品,输入 &e/offer 玩家名 你的出价 &7来向此玩家发送一个报价请求,对方同意后将获得你提供的物品,你获得出价的硬币."));
            return;
        }
        try {
            if (Integer.parseInt(price) <= 0) {
                throw new Error("illegal price");
            }
        } catch (Exception e) {
            player.sendMessage(CC.translate("&c你输入的价格有误!"));
            return;
        }
        if (Bukkit.getPlayer(targetPlayer) == null || !Bukkit.getPlayer(targetPlayer).isOnline() || PlayerUtil.isVanished(Bukkit.getPlayer(targetPlayer))) {
            player.sendMessage(CC.translate("&c你选择的玩家不在线!"));
            return;
        }
        Player target = Bukkit.getPlayer(targetPlayer);
        PlayerProfile targetProfile = PlayerProfile.getOrLoadPlayerProfileByUuid(target.getUniqueId());

        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.translate("&c你无法选择此玩家进行交易!"));
            return;
        }
        if (!profile.getCombatTimer().hasExpired()) {
            player.sendMessage(CC.translate("&c你无法在战斗中使用此功能!"));
            return;
        }

        // 当前时间
        long now = System.currentTimeMillis();
        long date = profile.getTradeLimit().getLastRefresh();
        //获取今天的日期
        String nowDay = dateFormat.format(now);
        //对比的时间
        String day = dateFormat.format(date);

        //daily reset
        if (!day.equals(nowDay) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 4) {
            profile.getTradeLimit().setLastRefresh(now);
            profile.getTradeLimit().setAmount(0);
            profile.getTradeLimit().setTimes(0);
        }

        if (profile.getTradeLimit().getTimes() >= 25) {
            player.sendMessage(CC.translate("&c你今天的交易次数已经达到上限! (25/25)"));
            player.sendMessage(CC.translate("&c使用 &e/tradeLimits &c查看你的今日交易上限情况."));
            return;
        }

        if (profile.getTradeLimit().getAmount() + Integer.parseInt(price) >= 50000) {
            player.sendMessage(CC.translate("&c你的开价加上今日已交易量已超过交易上限,因此你无法发起此交易报价."));
            player.sendMessage(CC.translate("&c使用 &e/tradeLimits &c查看你的今日交易上限情况."));
            return;
        }

        if (!player.getName().equalsIgnoreCase(player.getDisplayName())) {
            player.sendMessage(CC.translate("&c你无法在匿名模式下使用交易功能!"));
            return;
        }

        if (profile.getLevel() < 60) {
            player.sendMessage(CC.translate("&c&l等级不足! &7此指令在 " + LevelUtil.getLevelTag(profile.getPrestige(), 60) + " &7时解锁."));
            return;
        }

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(CC.translate("&c请手持你要出售的物品再设置出售对象与价格!"));
            return;
        }

        if (!ItemUtil.canTrade(player.getItemInHand())) {
            player.sendMessage(CC.translate("&c此物品无法用于交易!"));
            return;
        }

        //todo: create offer
        profile.getOfferData().createOffer(target.getUniqueId(), player.getItemInHand(), Integer.parseInt(price));
        player.setItemInHand(new ItemStack(Material.AIR));
        if (!targetProfile.getPlayerOption().isTradeNotify() && !player.hasPermission(PlayerUtil.getStaffPermission())) {
            player.sendMessage(CC.translate("&c对方在游戏选项之后中设置了不接受交易请求,因此无法查看你的请求提示."));
            player.sendMessage(CC.translate("&c但对方仍可以通过使用 &e/viewOffer " + player.getName() + " &c以同意你的请求."));
        } else {
            player.sendMessage(CC.translate("&e&l交易报价发送! &7成功向 " + LevelUtil.getLevelTag(targetProfile.getPrestige(), targetProfile.getLevel()) + " " + RankUtil.getPlayerColoredName(target.getUniqueId()) + " &7发送了交易报价!"));
            target.spigot()
                    .sendMessage(new ChatComponentBuilder(CC.translate("&e&l交易报价! " + LevelUtil.getLevelTag(profile.getPrestige(), profile.getLevel()) + " " + RankUtil.getPlayerColoredName(player.getUniqueId()) + " &7向你发送了交易报价,请"))
                            .append(new ChatComponentBuilder(CC.translate(" &e点击这里")).setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentBuilder(CC.translate("&6点击以接受")).create())).setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewOffer " + player.getName())).create())
                            .append(CC.translate(" &r&7以查看此交易报价."))
                            .create());
        }
    }

    @Command(
            names = "AuctionGui"
    )
    public void openAuctionGui(Player player) {
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() == null || !"auction".equals(eventFactory.getActiveNormalEventName())) {
            player.sendMessage(CC.translate("&c此指令当前无法使用!"));
            return;
        }
        ThePit.getApi().openAuctionMenu(player);
    }

    @Command(
            names = "cool"
    )
    public void onCoolCommand(Player player) {
        if (!PlayerUtil.isPlayerUnlockedPerk(player, "cool_perk")) {
            player.sendMessage(CC.translate("&c你当前无法使用该指令!"));
            return;
        }
        List<String> list = new ArrayList<>();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (PlayerUtil.isVanished(target)) {
                continue;
            }
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(target.getUniqueId());
            if (profile.isNicked() && profile.getNickPrestige() > 0) {
                list.add(CC.translate(profile.getFormattedNameWithRoman()));
            } else if (!profile.isNicked() && profile.getPrestige() > 0) {
                list.add(CC.translate(profile.getFormattedNameWithRoman()));
            }
        }
        if (list.size() > 0) {
            player.sendMessage(CC.translate("&b当前在线精通玩家数: &6" + list.size()));
            list.forEach(player::sendMessage);
        } else {
            player.sendMessage(CC.translate("&c当前没有精通玩家在线!"));
        }
    }

    @Command(
            names = "openKingsQuestUI",
            permissionNode = "pit.kingquest.ui"
    )
    public void handleOpenKingQuestUI(Player player) {
        KingQuestsUI.INSTANCE.openMenu(player);
    }

    @Command(
            names = "openBakeMaster",
            permissionNode = "pit.kingquest.ui"
    )
    public void handleOpenBake(Player player) {
        CakeBakeUI.INSTANCE.openMenu(player);
    }

}

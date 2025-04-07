package cn.charlotte.pit.events.impl.major;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PlacedBlockData;
import cn.charlotte.pit.event.PitDamageEvent;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.events.IEpicEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.medal.impl.challenge.RagePitTopMedal;
import cn.charlotte.pit.runnable.ClearRunnable;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 0:33
 */

public class RagePitEvent implements IEvent, IEpicEvent, Listener {
    @Getter
    private static final Map<UUID, DamageData> damageMap = new HashMap<>();
    private static EditSession session;
    @Getter
    private static int killed = 0;
    private static BukkitRunnable runnable;
    @Getter
    private static Cooldown timer;

    public static int getDamageRank(Player player) {
        DamageData damage = damageMap.get(player.getUniqueId());
        HashSet<Map.Entry<UUID, DamageData>> entry = new HashSet<>(damageMap.entrySet());

        Map<Double, Integer> rank = new HashMap<>();
        int i = 1;

        for (DamageData en : entry.stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparingDouble(DamageData::getDamage).reversed())
                .collect(Collectors.toList())) {
            rank.put(en.getDamage(), i);
            i++;
        }

        return rank.get(damage.getDamage());
    }

    @Override
    public String getEventInternalName() {
        return "rage_pit";
    }

    @Override
    public String getEventName() {
        return "疯狂天坑";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @Override
    public void onActive() {
        try {
            Bukkit.getPluginManager()
                    .registerEvents(this, ThePit.getInstance());
            //build wall - start
            Location middle = ThePit.getInstance().getPitConfig().getRagePitMiddle();
            BlockVector vector = new BlockVector(middle.getBlockX(), middle.getBlockY(), middle.getBlockZ());
            BukkitWorld world = new BukkitWorld(Bukkit.getWorlds().get(0));
            session = FaweAPI.getEditSessionBuilder(world).build();


            TaskManager.IMP.async(() -> {
                session.makeCylinder(vector, new SingleBlockPattern(new BaseBlock(BlockID.GLASS)), ThePit.getInstance().getPitConfig().getRagePitRadius(), ThePit.getInstance().getPitConfig().getRagePitHeight(), false);
                session.flushQueue();
            });


            for (PlacedBlockData blockData : ClearRunnable.getClearRunnable().getPlacedBlock()) {
                blockData.getLocation().getBlock().setType(Material.AIR);
            }
            ClearRunnable.getClearRunnable().getPlacedBlock().clear();

            //build wall - end

            //set Player's max health and teleport to spawn
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setMaxHealth(player.getMaxHealth() * 2);
                player.setHealth(player.getMaxHealth());

                Location location = ThePit.getInstance().getPitConfig()
                        .getSpawnLocations()
                        .get(RandomUtil.random.nextInt(ThePit.getInstance().getPitConfig().getSpawnLocations().size()));
                player.teleport(location);
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.5F);
                player.sendMessage(CC.translate("&5&l大型事件! &6&l疯狂天坑 &7事件开始!"));
            }

            timer = new Cooldown(5, TimeUnit.MINUTES);

            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (timer.hasExpired()) {
                        ThePit.getInstance()
                                .getEventFactory()
                                .inactiveEvent(RagePitEvent.this);
                        return;
                    }

                    ThePit.getInstance()
                            .getBossBar()
                            .getBossBar()
                            .setTitle(CC.translate("&5&l大型事件! &6&l" + getEventName() + " &7将在 &a" + TimeUtil.millisToTimer(timer.getRemaining()) + "&7 后结束!"));
                    ThePit.getInstance()
                            .getBossBar()
                            .getBossBar()
                            .setProgress(timer.getRemaining() / (1000 * 60 * 5D));
                }
            };

            runnable.runTaskTimer(ThePit.getInstance(), 20, 20);
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onPlayerKill(PitKillEvent event) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            killed++;
        }
    }

    @EventHandler
    public void onPlayerDamage(PitDamageEvent event) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            damageMap.putIfAbsent(event.getAttacker().getUniqueId(), new DamageData(event.getAttacker().getUniqueId(), 0));
            DamageData damageData = damageMap.get(event.getAttacker().getUniqueId());
            damageData.setDamage(damageData.getDamage() + event.getFinalDamage());
        }
    }

    @Override
    public void onInactive() {
        HandlerList.unregisterAll(this);

        //还原玩家血量,移除活动道具
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMaxHealth(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getExtraMaxHealthValue() + 20.0);
            InventoryUtil.removeItemWithInternalName(player, "angry_potato");
        }
        //取消Runnable
        runnable.cancel();
        //还原建筑
        FaweAPI.getTaskManager().async(() -> {
            session.undo(session);
            session.flushQueue();
        });
        /* TaskManager.IMP.async(() -> {
            session.undo(session);
            session.flushQueue();
        }); */
        //还原BossBar
        ThePit.getInstance()
                .getBossBar()
                .getBossBar()
                .setTitle("");
        //获取伤害前3名玩家
        HashSet<Map.Entry<UUID, DamageData>> entry = new HashSet<>(damageMap.entrySet());
        List<DamageData> collect = entry
                .stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparingDouble(DamageData::getDamage).reversed())
                .collect(Collectors.toList());
        //结算消息
        int rank = 1;
        for (DamageData data : collect) {
            Player player = Bukkit.getPlayer(data.getUuid());
            if (player == null || !player.isOnline()) {
                continue;
            }

            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            player.sendMessage(CC.GOLD + CC.CHAT_BAR);
            player.sendMessage(CC.translate("&6&l天坑事件结束: &c" + this.getEventName()));
            double rewardCoins = 0;
            int rewardRenown = 0;
            if (rank <= 3) {
                rewardCoins += 2000;
                rewardRenown += 2;
            } else if (rank <= 20) {
                rewardCoins += 500;
                rewardRenown += 1;
            } else {
                rewardCoins += 100;
            }
            if (killed >= 600) {
                rewardCoins = 2 * rewardCoins;
            }
            if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTier() >= 5 && rewardRenown > 0) {
                rewardRenown++;
            }
            int enchantBoostLevel = Utils.getEnchantLevel(player.getInventory().getLeggings(), "Paparazzi");
            if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) {
                enchantBoostLevel = 0;
            }
            if (enchantBoostLevel > 0) {
                rewardCoins += 0.5 * enchantBoostLevel * rewardCoins;
                rewardRenown += Math.floor(0.5 * enchantBoostLevel * rewardRenown);
                MythicLeggingsItem mythicLeggings = new MythicLeggingsItem();
                mythicLeggings.loadFromItemStack(player.getInventory().getLeggings());
                if (mythicLeggings.isEnchanted()) {
                    if (mythicLeggings.getMaxLive() > 0 && mythicLeggings.getLive() <= 2) {
                        player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    } else {
                        mythicLeggings.setLive(mythicLeggings.getLive() - 2);
                        player.getInventory().setLeggings(mythicLeggings.toItemStack());
                    }
                }
            }
            if (PlayerUtil.isPlayerUnlockedPerk(player, "self_confidence")) {
                if (rank <= 5) {
                    rewardCoins += 5000;
                } else if (rank <= 10) {
                    rewardCoins += 2500;
                } else if (rank <= 15) {
                    rewardCoins += 1000;
                }
            }
            profile.grindCoins(rewardCoins);
            profile.setCoins(profile.getCoins() + rewardCoins);
            profile.setRenown(profile.getRenown() + rewardRenown);
            profile.kingsQuestsData.checkUpdate();
            if (profile.kingsQuestsData.getAccepted()) {
                if (!profile.kingsQuestsData.getCompleted()) {
                    profile.kingsQuestsData.setCollectedRenown(profile.kingsQuestsData.getCollectedRenown() + rewardRenown);
                }
            }

            player.sendMessage(CC.translate("&6你的奖励: &6+" + rewardCoins + "硬币 &e+" + rewardRenown + "声望"));
            if (killed >= 600) {
                player.sendMessage(CC.translate("&6&l全局奖励: &a&l成功! &7所有人获得的金币翻倍!"));
            } else {
                player.sendMessage(CC.translate("&6&l全局奖励: &c&l失败! &7总共击败了" + killed + "名玩家"));
            }
            int damage = (int) (data.getDamage() / 2);
            player.sendMessage(CC.translate("&6&l你: &c造成" + damage + "❤ &7(排名#" + getDamageRank(player) + ")"));
            if (collect.size() >= 3) {
                player.sendMessage(CC.translate("&6顶级玩家: "));
                for (int i = 0; i < 3; i++) {
                    Player top = Bukkit.getPlayer(collect.get(i).getUuid());
                    if (top != null && top.isOnline()) {
                        PlayerProfile topProfile = PlayerProfile.getPlayerProfileByUuid(top.getUniqueId());

                        int d = (int) (collect.get(i).getDamage() / 2);
                        player.sendMessage(CC.translate(" &e&l#" + (i + 1) + " " + topProfile.getFormattedName() + " &e造成了 &c" + d + "❤ 伤害"));
                    }
                }
            }

            player.sendMessage(CC.GOLD + CC.CHAT_BAR);

            if (damageMap.size() >= 50 && getDamageRank(player) > 0 && getDamageRank(player) <= 3) {
                new RagePitTopMedal().addProgress(profile, 1);
            }


            rank++;
        }

        damageMap.clear();
        killed = 0;
    }

    @Data
    @AllArgsConstructor
    public static class DamageData {
        private UUID uuid;
        private double damage;
    }
}

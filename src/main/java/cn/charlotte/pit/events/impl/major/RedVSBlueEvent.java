package cn.charlotte.pit.events.impl.major;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitAssistEvent;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.event.PitProfileLoadedEvent;
import cn.charlotte.pit.events.IEpicEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.IScoreBoardInsert;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/4 12:31
 */
@Getter
public class RedVSBlueEvent implements IEvent, IEpicEvent, Listener, IScoreBoardInsert {
    private final List<UUID> redTeam = new ArrayList<>();
    private final List<UUID> blueTeam = new ArrayList<>();
    private final Map<UUID, Integer> kaMap = new HashMap<>();
    private Cooldown timer = new Cooldown(5, TimeUnit.MINUTES);
    private int redKills;
    private int blueKills;

    @Override
    public String getEventInternalName() {
        return "red_vs_blue";
    }

    @Override
    public String getEventName() {
        return "&c红&9蓝&e大战";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    public boolean isRedTeam(Player player) {
        return isRedTeam(player.getUniqueId());
    }

    public boolean isRedTeam(UUID player) {
        return redTeam.contains(player);
    }

    @EventHandler
    public void onProfileLoad(PitProfileLoadedEvent event) {
        if (redTeam.size() > blueTeam.size()) {
            blueTeam.add(event.getPlayerProfile().getPlayerUuid());
        } else {
            redTeam.add(event.getPlayerProfile().getPlayerUuid());
        }
        this.sendPacket(Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid()));
        for (Player target : Bukkit.getOnlinePlayers()) {
            this.sendPacket(target);
        }
    }

    @Override
    public void onActive() {
        Bukkit.getPluginManager()
                .registerEvents(this, ThePit.getInstance());

        this.timer = new Cooldown(5, TimeUnit.MINUTES);

        ArrayList<? extends Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer.hasExpired()) {
                    ThePit.getInstance()
                            .getEventFactory()
                            .inactiveEvent(RedVSBlueEvent.this);
                    cancel();
                }
            }
        }.runTaskTimer(ThePit.getInstance(), 20, 20);

        for (Player player : players) {
            scatterTeam(player);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(player);
        }
    }

    private void scatterTeam(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getPrestige() < 1) {
            if (redTeam.size() >= blueTeam.size()) {
                blueTeam.add(player.getUniqueId());
            } else {
                redTeam.add(player.getUniqueId());
            }
            return;
        }

        int redTotalPoint = 0;
        int blueTotalPoint = 0;
        for (UUID uuid : redTeam) {
            PlayerProfile playerProfileByUuid = PlayerProfile.getPlayerProfileByUuid(uuid);
            redTotalPoint += playerProfileByUuid.getPrestige() * 120 + LevelUtil.getLevelByExp(playerProfileByUuid.getPrestige(), playerProfileByUuid.getExperience());
        }

        for (UUID uuid : blueTeam) {
            PlayerProfile playerProfileByUuid = PlayerProfile.getPlayerProfileByUuid(uuid);
            blueTotalPoint += playerProfileByUuid.getPrestige() * 120 + LevelUtil.getLevelByExp(playerProfileByUuid.getPrestige(), playerProfileByUuid.getExperience());
        }

        if (redTotalPoint >= blueTotalPoint) {
            blueTeam.add(player.getUniqueId());
        } else {
            redTeam.add(player.getUniqueId());
        }
    }

    @Override
    public void onInactive() {
        try {
            HandlerList.unregisterAll(this);
            resetArmor();
            boolean redWin = redKills >= blueKills;
            if (redKills == blueKills) {
                redKills++;
            }

            CC.boardCast(CC.CHAT_BAR);
            CC.boardCast("&6&l天坑事件结束: " + this.getEventName() + "&6&l!");

            List<Map.Entry<UUID, Integer>> list = kaMap.entrySet().stream()
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .collect(Collectors.toList());

            Collections.reverse(list);
            Map<UUID, Integer> map = new HashMap<>();
            int i = 1;
            for (Map.Entry<UUID, Integer> entry : list) {
                map.put(entry.getKey(), i);
                i++;
            }


            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                boolean redTeam = isRedTeam(player);
                int rewardCoins = 0;
                int rewardRenown = 0;
                boolean win = false;
                if (redWin) {
                    if (redTeam) {
                        rewardCoins += 1500;
                        win = true;
                    }
                } else if (!redTeam) {
                    rewardCoins += 1500;
                    win = true;
                }
                Integer integer = map.get(player.getUniqueId());
                if (integer != null) {
                    if (integer <= 3) {
                        rewardCoins += 2500;
                        rewardRenown += 2;
                    } else if (integer <= 20) {
                        rewardCoins += 1500;
                        rewardRenown += 1;
                    } else {
                        rewardCoins += 200;
                    }
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
                    if (integer != null) {
                        if (integer <= 5) {
                            rewardCoins += 5000;
                        } else if (integer <= 10) {
                            rewardCoins += 2500;
                        } else if (integer <= 15) {
                            rewardCoins += 1000;
                        }
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
                player.sendMessage(CC.translate("&6&l你的奖励: &6+" + rewardCoins + "硬币" + (rewardRenown == 0 ? "" : "&e +" + rewardRenown + "声望")));
                player.sendMessage(CC.translate("&6&l胜利队伍: " + (redWin ? "&c红队 &e以 " + redKills + " 击杀击败了 &9蓝队 &e(+" + (redKills - blueKills) + ")" : "&9蓝队 &e以 " + blueKills + " 击杀和助攻击败了 &c红队 &e(+" + (blueKills - redKills) + ")")));
                player.sendMessage(CC.translate("&6&l团队奖励: " + (win ? "&a&l成功！ &7获得了大量金币奖励" : "&c&l失败")));
                Integer ka = kaMap.get(player.getUniqueId());
                if (ka != null) {
                    player.sendMessage(CC.translate("&6&l你的战绩: " + (redTeam ? "&c" : "&9") + ka + " 击杀和助攻 &7(排名#" + integer + ")"));
                }
                player.sendMessage(CC.translate("&6&l顶级玩家: "));

                for (int j = 0; j < 3; j++) {
                    if (list.size() <= j) break;
                    Map.Entry<UUID, Integer> entry = list.get(j);
                    Player target = Bukkit.getPlayer(entry.getKey());
                    if (target != null && target.isOnline()) {
                        PlayerProfile targetP = PlayerProfile.getPlayerProfileByUuid(target.getUniqueId());
                        String displayName = targetP.getFormattedName();

                        player.sendMessage(CC.translate(" &e&l#" + (j + 1) + " " + displayName + " &e获得了 " + (isRedTeam(target) ? "&c" : "&9") + kaMap.get(target.getUniqueId()) + " 击杀和助攻"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getOnlinePlayers().forEach(error -> {
                CC.printError(error, e);
            });
        }
        CC.boardCast(CC.CHAT_BAR);


        this.redKills = 0;
        this.blueKills = 0;
        this.blueTeam.clear();
        this.redTeam.clear();
    }


    private void sendPacket(Player player) {
        PacketPlayOutEntityEquipment packet = null;
        if (redTeam.contains(player.getUniqueId())) {
            packet = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.WOOL).durability(14).build()));
        } else if (blueTeam.contains(player.getUniqueId())) {
            packet = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemBuilder(Material.WOOL).durability(11).build()));
        }
        if (packet == null) {
            return;
        }
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(player)) continue;
            ((CraftPlayer) target).getHandle()
                    .playerConnection
                    .sendPacket(packet);
        }
    }

    private void resetArmor() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (player.equals(target)) continue;
                connection.sendPacket(new PacketPlayOutEntityEquipment(target.getEntityId(), 4, CraftItemStack.asNMSCopy(target.getInventory().getHelmet())));
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        this.sendPacket(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            damager = (Player) ((Projectile) event.getDamager()).getShooter();
        }
        if (damager == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (isRedTeam(damager)) {
            if (isRedTeam((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        } else {
            if (!isRedTeam((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onKilled(PitKillEvent event) {
        if (redTeam.contains(event.getKiller().getUniqueId())) {
            redKills++;
        } else if (blueTeam.contains(event.getKiller().getUniqueId())) {
            blueKills++;
        }

        kaMap.putIfAbsent(event.getKiller().getUniqueId(), 0);
        kaMap.put(event.getKiller().getUniqueId(), kaMap.get(event.getKiller().getUniqueId()) + 1);
    }

    @EventHandler
    public void onAssist(PitAssistEvent event) {
        kaMap.putIfAbsent(event.getAssist().getUniqueId(), 0);
        kaMap.put(event.getAssist().getUniqueId(), kaMap.get(event.getAssist().getUniqueId()) + 1);
    }

    @Override
    public List<String> insert(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&f剩余时间: &a" + TimeUtil.millisToTimer(timer.getRemaining()));
        Integer ka = kaMap.get(player.getUniqueId());
        if (ka != null) {
            lines.add("&f你的击杀&助攻: &a" + ka);
        }
        if (blueTeam.contains(player.getUniqueId())) {
            lines.add("&f击杀: &9" + blueKills + "&f vs &c" + redKills);
            lines.add("&f队伍: &9⬛");
        } else {
            lines.add("&f击杀: &c" + redKills + "&f vs &9" + blueKills);
            lines.add("&f队伍: &c⬛");
        }

        return lines;
    }
}

package cn.charlotte.pit.events.impl;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.config.PitConfig;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.INormalEvent;
import cn.charlotte.pit.medal.impl.challenge.CakeEventMedal;
import cn.charlotte.pit.util.LocationUtil;
import cn.charlotte.pit.util.aabb.AxisAlignedBB;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/5 23:37
 */
public class CakeEvent implements IEvent, INormalEvent, Listener {
    private static CuboidRegion[] regions;
    private final DecimalFormat numFormatTwo = new DecimalFormat("0.00");
    private final DecimalFormat df = new DecimalFormat(",###,###,###,###");
    private BukkitRunnable runnable;
    private EditSession session;
    private AxisAlignedBB alignedBB;
    private Map<UUID, CakePlayerData> dataCache;

    public CakeEvent() {
        if (regions == null) {
            PitConfig config = ThePit.getInstance().getPitConfig();
            regions = new CuboidRegion[]{
                    new CuboidRegion(BukkitUtil.toVector(config.getCakeZoneAPosA()), BukkitUtil.toVector(config.getCakeZoneAPosB())),
                    new CuboidRegion(BukkitUtil.toVector(config.getCakeZoneBPosA()), BukkitUtil.toVector(config.getCakeZoneBPosB())),
                    new CuboidRegion(BukkitUtil.toVector(config.getCakeZoneCPosA()), BukkitUtil.toVector(config.getCakeZoneCPosB())),
                    new CuboidRegion(BukkitUtil.toVector(config.getCakeZoneDPosA()), BukkitUtil.toVector(config.getCakeZoneDPosB()))
            };
        }
    }

    @Override
    public String getEventInternalName() {
        return "cake";
    }

    @Override
    public String getEventName() {
        return "&d蛋糕争夺战";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @Override
    public void onActive() {
        CC.boardCast(MessageType.EVENT, "&d蛋糕! &7巨型蛋糕已生成在地图上，吃掉蛋糕获得大量金币和经验!");

        this.dataCache = new HashMap<>();

        final CuboidRegion region = regions[RandomUtil.random.nextInt(regions.length)];
        final Vector pos1 = region.getPos1();
        final Vector pos2 = region.getPos2();
        this.alignedBB = new AxisAlignedBB(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());

        TaskManager.IMP.async(() -> {
            BukkitWorld world = new BukkitWorld(Bukkit.getWorlds().get(0));
            this.session = FaweAPI.getEditSessionBuilder(world).build();

            final RandomPattern pattern = new RandomPattern();
            pattern.add(new BaseBlock(BlockID.CAKE_BLOCK), 0.94);
            pattern.add(new BaseBlock(BlockID.STAINED_CLAY, 2), 0.03);
            pattern.add(new BaseBlock(BlockID.STAINED_CLAY, 15), 0.03);

            session.setBlocks(region, pattern);

            session.flushQueue();
        });

        Bukkit.getPluginManager()
                .registerEvents(this, ThePit.getInstance());

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                ThePit.getInstance().getEventFactory().inactiveEvent(CakeEvent.this);
            }
        };

        this.runnable.runTaskLater(ThePit.getInstance(), 20 * 60 * 5);
    }

    @Override
    public void onInactive() {
        TaskManager.IMP.async(() -> {
            this.session.undo(session);
            this.session.flushQueue();
        });

        HandlerList.unregisterAll(this);

        this.runnable.cancel();

        for (UUID uuid : dataCache.keySet()) {
            if (dataCache.get(uuid).coins >= 5000 && Bukkit.getPlayer(uuid) != null) {
                new CakeEventMedal().addProgress(PlayerProfile.getPlayerProfileByUuid(uuid), 1);
            }
        }
    }

    @EventHandler
    public void onEatCake(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();
        final Location location = block.getLocation();
        final AxisAlignedBB blockAABB = new AxisAlignedBB(location.getX() - 1, location.getY() - 1, location.getZ() - 1, location.getX() + 1, location.getY() + 1, location.getZ() + 1);
        final Player player = event.getPlayer();
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());


        if (alignedBB.intersectsWith(blockAABB)) {
            CakePlayerData cakePlayerData = dataCache.get(player.getUniqueId());
            if (cakePlayerData == null) {
                cakePlayerData = new CakePlayerData();
                cakePlayerData.uuid = player.getUniqueId();
                cakePlayerData.name = player.getDisplayName();
                cakePlayerData.cooldown = new Cooldown(100);

                dataCache.put(player.getUniqueId(), cakePlayerData);
            }

            if (!cakePlayerData.cooldown.hasExpired()) {
                return;
            }

            if (!canEat(block.getLocation())) {
                CC.send(MessageType.EVENT, player, "&d&l蛋糕! &c你不能吃里面的蛋糕! 请先吃掉周围的蛋糕!");
                return;
            }

            cakePlayerData.cooldown = new Cooldown(100);

            if (block.getType() == Material.CAKE_BLOCK) {
                int baseCoins = 1;

                final BlockState state = block.getState();
                if (block.getData() == 6) {
                     baseCoins += 5;
                    block.setType(Material.AIR);
                } else {
                    block.setData((byte) (block.getData() + 1));
                }
                block.getState().update();

                // final double totalCoins = baseCoins + baseCoins * i * 0.25D;
                final double totalCoins = baseCoins;

                cakePlayerData.clicked++;
                cakePlayerData.coins += totalCoins;

                profile.grindCoins(totalCoins);
                profile.setCoins(profile.getCoins() + totalCoins);

                player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                CC.send(MessageType.EVENT, player, "&d&l蛋糕! &6+" + totalCoins + "金币 &d" + cakePlayerData.clicked + "次吃蛋糕" + " &6(" + numFormatTwo.format(cakePlayerData.coins) + "金币在这次事件中)");

            } else if (block.getType() == Material.STAINED_CLAY) {
                final byte data = block.getState().getData().getData();
                if (data == 2) {
                    block.setType(Material.AIR);

                    double totalCoins = 20;
                    cakePlayerData.clicked++;
                    cakePlayerData.coins += totalCoins;

                    profile.grindCoins(totalCoins);
                    profile.setCoins(profile.getCoins() + totalCoins);

                    player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                    CC.send(MessageType.EVENT, player, "&d&l蛋糕! &7吃下了 &c樱桃 &6+20金币&7!");
                } else if (data == 15) {
                    block.setType(Material.AIR);

                    double totalXp = 20;
                    cakePlayerData.clicked++;
                    cakePlayerData.coins += totalXp;

                    profile.setExperience(profile.getExperience() + totalXp);
                    profile.applyExperienceToPlayer(player);

                    player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                    CC.send(MessageType.EVENT, player, "&d&l蛋糕! &7吃下了 &4巧克力 &b+20经验&7!");
                }
            }
        }

    }

    private boolean canEat(Location location) {
        for (Location face : LocationUtil.getFaces(location)) {
            if (face.getBlock().getType() != Material.CAKE_BLOCK && face.getBlock().getType() != Material.STAINED_CLAY) {
                return true;
            }
        }
        return false;
    }

    public static class CakePlayerData {
        private UUID uuid;
        private String name;
        private int coins;
        private int clicked;
        private Cooldown cooldown;
    }
}

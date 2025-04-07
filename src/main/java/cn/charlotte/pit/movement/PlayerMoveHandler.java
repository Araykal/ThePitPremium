package cn.charlotte.pit.movement;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.PitConfig;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.runnable.ProfileLoadRunnable;
import cn.charlotte.pit.util.BlockUtil;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.VectorUtil;
import cn.charlotte.pit.util.aabb.AxisAlignedBB;
import cn.charlotte.pit.util.chat.ActionBarUtil;
import cn.charlotte.pit.util.chat.CC;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import spg.lgdev.handler.MovementHandler;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/16 23:49
 */
public class PlayerMoveHandler implements MovementHandler, Listener {
    private static final Set<Player> cantMoveList = Collections.synchronizedSet(new HashSet<>());

    public PlayerMoveHandler() {
        Bukkit.getServer().getPluginManager().registerEvents(this, ThePit.getInstance());
    }

    public static Set<Player> getCantMoveList() {
        return PlayerMoveHandler.cantMoveList;
    }

    public static void checkMove(Location to, Location from, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        //when X/Z Loc change
        if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()) {
            if (!profile.isLoaded() && ProfileLoadRunnable.getInstance() != null && ProfileLoadRunnable.getInstance().getCooldownMap() != null && ProfileLoadRunnable.getInstance().getCooldownMap().containsKey(player.getUniqueId())) {
                ActionBarUtil.sendActionBar(player, "&c正在加载您的游戏数据,如长时间等待请尝试重新进入...");

                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 9999999, 1, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, -100, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -100, false));
                return;
            }
        }

        profile.setLastActionTimestamp(System.currentTimeMillis());
        if (to.getBlockX() != from.getBlockX() ||
                to.getBlockY() != from.getBlockY() ||
                to.getBlockZ() != from.getBlockZ()) {

            if (profile.isScreenShare()) {
                BookUtil.openPlayer(player,
                        BookUtil.writtenBook()
                                .title(CC.translate("&c$screenShareRequest"))
                                .author("Amadeus Ai")
                                .pages(
                                        new BookUtil.PageBuilder()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&4&l您因疑似作弊而被冻结!"))
                                                                .build()
                                                )
                                                .newLine()
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0请在 3 分钟 内添加以下QQ:"))
                                                                .build()
                                                )
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0QQ: " + profile.getScreenShareQQ()))
                                                                .build()
                                                )
                                                .newLine()
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0如关闭客户端/超时未添加"))
                                                                .build()
                                                )
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0等拒绝查端的行为,"))
                                                                .build()
                                                )
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0账号会被封禁 30 天!"))
                                                                .build()
                                                )
                                                .newLine()
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0如遇到问题,可在公屏向管理员求助."))
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            }
            if (profile.getWipedData() != null && !profile.getWipedData().isKnow()) {
                BookUtil.openPlayer(player,
                        BookUtil.writtenBook()
                                .title(CC.translate("&c$wipeNotification #" + player.getName()))
                                .author("Amadeus Ai")
                                .pages(
                                        new BookUtil.PageBuilder()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&0您因" + profile.getWipedData().getReason()))
                                                                .build()
                                                )
                                                .newLine()
                                                .add(
                                                        CC.translate("&0我们已清除您的存档")
                                                )
                                                .newLine()
                                                .add(
                                                        CC.translate("&0希望您在未来的游戏中")
                                                )
                                                .newLine()
                                                .add(
                                                        CC.translate("&0遵守我们的规则，谢谢")
                                                ).newLine()
                                                .add(
                                                        CC.translate("&0如有疑问，请在论坛中申诉")
                                                )
                                                .newLine()
                                                .newLine()
                                                .add(
                                                        BookUtil.TextBuilder
                                                                .of(CC.translate("&a我已知晓"))
                                                                .onHover(BookUtil.HoverAction.showText(CC.translate("&f点击不再提示")))
                                                                .onClick(BookUtil.ClickAction.runCommand("/iKnowIGotWiped"))
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            }

            PitConfig config = ThePit.getInstance().getPitConfig();
            final AxisAlignedBB aabb = new AxisAlignedBB(config.getPitLocA().getX(), config.getPitLocA().getY(), config.getPitLocA().getZ(), config.getPitLocB().getX(), config.getPitLocB().getY(), config.getPitLocB().getZ());

            final AxisAlignedBB playerAABB = new AxisAlignedBB(to.getX(), to.getY(), to.getZ(), to.getX() + 0.8, to.getY() + 2, to.getZ() + 0.8);


            final boolean intersects = aabb.intersectsWith(playerAABB);

            boolean isInArena = !intersects;

            profile.setInArena(isInArena);

            if (!profile.isEditingMode()) {
                if (BlockUtil.isBlockNearby(player.getLocation(),3) && player.getGameMode() == GameMode.ADVENTURE){
                    player.setGameMode(GameMode.SURVIVAL);
                    return;
                }
                if (profile.isInArena()) {
                    if (player.getGameMode() == GameMode.ADVENTURE) {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                } else {
                    if (player.getGameMode() == GameMode.SURVIVAL) {
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                }
            }
            if (PlayerUtil.isStaffSpectating(player)) {
                player.setAllowFlight(true);
            }
        }

        boolean backing = player.hasMetadata("backing");
        if (backing) {
            if (to.getBlockX() != from.getBlockX() ||
                    to.getBlockY() != from.getBlockY() ||
                    to.getBlockZ() != from.getBlockZ()) {
                player.removeMetadata("backing", ThePit.getInstance());
                player.sendMessage(CC.translate("&c回城被取消."));
            }
        }


        if (to.clone().add(0, -1, 0).getBlock().getType() == Material.SLIME_BLOCK) {
            BlockIterator blockIterator = new BlockIterator(player.getLocation());
            for (int i = 0; i < 30; i++) {
                blockIterator.next();
            }
            to.setPitch(-30);
            player.getLocation().setPitch(-30);
            VectorUtil.entityPush(player, blockIterator.next().getLocation(), 110);

            player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float) to.getX(), (float) to.getY(), (float) to.getZ(), 0, 0, 0, 0, 1, 1);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (cantMoveList.contains(event.getPlayer())) {
            final Location to = event.getTo();
            final Location from = event.getFrom();

            if (to.getBlockX() != from.getBlockX() ||
                to.getBlockY() != from.getBlockY() ||
                to.getBlockZ() != from.getBlockZ()) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void handleUpdateLocation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {
        checkMove(location, location1, player.getPlayer());
    }

    @Override
    public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {
        PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).setLastActionTimestamp(System.currentTimeMillis());
    }
}

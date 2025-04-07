package cn.charlotte.pit.enchantment.type.genesis;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.music.PositionSongPlayer;
import cn.charlotte.pit.util.music.Song;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import spg.lgdev.handler.MovementHandler;
import spg.lgdev.iSpigot;

import java.util.*;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/30 21:13
 */
@ArmorOnly
public class JerryEnchant extends AbstractEnchantment implements ITickTask, MovementHandler {

    private final Map<UUID, PositionSongPlayer> playerMap = new HashMap<>();
    private final Song demon;
    private final Song angle;

    @SneakyThrows
    public JerryEnchant() {
        this.angle = Song.loadSong("God.nbs");
        this.demon = Song.loadSong("Zeppeli.nbs");
        startCleanupTask();
        registerMovementHandler();
    }

    private void startCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PositionSongPlayer> entry : new HashSet<>(playerMap.entrySet())) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null || !player.isOnline() || !isWearingLeggingsWithEnchantment(player)) {
                        stopPlaying(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(ThePit.getInstance(), 20, 20);
    }

    private boolean isWearingLeggingsWithEnchantment(Player player) {
        return player.getInventory().getLeggings() != null && getItemEnchantLevel(player.getInventory().getLeggings()) != -1;
    }

    private void stopPlaying(UUID playerId) {
        PositionSongPlayer player = playerMap.remove(playerId);
        if (player != null) {
            player.setPlaying(false);
        }
    }

    private void registerMovementHandler() {
        try {
            iSpigot.INSTANCE.addMovementHandler(this);
        } catch (NoClassDefFoundError ignore) {
        }
    }

    @Override
    public String getEnchantName() {
        return "DJ #1";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "jerry_genesis";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.GENESIS;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7此附魔只能通过阵营活动 &b光&c暗&f派系 &7获得." +
                "/s&7向周围的玩家播放音乐." +
                "/s&7天使阵营播放音乐: &b众神眷恋的幻想乡!" +
                "/s&7恶魔阵营播放音乐: &c特佩斯的年幼末裔";
    }

    @Override
    public void handle(int enchantLevel, Player target) {
        PositionSongPlayer songPlayer = playerMap.get(target.getUniqueId());
        if (songPlayer == null) {
            initializeSongPlayer(target);
        } else {
            PlayerUtil.sendParticle(target.getLocation().add(0, 2.0, 0), EnumParticle.NOTE, 2);
        }
    }

    private void initializeSongPlayer(Player target) {
        Song song = getSongForTeam(target);
        if (song != null) {
            PositionSongPlayer player = new PositionSongPlayer(song);
            player.setTargetLocation(target.getLocation());
            player.setAutoDestroy(false);
            player.setLoop(true);
            player.setPlaying(true);
            player.setVolume((byte) 0.05);
            playerMap.put(target.getUniqueId(), player);
        }
    }

    private Song getSongForTeam(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getGenesisData().getTeam() == GenesisTeam.DEMON) {
            return demon;
        } else if (profile.getGenesisData().getTeam() == GenesisTeam.ANGEL) {
            return angle;
        }
        return null;
    }


    @Override
    public int loopTick(int enchantLevel) {
        return 10;
    }

    @Override
    public void handleUpdateLocation(Player player, Location from, Location to, PacketPlayInFlying packet) {
        PositionSongPlayer songPlayer = playerMap.get(player.getUniqueId());
        if (songPlayer != null) {
            songPlayer.setTargetLocation(player.getLocation());
        }
    }

    @Override
    public void handleUpdateRotation(Player player, Location from, Location to, PacketPlayInFlying packet) {
    }
}


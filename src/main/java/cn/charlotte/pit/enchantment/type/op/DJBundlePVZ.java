package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.music.NBSDecoder;
import cn.charlotte.pit.util.music.PositionSongPlayer;
import cn.charlotte.pit.util.music.Song;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import spg.lgdev.handler.MovementHandler;
import spg.lgdev.iSpigot;

import java.util.*;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/5 14:57
 */
@ArmorOnly
public class DJBundlePVZ extends AbstractEnchantment implements ITickTask, MovementHandler {
    private final Map<UUID, PositionSongPlayer> playerMap = new HashMap<>();
    private final Song song;
    private final Song song2;
    private final Song song3;
    private final Song song4;

    @SneakyThrows
    public DJBundlePVZ() {
        this.song = NBSDecoder.parse(ThePit.getInstance().getClass().getClassLoader().getResourceAsStream("rapid_as_wildfire.nbs")); //pvz.nbs
        this.song2 = NBSDecoder.parse(ThePit.getInstance().getClass().getClassLoader().getResourceAsStream("pvz2.nbs"));
        this.song3 = NBSDecoder.parse(ThePit.getInstance().getClass().getClassLoader().getResourceAsStream("pvz3.nbs"));
        this.song4 = NBSDecoder.parse(ThePit.getInstance().getClass().getClassLoader().getResourceAsStream("pvz4.nbs"));


        new BukkitRunnable() {
            @Override
            public void run() {
                Set<Map.Entry<UUID, PositionSongPlayer>> entries = new HashSet<>(playerMap.entrySet());
                for (Map.Entry<UUID, PositionSongPlayer> entry : entries) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null || !player.isOnline()) {
                        PositionSongPlayer remove = playerMap.remove(entry.getKey());
                        remove.setPlaying(false);
                        continue;
                    }
                    if (player.getInventory().getLeggings() == null || getItemEnchantLevel(player.getInventory().getLeggings()) == -1) {
                        PositionSongPlayer remove = playerMap.remove(entry.getKey());
                        remove.setPlaying(false);
                    }
                }
            }
        }.runTaskTimer(ThePit.getInstance(), 20, 20);

        try {
            iSpigot.INSTANCE.addMovementHandler(this);
        } catch (NoClassDefFoundError ignore) {
        }
    }

    @Override
    public String getEnchantName() {
        return "DJ #PVZ";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "dj_pvz_bundle";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.OP;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7此附魔只能通过阵营活动 &b光&c暗&f派系 &7获得."
                + "/s&7向周围的玩家播放4首 &7&oPlants Vs Zombies 植物大战僵尸 &7游戏原声之一."
                + "/s&7&o不是所有人都能得到它的";
    }

    @Override
    public void handle(int enchantLevel, Player target) {
        PositionSongPlayer songPlayer = playerMap.get(target.getUniqueId());
        if (songPlayer == null) {
            //Song song = (Song) RandomUtil.helpMeToChooseOne(this.song, this.song2, this.song3, this.song4);
            //temp code
            Song song = this.song;
            PositionSongPlayer player = new PositionSongPlayer(song);
            player.setTargetLocation(target.getLocation());
            player.setAutoDestroy(false);
            player.setLoop(true);
            player.setPlaying(true);
            player.setVolume((byte) 0.05);

            this.playerMap.put(target.getUniqueId(), player);
        } else {
            target.getWorld().playEffect(target.getLocation().clone().add(0, 3, 0), Effect.NOTE, 1);
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 10;
    }

    @Override
    public void handleUpdateLocation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {
        PositionSongPlayer songPlayer = this.playerMap.get(player.getPlayer().getUniqueId());
        if (songPlayer != null) {
            songPlayer.setTargetLocation(player.getPlayer().getLocation());
        }
    }

    @Override
    public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {

    }
}

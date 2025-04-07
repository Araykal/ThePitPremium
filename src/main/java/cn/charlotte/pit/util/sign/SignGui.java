package cn.charlotte.pit.util.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 21:33
 */
public class SignGui {
    protected Map<String, Vector> signLocations = new ConcurrentHashMap<>();
    protected Map<String, SignGUIListener> listeners = new ConcurrentHashMap<>();

    public SignGui(JavaPlugin plugin) {
        ProtocolLibrary.getProtocolManager()
                .addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        final Player player = event.getPlayer();
                        Vector v = signLocations.remove(player.getName());
                        com.comphenix.protocol.wrappers.BlockPosition bp = event.getPacket().getBlockPositionModifier().getValues().get(0);
                        final WrappedChatComponent[] chatarray = event.getPacket().getChatComponentArrays().getValues().get(0);
                        final String[] lines = {chatarray[0].getJson().replace("\"", ""), chatarray[1].getJson().replace("\"", ""), chatarray[2].getJson().replace("\"", ""), chatarray[3].getJson().replace("\"", "")};
                        final SignGUIListener response = listeners.remove(event.getPlayer().getName());

                        if (v == null) {
                            return;
                        }
                        if (bp.getX() != v.getBlockX()) {
                            return;
                        }
                        if (bp.getY() != v.getBlockY()) {
                            return;
                        }
                        if (bp.getZ() != v.getBlockZ()) {
                            return;
                        }

                        if (response != null) {
                            event.setCancelled(true);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> response.onSignDone(player, lines));
                        }
                    }
                });
    }

    public void open(Player player, String[] messages, SignGUIListener response) {
        Location loc = new Location(player.getWorld(), 0, 1, 0);
        player.sendBlockChange(loc, Material.SIGN_POST, (byte) 0);
        player.sendSignChange(loc, messages);

        try {
            EntityPlayer handle = ((CraftPlayer) player).getHandle();
            PlayerConnection connection = handle.playerConnection;

            PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(
                    new BlockPosition(
                            loc.getX(),
                            loc.getY(),
                            loc.getZ()
                    )
            );

            connection.sendPacket(packet);
            signLocations.put(player.getName(), loc.toVector());
            listeners.put(player.getName(), response);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public interface SignGUIListener {
        void onSignDone(Player player, String[] lines);
    }

}

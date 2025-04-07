package cn.charlotte.pit.util.chat;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 13:26
 */
public class TitleUtil {


    public static void sendTitle(Player player, String title, String sub, int fadeIn, int fadeOut, int duration) {
        if (title != null) {
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(CC.translate(title)), fadeIn, fadeOut, duration);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        if (sub != null) {
            PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(CC.translate(sub)), fadeIn, fadeOut, duration);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}

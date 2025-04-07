package cn.charlotte.pit.util.rank;
import cn.charlotte.pit.ThePit;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Yurinan, Misoryan
 * @since 2021/1/5 14:55
 */
public class RankUtil {

    public static String getPlayerRank(String name) {
        return LuckPermsUtil.getPrefix(Bukkit.getOfflinePlayer(name).getUniqueId()); //default player rank
    }

    public static boolean isPlayerNicked(UUID uuid) {
        return getPlayerRealColoredName(uuid).equalsIgnoreCase(getPlayerColoredName(uuid));
    }

    public static boolean isPlayerNicked(String name) {

        return getPlayerRealColoredName(name).equalsIgnoreCase(getPlayerColoredName(name));
    }

    public static boolean isPlayerNicked(Player player) {

        return isPlayerNicked(player.getUniqueId());
    }


    public static String getPlayerRankColor(String name) {

        return getNameColor(Bukkit.getOfflinePlayer(name).getUniqueId()).toString(); //default rank color

    }

    public static String getPlayerRankColor(UUID uuid) {

        return getNameColor(uuid).toString(); //default rank color
    }

    public static String getPlayerColoredName(String name) {

        return getNameFormatWithPrefix(Bukkit.getOfflinePlayer(name).getUniqueId()); //[NickRank] NickName
    }

    private static Boolean mmEnable = null;

    public static String getPlayerName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            User user = LuckPermsUtil.getUser(uuid);
            if (user != null) {
                return user.getUsername();
            }

            if (mmEnable == null) {
                mmEnable = Bukkit.getPluginManager().getPlugin("MythicMobs") != null;
            }

            if (mmEnable) {
                Optional<ActiveMob> mob = MythicMobs.inst().getMobManager().getActiveMob(uuid);
                if (mob.isPresent()) {
                    return mob.get().getType().getDisplayName();
                }
            }

            return "Bot";
        }

        return player.getDisplayName();
    }

    public static String getPlayerColoredName(UUID uuid) {
        return getNameFormatWithPrefix(uuid); //[NickRank] NickName

    }

    public static String getPlayerRealColoredName(String name) {

        try {
            UUID uuid = LuckPermsUtil.userManager.lookupUniqueId(name).get();
            return getNameFormat(uuid);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getPlayerRealColoredName(UUID uuid) {

        return getNameFormat(uuid); //fixme
    }

    public static String getNameFormat(UUID uuid) {
        return getNameColor(uuid) + getPlayerName(uuid);
    }


    public static String getNameFormatWithPrefix(UUID uuid) {
        String playerName = getPlayerName(uuid);

        if (playerName == null || "null".equals(playerName)) {
            return ThePit.getApi().getBotName();
        }
        return LuckPermsUtil.getPrefix(uuid) + getPlayerName(uuid);
    }

    public static ChatColor getNameColor(UUID uuid) {
        BaseComponent[] text = TextComponent.fromLegacyText(LuckPermsUtil.getPrefix(uuid));
        ChatColor color = text[text.length - 1].getColor();
        if (color == ChatColor.WHITE) color = ChatColor.GRAY;
        return color;
    }

}

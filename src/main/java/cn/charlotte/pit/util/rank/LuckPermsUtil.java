package cn.charlotte.pit.util.rank;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.ChatColor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsUtil {

    static LuckPerms luckPerms = LuckPermsProvider.get();
    static UserManager userManager = luckPerms.getUserManager();
    static GroupManager groupManager = luckPerms.getGroupManager();

    public LuckPermsUtil() {
    }

    public static String getPrefix(UUID uuid) {
        User user = getUser(uuid);
        return user != null && user.getCachedData().getMetaData().getPrefix() != null ? ChatColor.translateAlternateColorCodes('&', user.getCachedData().getMetaData().getPrefix()) + " " : "§7";
    }

    public static String getSuffix(UUID uuid) {
        User user = getUser(uuid);
        return user != null && user.getCachedData().getMetaData().getSuffix() != null ? " " + ChatColor.translateAlternateColorCodes('&', user.getCachedData().getMetaData().getSuffix()) : "§7";
    }

    public static User getUser(UUID uuid) {
        if (userManager == null) {
            return null;
        }

        if (userManager.isLoaded(uuid)) {
            return userManager.getUser(uuid);
        } else {
            CompletableFuture<User> userCompletableFuture = userManager.loadUser(uuid);
            return userCompletableFuture.join();
        }
    }

}

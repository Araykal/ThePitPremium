package cn.charlotte.pit.util.command.param.defaults;

import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.command.param.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerParameterType implements ParameterType<Player> {

    @Override
    public Player transform(final CommandSender sender, final String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals("")))
            return ((Player) sender);

        final Player player = Bukkit.getServer().getPlayer(source);

        if (player == null || (PlayerUtil.isVanished(player) && !PlayerUtil.isStaff((Player) sender))) {
            sender.sendMessage(ChatColor.RED + "玩家 " + source + " 不在线,请检查你的输入是否有误.");
            return (null);
        }

        return (player);
    }

    @Override
    public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
        final List<String> completions = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
                if (PlayerUtil.isVanished(player) && !PlayerUtil.isStaff(sender)) {
                    continue;
                }
                completions.add(player.getName());
            }
        }

        return completions;
    }

}
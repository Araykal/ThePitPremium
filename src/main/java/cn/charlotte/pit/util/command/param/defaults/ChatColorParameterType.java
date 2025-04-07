package cn.charlotte.pit.util.command.param.defaults;

import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.command.param.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatColorParameterType implements ParameterType<ChatColor> {
    @Override
    public ChatColor transform(CommandSender sender, String source) {
        return CC.getColorFromName(source);
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        final String compare = source.trim().toLowerCase();

        List<String> completed = new ArrayList<>();

        for (String colorName : CC.getColorNames()) {
            if (colorName.startsWith(compare)) {
                completed.add(colorName);
            }
        }

        return completed;
    }
}

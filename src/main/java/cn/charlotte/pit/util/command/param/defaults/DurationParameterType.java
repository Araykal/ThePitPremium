package cn.charlotte.pit.util.command.param.defaults;

import cn.charlotte.pit.util.command.param.ParameterType;
import cn.charlotte.pit.util.time.Duration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 15:32
 */
public class DurationParameterType implements ParameterType<Duration> {
    @Override
    public Duration transform(CommandSender sender, String source) {
        return Duration.fromString(source);
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return null;
    }
}

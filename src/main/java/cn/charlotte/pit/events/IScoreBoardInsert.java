package cn.charlotte.pit.events;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/31 22:12
 */
public interface IScoreBoardInsert {
    List<String> insert(Player player);
}

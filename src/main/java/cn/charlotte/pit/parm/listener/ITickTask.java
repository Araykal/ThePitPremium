package cn.charlotte.pit.parm.listener;

import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/29 23:00
 */
public interface ITickTask {

    /**
     * @param enchantLevel 持有玩家物品等级
     * @param player       持有物品玩家
     */
    void handle(int enchantLevel, Player player);

    /**
     * @return 每多少tick执行一次，20tick为1秒
     */
    int loopTick(int enchantLevel);

}

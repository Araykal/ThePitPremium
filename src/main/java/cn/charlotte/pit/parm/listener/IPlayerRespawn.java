package cn.charlotte.pit.parm.listener;

import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/2 0:04
 */
public interface IPlayerRespawn {

    /**
     * @param enchantLevel 附魔等级
     * @param myself       玩家对象（被攻击者，附魔物品持有者）
     */
    void handleRespawn(int enchantLevel, Player myself);

}

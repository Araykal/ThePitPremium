package cn.charlotte.pit.parm.listener;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:56
 * 4
 */
public interface IItemDamage {

    /**
     * @param enchantLevel 物品附魔等级
     * @param itemStack    物品
     * @param myself       玩家本身（物品持有者）
     * @param cancel       取消事件
     */
    void handleItemDamaged(int enchantLevel, ItemStack itemStack, Player myself, AtomicBoolean cancel);

}

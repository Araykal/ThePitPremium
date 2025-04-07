package cn.charlotte.pit.parm.listener;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/2 23:21
 */
public interface IPlayerAssist {
    /**
     * @param enchantLevel 附魔等级
     * @param myself       玩家对象（被攻击者，附魔物品持有者）
     * @param target       死亡生物
     * @param damage       物品初始伤害（未经过护甲处理）
     * @param finalDamage  最终伤害，即target受到的最终伤害
     * @param coins        结算金币，回调修改，最后统一结算发消息给玩家并且修改完架金币
     * @param experience   结算经验，回调修改，同上
     */
    void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience);

}

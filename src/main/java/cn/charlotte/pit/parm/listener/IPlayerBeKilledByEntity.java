package cn.charlotte.pit.parm.listener;

import cn.charlotte.pit.enchantment.param.listen.UnknowEntityType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/29 22:42
 * 4
 */
@UnknowEntityType
public interface IPlayerBeKilledByEntity {
    /**
     * @param enchantLevel 附魔等级
     * @param myself       玩家对象（被攻击者，附魔物品持有者）
     * @param target       击杀者
     * @param coins        结算金币，回调修改，最后统一结算发消息给玩家并且修改完架金币
     * @param experience   结算经验，回调修改，同上
     */
    void handlePlayerBeKilledByEntity(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience);
}

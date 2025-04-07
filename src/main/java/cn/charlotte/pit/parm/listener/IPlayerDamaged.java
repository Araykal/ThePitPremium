package cn.charlotte.pit.parm.listener;

import cn.charlotte.pit.enchantment.param.listen.UnknowEntityType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:56
 * 4
 */
@UnknowEntityType
public interface IPlayerDamaged {

    /**
     * @param enchantLevel 附魔等级
     * @param myself       玩家对象（被攻击者，附魔物品持有者）
     * @param attacker     （攻击者）
     * @param damage       物品初始伤害（未经过护甲处理）
     * @param finalDamage  最终伤害，即target受到的最终伤害
     * @param boostDamage  伤害倍率，将在所有附魔结算后统一计算，基础值为1
     * @param cancel       取消事件
     *                     Todo: @param projectile 攻击事件的投掷物(没有则为null)
     */
    void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel);

}

package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.beam.beam.Beam;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/17 22:35
 */
@ArmorOnly
public class LaserEnchant extends AbstractEnchantment implements IAttackEntity, ITickTask {
    private final Map<UUID, TargetInfo> tracker = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "镭射激光";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "laser";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.OP;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7攻击选中一名玩家并对其发射激光,持续5秒,每秒造成 &c0.5❤ 必中&7伤害";
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (tracker.containsKey(attacker.getUniqueId())) {
            return;
        }

        final Beam beam = new Beam(attacker.getLocation(), target.getLocation(), 100, 10);
        beam.start();

        final TargetInfo targetInfo = new TargetInfo();
        targetInfo.beam = beam;
        targetInfo.player = (Player) target;
        targetInfo.number = 0;

        tracker.put(attacker.getUniqueId(), targetInfo);
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        final TargetInfo target = tracker.get(player.getUniqueId());
        if (target == null) {
            return;
        }
        if (target.player == null) {
            return;
        }

        if (target.player.isOnline()) {
            if (player.getLocation().distanceSquared(target.player.getLocation()) <= 8 * 8 && target.number <= 5) {
                target.number++;
                target.beam.setStartingPosition(player.getLocation());
                target.beam.setEndingPosition(target.player.getLocation());
                target.player.damage(1, player);
                return;
            }
        }
        target.beam.stop();

        tracker.remove(player.getUniqueId());
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 10;
    }

    public static class TargetInfo {
        private Player player;
        private int number;
        private Beam beam;
    }
}

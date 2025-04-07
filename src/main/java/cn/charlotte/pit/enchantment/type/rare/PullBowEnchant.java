package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/6 22:22
 */
@cn.charlotte.pit.enchantment.param.item.BowOnly
public class PullBowEnchant extends AbstractEnchantment implements IPlayerShootEntity, IActionDisplayEnchant {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "吸力";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "pullbow_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7箭矢命中可以将敌人" + (enchantLevel >= 2 ? "及其周围2.5格的玩家" : "") + "&7向你所在的位置拖拽"
                + "/s&7此附魔" + (enchantLevel >= 3 ? "每 &f8 &7秒只能触发一次." : "&7每影响一名玩家,此附魔需要额外等待 &f8 &7秒才能再次触发.");
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetEntity = (Player) target;
        if (cooldown.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                int count = 0;
                for (Player targetPlayer : PlayerUtil.getNearbyPlayers(target.getLocation(), 2.5D)) {
                    if (targetPlayer == attacker) continue;

                    if (enchantLevel <= 1 && !targetPlayer.getUniqueId().equals(targetEntity.getUniqueId())) {
                        continue;
                    }
                    Vector direction = targetPlayer.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();
                    direction.setX(direction.getX() * -1);
                    direction.setZ(direction.getZ() * -1);
                    targetPlayer.setVelocity(direction);
                    count++;
                }
                cooldown.put(attacker.getUniqueId(), new Cooldown(enchantLevel >= 3 ? 8 : 8L * count, TimeUnit.SECONDS));
            }, 1L);
        }
    }

    @Override
    public String getText(int level, Player player) {
        return cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.get(player.getUniqueId()).getRemaining()).replace(" ", "");
    }
}

package cn.charlotte.pit.enchantment.type.special;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.rank.RankUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/22 17:30
 */
@AutoRegister
public class SoulRipperEnchant extends AbstractEnchantment implements IAttackEntity, IActionDisplayEnchant {

    public static final HashMap<UUID, UUID> buff = new HashMap<>();
    public static final HashMap<UUID, Integer> tier = new HashMap<>();
    public static final HashMap<UUID, Cooldown> duration = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "灵魂收割者";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "soul_ripper_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.SPECIAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7连续攻击同一玩家时,每一击造成的伤害 &c+15%"
                + "/s&7(可叠加,最高5层,持续5秒,每次连续攻击刷新持续时间,攻击不同玩家后重置)";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player victim = (Player) target;
        //remove buff if buff expired or player is hitting different enemies
        if (duration.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired() || victim.getUniqueId() != buff.getOrDefault(attacker.getUniqueId(), UUID.randomUUID())) {
            buff.remove(attacker.getUniqueId());
            tier.remove(attacker.getUniqueId());
        }

        buff.putIfAbsent(attacker.getUniqueId(), victim.getUniqueId());

        //refresh buff & add damage
        tier.put(attacker.getUniqueId(), Math.min(5, tier.getOrDefault(attacker.getUniqueId(), 0) + 1));
        duration.put(attacker.getUniqueId(), new Cooldown(5, TimeUnit.SECONDS));
        boostDamage.getAndAdd(tier.get(attacker.getUniqueId()) * 0.15);

    }

    @Override
    public String getText(int level, Player player) {
        StringBuilder builder = new StringBuilder();
        //buff目标不存在或持续时间已过时显示无目标
        if (!buff.containsKey(player.getUniqueId()) || duration.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() || !tier.containsKey(player.getUniqueId())) {
            builder.append("&c&l无");
            return builder.toString();
        }
        builder.append("&e&l")
                .append(CC.stripColor(RankUtil.getPlayerColoredName(buff.get(player.getUniqueId()))))
                .append(" ")
                .append("&c&l")
                .append(RomanUtil.convert(tier.getOrDefault(player.getUniqueId(), 1)))
                .append(" ")
                .append("&a&l")
                .append(TimeUtil.millisToSeconds(duration.getOrDefault(player.getUniqueId(), new Cooldown(0)).getRemaining()))
                .append("秒");
        return builder.toString();
    }
}

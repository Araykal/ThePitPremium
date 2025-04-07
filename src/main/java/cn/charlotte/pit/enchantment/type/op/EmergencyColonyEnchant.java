package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.buff.impl.HealPoisonDeBuff;
import cn.charlotte.pit.buff.impl.HemorrhageDeBuff;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Creator Misoryan
 * @Date 2021/5/11 16:35
 */
@ArmorOnly
public class EmergencyColonyEnchant extends AbstractEnchantment implements ITickTask, IPlayerDamaged {

    private static final HealPoisonDeBuff healPoisonDeBuff = new HealPoisonDeBuff();
    private static final HemorrhageDeBuff hemorrhageDeBuff = new HemorrhageDeBuff();
    private static final HashMap<UUID, Integer> hitCount = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "危险集群";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "emergency_cluster";
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
        return "&7每秒对周围 &e8 &7格内所有玩家施加 &e5 &7层 &a不愈之毒 &f(00:10) &7与"
                + "/s&2阻滞 &f(00:10) &7, &c流血 &f(00:10) &7效果,并恢复自身 &c1❤ &7生命值;"
                + "/s&7每 &e5 &7秒对周围 &e8 &7格内所有使用附魔 &9沉默 &7的玩家"
                + "/s&7劈下 &c7.5❤ &7普通伤害的闪电并附带击退;"
                + "/s&7受到攻击时为攻击者施加 &e2 &7层 &a不愈之毒 &f(00:20) &7效果,且"
                + "/s&7受到带有 &a不愈之毒 &7效果的玩家攻击时受到的伤害 &9-120% &7;"
                + "/s&7每受到 &e10 &7次攻击时,对周围 &e10 &7格内的所有玩家施加 &4失衡 &f(00:03)"
                + "/s&7与 &e40 &7层 &a不愈之毒 &f(00:10) &7效果,并劈下 &c10❤ &7普通伤害的闪电并附带击退."
                + "/s/s&7效果 &a不愈之毒&7: 恢复生命时每层降低恢复量 &a1%"
                + "/s&7效果 &2阻滞&7: 无法受到与被施加 &b速度 &7与 &a跳跃提升 &7效果"
                + "/s&7效果 &c流血&7: 无法受到与被施加 &6生命吸收 &7效果"
                + "/s&7效果 &4失衡&7: 失明,移动速度与攻击力极大幅度降低";
        /*
        return "&7TickTask20: buffStack_healPoison_tier4_duration10_range5"
                + "/s&7TickTask20: buffStack_pinDown_tier1_duration10_range5"
                + "/s&7TickTask20: buffStack_hemorrhaged_tier1_duration10_range5"
                + "/s&7TickTask20: heal_2"
                + "/s&7Damaged1: buffStack_healPoison_tier2_duration20"
                + "/s&7Damaged15: stun_duration3_range8"
                + "/s&7Damaged15: lightning_strike_damage15_range8"
                + "/s&7Damaged15: buffStack_healPoison_tier40_duration10_range8"
                + "/s&7Damaged: atk_decrease_rate1.2_ifCheckBuff_healPoison";

         */
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerUtil.heal(player, 2);
        for (Player p : PlayerUtil.getNearbyPlayers(player.getLocation(), 8)) {
            if (p.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            healPoisonDeBuff.stackBuff(p, 10 * 1000L, 5);
            hemorrhageDeBuff.stackBuff(p, 10 * 1000L);
            if ((System.currentTimeMillis() / 1000) % 3 == 0) {
                if (PlayerUtil.isEquippingSomber(p)) {
                    PlayerUtil.playThunderEffect(p.getLocation());
                    p.damage(15, player);
                }
            }
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player player = (Player) attacker;
        if (UtilKt.hasRealMan(player)) return;
        healPoisonDeBuff.stackBuff(player, 20 * 1000L, 2);
        hitCount.putIfAbsent(myself.getUniqueId(), 0);
        hitCount.put(myself.getUniqueId(), hitCount.get(myself.getUniqueId()) + 1);
        if (healPoisonDeBuff.getPlayerBuffData(player).getTier() > 0) {
            boostDamage.getAndAdd(-1.2);
        }
        if (hitCount.get(myself.getUniqueId()) % 10 == 0) {
            for (Player p : PlayerUtil.getNearbyPlayers(player.getLocation(), 10)) {
                if (p.getUniqueId().equals(myself.getUniqueId())) {
                    continue;
                }
                PlayerUtil.playThunderEffect(p.getLocation());
                p.damage(20, player);
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 20));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 20));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3 * 20, 20));
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20, 20));
            }
        }
    }
}

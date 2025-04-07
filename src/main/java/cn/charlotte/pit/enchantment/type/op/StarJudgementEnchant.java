package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/9 20:23
 */
@WeaponOnly
public class StarJudgementEnchant extends AbstractEnchantment implements IAttackEntity, IActionDisplayEnchant {
    @Override
    public String getEnchantName() {
        return "星辰裁决";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "star_judgement_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NOSTALGIA_RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7每" + (4 - enchantLevel <= 1 ? "" : (" &e" + (4 - enchantLevel) + " ")) + "&7次攻击有 &e" + (enchantLevel >= 3 ? "30%" : "20%") + " &7的几率造成二连击,"
                + "/s&7第二击造成 &c1❤ &7的&f真实&7伤害";
    }

    @Override
    public String getText(int level, Player player) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        return (hit % (Math.max(1, 4 - level)) == 0 ? "&a&l✔" : "&e&l" + (Math.max(1, 4 - level) - hit % (Math.max(1, 4 - level))));
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        int hit = (attacker.getItemInHand() != null && attacker.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getMeleeHit());
        final boolean b = hit % (Math.max(1, 4 - enchantLevel)) == 0;
        if (b) {
            final boolean success = RandomUtil.hasSuccessfullyByChance(enchantLevel >= 3 ? 0.3 : 0.2);
            if (success && target instanceof Player) {
                Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                    final Player player = (Player) target;
                    final double nowHealth = player.getHealth();
                    player.setHealth(Math.max(0.0, nowHealth - 2.0));
                });
            }
        }
    }
}

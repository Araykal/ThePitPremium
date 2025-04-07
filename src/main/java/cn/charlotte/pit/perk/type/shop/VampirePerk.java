package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.medal.impl.challenge.VampireMedal;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.type.BowOnly;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/1 22:22
 */
public class VampirePerk extends AbstractPerk implements IAttackEntity, IPlayerShootEntity, IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "Vampire";
    }

    @Override
    public String getDisplayName() {
        return "吸血鬼";
    }

    @Override
    public Material getIcon() {
        return Material.SPIDER_EYE;
    }

    @Override
    public double requireCoins() {
        return 4000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 55;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7攻击击中其他玩家时恢复自身 &c0.5❤ &7生命值.");
        lines.add("&7如果攻击为箭矢命中,则恢复量提升至 &c1.5❤ &7.");
        lines.add("&7击杀敌人额外获得 &c生命恢复 I &f(00:08) &7效果,");
        lines.add("&7但&c不会获得回复道具&7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {
    }

    @Override
    public void onPerkInactive(Player player) {
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }
        if (PlayerUtil.isPlayerChosePerk(attacker, "rambo")) {
            return;
        }
        new VampireMedal().addProgress(PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()), (int) Math.max(0, attacker.getMaxHealth() - Math.min(attacker.getHealth() + 1, attacker.getMaxHealth())));
        attacker.setHealth(Math.min(attacker.getHealth() + 1, attacker.getMaxHealth()));
    }

    @Override
    @BowOnly
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }
        if (PlayerUtil.isPlayerChosePerk(attacker, "rambo")) {
            return;
        }
        Player targetPlayer = (Player) target;
        if (Utils.getEnchantLevel(targetPlayer.getItemInHand(), "BulletTime") > 0 && targetPlayer.isBlocking()) {
            return;
        }
        new VampireMedal().addProgress(PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()), (int) Math.max(0, attacker.getMaxHealth() - Math.min(attacker.getHealth() + 3, attacker.getMaxHealth())));
        attacker.setHealth(Math.min(attacker.getHealth() + 3, attacker.getMaxHealth()));
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }
        if (PlayerUtil.isPlayerChosePerk(myself, "rambo")) {
            return;
        }
        Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
            myself.removePotionEffect(PotionEffectType.REGENERATION);
            myself.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 0));
        });
    }
}

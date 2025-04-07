package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.ActionBarUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/21 19:02
 */
public class MarathonPerk extends AbstractPerk implements IAttackEntity, IPlayerShootEntity, IPlayerDamaged, ITickTask {

    @Override
    public String getInternalPerkName() {
        return "marathon";
    }

    @Override
    public String getDisplayName() {
        return "马拉松";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_BOOTS;
    }

    @Override
    public double requireCoins() {
        return 8000;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 6;
    }

    @Override
    public int requireLevel() {
        return 90;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7装备此天赋且&c&l未装备靴子时&7获得以下效果:");
        lines.add("  &f▶ &7自身带有 &b速度 &7效果时造成的伤害 &c+18%");
        lines.add("  &f▶ &7自身带有 &b速度 &7效果时受到的伤害 &9-18%");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.getInventory().getBoots() == null || attacker.getInventory().getBoots().getType() == Material.AIR) {
            if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                boostDamage.getAndAdd(0.18);
            }
        }
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (myself.getInventory().getBoots() == null || myself.getInventory().getBoots().getType() == Material.AIR) {
            if (myself.hasPotionEffect(PotionEffectType.SPEED)) {
                boostDamage.getAndAdd(-0.18);
            }
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (attacker.getInventory().getBoots() == null || attacker.getInventory().getBoots().getType() == Material.AIR) {
            if (attacker.hasPotionEffect(PotionEffectType.SPEED)) {
                boostDamage.getAndAdd(0.15);
            }
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() != Material.AIR && PlayerUtil.isPlayerChosePerk(player, getInternalPerkName())) {
            ActionBarUtil.sendActionBar(player, "&c请取消装备靴子类护甲,否则天赋 马拉松 无法正常生效!");
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }
}

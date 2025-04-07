package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/21 19:10
 */
public class FirstStrikePerk extends AbstractPerk implements IAttackEntity, IPlayerShootEntity, ITickTask {

    private static final HashMap<UUID, List<UUID>> firstStrikes = new HashMap<>();

    @Override
    public String getInternalPerkName() {
        return "first_strike";
    }

    @Override
    public String getDisplayName() {
        return "先发制人";
    }

    @Override
    public Material getIcon() {
        return Material.COOKED_CHICKEN;
    }

    @Override
    public double requireCoins() {
        return 8000;
    }

    @Override
    public double requireRenown(int level) {
        return 25;
    }

    @Override
    public int requirePrestige() {
        return 5;
    }

    @Override
    public int requireLevel() {
        return 80;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7对其他玩家的首次攻击造成伤害 &c+35% &7,");
        lines.add("&7且额外获得 &b速度 I &f(00:05)");
        lines.add("&7自身死亡时重置首次攻击统计.");
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
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        firstStrikes.putIfAbsent(attacker.getUniqueId(), new ArrayList<>());
        if (firstStrikes.get(attacker.getUniqueId()).contains(targetPlayer.getUniqueId())) {
            return;
        }
        firstStrikes.get(attacker.getUniqueId()).add(target.getUniqueId());
        boostDamage.getAndAdd(0.35);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 0), true);
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player targetPlayer = (Player) target;
        firstStrikes.putIfAbsent(attacker.getUniqueId(), new ArrayList<>());
        if (firstStrikes.get(attacker.getUniqueId()).contains(targetPlayer.getUniqueId())) {
            return;
        }
        firstStrikes.get(attacker.getUniqueId()).add(target.getUniqueId());
        boostDamage.getAndAdd(0.35);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 0), true);
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isInArena() && firstStrikes.get(player.getUniqueId()) != null) {
            firstStrikes.get(player.getUniqueId()).clear();
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20 * 3;
    }
}

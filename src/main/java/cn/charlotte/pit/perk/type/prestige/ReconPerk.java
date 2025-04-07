package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/24 20:54
 */
public class ReconPerk extends AbstractPerk implements IPlayerShootEntity {
    private final Map<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getInternalPerkName() {
        return "recon_perk";
    }

    @Override
    public String getDisplayName() {
        return "侦察兵";
    }

    @Override
    public Material getIcon() {
        return Material.EYE_OF_ENDER;
    }

    @Override
    public double requireCoins() {
        return 6000;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 7;
    }

    @Override
    public int requireLevel() {
        return 60;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7每 &e4 &7次箭矢命中造成的伤害 &c+50% &7并额外获得 &b40 经验值 &7.");
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
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        final Cooldown cooldown = this.cooldown.get(attacker.getUniqueId());
        if (cooldown != null && !cooldown.hasExpired()) {
            return;
        }

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getBowHit() % 4 == 0) {
            boostDamage.getAndAdd(0.5);
            profile.setExperience(profile.getExperience() + 40);

            this.cooldown.put(attacker.getUniqueId(), new Cooldown(2, TimeUnit.SECONDS));
        }
    }
}

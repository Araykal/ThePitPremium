package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/1 22:13
 */
public class StrengthPerk extends AbstractPerk implements IPlayerKilledEntity, ITickTask, IAttackEntity {
    @Override
    public String getInternalPerkName() {
        return "Strength";
    }

    @Override
    public String getDisplayName() {
        return "力量";
    }

    @Override
    public Material getIcon() {
        return Material.REDSTONE;
    }

    @Override
    public double requireCoins() {
        return 2000;
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
        return 20;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7每击杀一名玩家,你在7秒内的攻击力 &c+4% &7,");
        lines.add("&7攻击力增益可叠加,最高10层,每次增加重置持续时间.");
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
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        profile.setStrengthTimer(new Cooldown(7, TimeUnit.SECONDS));
        if (profile.getStrengthNum() < 10) {
            profile.setStrengthNum(profile.getStrengthNum() + 1);
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getStrengthTimer().hasExpired()) {
            profile.setStrengthNum(0);
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getStrengthNum() > 0 && !profile.getStrengthTimer().hasExpired() && profile.getStrengthNum() <= 10) {
            boostDamage.set(boostDamage.get() + (profile.getStrengthNum() * 4D / 100D));
        }
    }
}

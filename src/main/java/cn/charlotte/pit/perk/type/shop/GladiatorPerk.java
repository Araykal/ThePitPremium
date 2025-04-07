package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/2 17:27
 */
public class GladiatorPerk extends AbstractPerk implements IPlayerDamaged {
    @Override
    public String getInternalPerkName() {
        return "Gladiator";
    }

    @Override
    public String getDisplayName() {
        return "角斗士";
    }

    @Override
    public Material getIcon() {
        return Material.BONE;
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
        return 60;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7在以你为中心的 &b12 &7格范围内,");
        lines.add("&7每存在一名除你以外的玩家,");
        lines.add("&7你受到的伤害 &9-3% &7(最高叠加10层,叠加低于3层时无效果) .");
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
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        double boost = PlayerUtil.getNearbyPlayers(myself.getLocation(), 12).size();

        int sybilLevel = Utils.getEnchantLevel(myself.getInventory().getLeggings(), "sybil");
        if (sybilLevel > 0) {
            boost += sybilLevel + 1;
        }

        if (boost > 10) {
            boost = 10;
        }
        if (boost < 3) {
            boost = 0;
        }
        boostDamage.set(boostDamage.get() - 0.01 * 3 * boost);
    }
}

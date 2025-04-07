package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.parm.listener.IPlayerAssist;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/25 17:22
 */
public class CoopCatPerk extends AbstractPerk implements IPlayerAssist {

    @Override
    public String getInternalPerkName() {
        return "CoopCat";
    }

    @Override
    public String getDisplayName() {
        return "助攻小猫";
    }

    @Override
    public Material getIcon() {
        return Material.RAW_FISH;
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
        return 5;
    }

    @Override
    public int requireLevel() {
        return 50;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7助攻获得的硬币与经验值 &b+50% &7.");
        return lines;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience) {
        coins.getAndAdd(0.5);
        experience.getAndAdd(0.5);
    }
}

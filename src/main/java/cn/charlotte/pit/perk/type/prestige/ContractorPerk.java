package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/6 10:35
 */
public class ContractorPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "contractor_perk";
    }

    @Override
    public String getDisplayName() {
        return "契约者";
    }

    @Override
    public Material getIcon() {
        return Material.ENCHANTED_BOOK;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 5;
    }

    @Override
    public int requirePrestige() {
        return 2;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList("&7每次升级:", "  &7每日可额外完成 &e2 &7次挑战任务.");
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }
}

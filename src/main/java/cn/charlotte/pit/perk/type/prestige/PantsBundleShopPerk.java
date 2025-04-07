package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/25 18:33
 */
public class PantsBundleShopPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "pants_bundle_shop_unlock";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 神话之甲收纳箱";
    }

    @Override
    public Material getIcon() {
        return Material.STORAGE_MINECART;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 12;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在商店中购买神话之甲收纳箱.");
        lines.add("&7神话之甲收纳箱使用后会打包背包内");
        lines.add("&710条&c未附魔&7的神话之甲.");
        lines.add("&7并允许你随时将其取出.");
        return lines;
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
}

package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/2 17:07
 */
public class SelfConfidencePerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "self_confidence";
    }

    @Override
    public String getDisplayName() {
        return "自信";
    }

    @Override
    public Material getIcon() {
        return Material.RAW_FISH;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 50;
    }

    @Override
    public int requirePrestige() {
        return 9;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7参与 &e大型事件 &7获得靠前排名可获得额外硬币:");
        lines.add("  &f▶ &7#1 - #5 : &6+5000 硬币");
        lines.add("  &f▶ &7#6 - #10 : &6+2500 硬币");
        lines.add("  &f▶ &7#11 - #15 : &6+1000 硬币");
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
    public ItemStack getIconWithNameAndLore(String name, List<String> lore, int durability, int amount) {
        return new ItemBuilder(Material.RAW_FISH)
                .durability(3)
                .name(name)
                .lore(lore)
                .amount(amount)
                .build();
    }
}

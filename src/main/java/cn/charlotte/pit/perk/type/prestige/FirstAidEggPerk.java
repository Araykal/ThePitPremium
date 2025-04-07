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
 * @Created_In: 2021/3/17 16:58
 */
public class FirstAidEggPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "first_aid_egg_perk";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 急救蛋";
    }

    @Override
    public Material getIcon() {
        return Material.MONSTER_EGG;
    }

    @Override //200 coins for the item
    public double requireCoins() {
        return 0;
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
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在商店中购买急救蛋.");
        lines.add(" ");
        lines.add("&7急救蛋会在使用后,恢复使用者 &c2.5❤ &7生命值并移除使用者的速度效果.");
        lines.add("&7&o30 秒内只能使用一次急救蛋,击杀玩家可以减少5秒冷却时间.");
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
        return new ItemBuilder(Material.MONSTER_EGG)
                .durability(96)
                .name(name)
                .lore(lore)
                .amount(amount)
                .build();
    }
}

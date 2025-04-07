package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/22 16:11
 */
public class BarbarianPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "Barbarian";
    }

    @Override
    public String getDisplayName() {
        return "野蛮人";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_AXE;
    }

    @Override
    public double requireCoins() {
        return 3000;
    }

    @Override
    public double requireRenown(int level) {
        return 10;
    }

    @Override
    public int requirePrestige() {
        return 2;
    }

    @Override
    public int requireLevel() {
        return 30;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7天赋携带期间额外获得以下物品:");
        lines.add("  &f▶ 铁斧 &7(&9+7 基础攻击力&7)");
        lines.add("&7天赋携带期间替换商店商品 &b钻石剑 &7为:");
        lines.add("  &f▶ &b钻石斧 &7(&9+8 基础攻击力&7) (&6150 硬币&7)");
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
        player.getInventory().addItem(new ItemBuilder(Material.IRON_AXE).canDrop(false).deathDrop(false).canSaveToEnderChest(false).internalName("perk_barbarian").lore("&7天赋物品").itemDamage(7).buildWithUnbreakable());
    }

    @Override
    public void onPerkInactive(Player player) {
        InventoryUtil.removeItemWithInternalName(player, "perk_barbarian");
    }
}

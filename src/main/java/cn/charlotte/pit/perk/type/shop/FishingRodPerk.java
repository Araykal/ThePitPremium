package cn.charlotte.pit.perk.type.shop;

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
 * @Created_In: 2021/1/1 22:11
 */
public class FishingRodPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "FishingRod";
    }

    @Override
    public String getDisplayName() {
        return "渔夫";
    }

    @Override
    public Material getIcon() {
        return Material.FISHING_ROD;
    }

    @Override
    public double requireCoins() {
        return 1000;
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
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int requireLevel() {
        return 10;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7天赋携带期间额外获得以下物品:");
        lines.add("  &f▶ &a钓鱼竿");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.FISHING_ROD).deathDrop(false).canDrop(false).canSaveToEnderChest(false).internalName("perk_fishing_rod").lore("&7天赋物品").buildWithUnbreakable());
    }

    @Override
    public void onPerkInactive(Player player) {
        InventoryUtil.removeItemWithInternalName(player, "perk_fishing_rod");
    }
}

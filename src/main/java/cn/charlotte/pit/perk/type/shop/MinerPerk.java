package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.impl.challenge.MinerMedal;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/3 20:26
 */
public class MinerPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "Miner";
    }

    @Override
    public String getDisplayName() {
        return "矿工";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    public double requireCoins() {
        return 3000;
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
        return 30;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7天赋携带期间额外获得以下物品:");
        lines.add("  &f▶ &b钻石稿 &7(&9效率 IV&7)");
        lines.add("  &f▶ &f24 * 圆石");
        lines.add("&7击杀玩家额外获得以下物品:");
        lines.add("  &f▶ &f3 * 圆石");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_PICKAXE).deathDrop(false).canDrop(false).canSaveToEnderChest(false).internalName("perk_miner").enchant(Enchantment.DIG_SPEED, 4).buildWithUnbreakable());
        player.getInventory().addItem(new ItemBuilder(Material.COBBLESTONE).canDrop(false).canSaveToEnderChest(false).deathDrop(true).internalName("perk_miner").amount(24).build());
    }

    @Override
    public void onPerkInactive(Player player) {
        InventoryUtil.removeItemWithInternalName(player, "perk_miner");
    }

    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        myself.getInventory().addItem(new ItemBuilder(Material.COBBLESTONE).canDrop(false).deathDrop(true).canSaveToEnderChest(false).internalName("perk_miner").amount(3).build());
        if (InventoryUtil.getAmountOfItem(myself, new ItemBuilder(Material.COBBLESTONE).canDrop(false).deathDrop(true).canSaveToEnderChest(false).internalName("perk_miner").build()) >= 64) {
            new MinerMedal().setProgress(PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId()), 1);
        }
    }
}

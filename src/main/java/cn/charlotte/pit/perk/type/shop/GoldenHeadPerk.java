package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/1 21:42
 */
public class GoldenHeadPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "GoldenHead";
    }

    @Override
    public String getDisplayName() {
        return "金头";
    }

    @Override
    public Material getIcon() {
        return Material.SKULL_ITEM;
    }

    @Override
    public double requireCoins() {
        return 500;
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
        return 10;
    }

    @Override
    public int getMaxLevel() {
        return 0;
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
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7将击杀获得的回复道具变成 &6金头 &7.");
        lines.add("&7食用 &6金头 &7后获得以下效果:");
        lines.add("  &f▶ &c生命恢复 I &f(00:04)");
        lines.add("  &f▶ &6伤害吸收 &f(&63❤&f)");
        return lines;
    }

    @PlayerOnly
    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }

        if (PlayerUtil.getAmountOfActiveHealingPerk(myself) != 1) {
            return;
        }

        if (PlayerUtil.getPlayerHealItemAmount(myself) < PlayerUtil.getPlayerHealItemLimit(myself)) {
            for (ItemStack itemStack : myself.getInventory()) {
                if (itemStack != null && itemStack.getType() == Material.SKULL_ITEM && ItemUtil.getInternalName(itemStack).equals("golden_head")) {
                    itemStack.setAmount(itemStack.getAmount() + 1);
                    return;
                }
            }
            myself.getInventory().addItem(new ItemBuilder(Material.SKULL_ITEM)
                    .setSkullProperty("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyZGNhNjhjOGY4YWY1MzNmYjczN2ZhZWVhY2JlNzE3Yjk2ODc2N2ZjMTg4MjRkYzJkMzdhYzc4OWZjNzcifX19")
                    .durability(3)
                    .name("&6金头")
                    .deathDrop(true)
                    .removeOnJoin(true)
                    .canSaveToEnderChest(false)
                    .canDrop(false)
                    .canTrade(false)
                    .isHealingItem(true)
                    .internalName("golden_head")
                    .build());
        }
    }

    @Override
    public ItemStack getIconWithNameAndLore(String name, List<String> lore, int durability, int amount) {
        return new ItemBuilder(Material.SKULL_ITEM)
                .setSkullProperty("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyZGNhNjhjOGY4YWY1MzNmYjczN2ZhZWVhY2JlNzE3Yjk2ODc2N2ZjMTg4MjRkYzJkMzdhYzc4OWZjNzcifX19")
                .durability(3)
                .name(name)
                .lore(lore)
                .amount(amount)
                .build();
    }
}

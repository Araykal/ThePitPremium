package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/7 15:39
 */
@Passive
public class CelebrityPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "celebrity";
    }

    @Override
    public String getDisplayName() {
        return "\"声望\"";
    }

    @Override
    public Material getIcon() {
        return Material.RAW_FISH;
    }

    @Override
    public ItemStack getIconWithNameAndLore(String name, List<String> lore, int durability, int amount) {
        return new ItemBuilder(getIcon())
                .name(name)
                .lore(lore)
                .durability(3)
                .build();
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 300;
    }

    @Override
    public int requirePrestige() {
        return 30;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&o\"旅途有终点。\"");
        lines.add("");
        lines.add("&7击杀获得的硬币 &6+100% &7.");
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
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        coins.getAndAdd(coins.get());
    }
}

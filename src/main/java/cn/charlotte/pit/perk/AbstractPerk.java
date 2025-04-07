package cn.charlotte.pit.perk;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 21:18
 */
public abstract class AbstractPerk {

    public abstract String getInternalPerkName();

    public abstract String getDisplayName();

    public abstract Material getIcon();

    public abstract double requireCoins();

    public abstract double requireRenown(int level);

    public abstract int requirePrestige();

    public abstract int requireLevel();

    public abstract List<String> getDescription(Player player);

    public abstract int getMaxLevel();

    public abstract PerkType getPerkType();

    public abstract void onPerkActive(Player player);

    public abstract void onPerkInactive(Player player);

    public void onUnlock(Player player) {

    }

    public void onUpgrade(Player player) {

    }

    public int getPlayerLevel(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        boolean passiveFound = this.getClass().isAnnotationPresent(Passive.class);

        if (passiveFound) {
            PerkData perkData = profile.getUnlockedPerkMap().get(getInternalPerkName());
            if (perkData != null) {
                return perkData.getLevel();
            } else {
                return -1;
            }
        }

        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            PerkData perkData = entry.getValue();
            if (perkData.getPerkInternalName().equals(this.getInternalPerkName())) {
                return perkData.getLevel();
            }
        }
        return -1;
    }

    public ItemStack getIconWithNameAndLore(String name, List<String> lore, int durability, int amount) {
        return new ItemBuilder(getIcon())
                .name(name)
                .lore(lore)
                .durability(durability)
                .amount(1)
                .build();
    }

}

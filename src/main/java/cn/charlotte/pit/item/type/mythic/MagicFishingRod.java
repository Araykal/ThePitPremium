package cn.charlotte.pit.item.type.mythic;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/7 1:10
 */
public class MagicFishingRod extends IMythicItem {
    @Override
    public String getInternalName() {
        return "magic_fishing_rod";
    }

    @Override
    public String getItemDisplayName() {
        return "神奇鱼竿";
    }

    @Override
    public Material getItemDisplayMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        if (isEnchanted()) {
            lore.add("");
            for (Map.Entry<AbstractEnchantment, Integer> entry : this.enchantments.entrySet()) {
                getEnchantLore(lore, entry, enchantments.entrySet());
            }
        } else {
            lore.add("&7死亡后保留");
            lore.add("");
            lore.add("&7在神话之井中使用 | " + ThePit.getApi().getWaterMark());
        }


        ItemBuilder builder = new ItemBuilder(this.getItemDisplayMaterial())
                .name(getItemDisplayName())
                .lore(lore)
                .internalName(getInternalName())
                .deathDrop(false)
                .canDrop(false)
                .canTrade(true)
                .canSaveToEnderChest(true)
                .removeOnJoin(false)
                .enchant(enchantments);
        if (isEnchanted()) {
            builder.shiny();
        }
        return builder.build();
    }


}

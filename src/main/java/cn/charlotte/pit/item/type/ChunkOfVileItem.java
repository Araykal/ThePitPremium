package cn.charlotte.pit.item.type;

import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChunkOfVileItem {

    public static ItemStack toItemStack() {
        return new ItemBuilder(Material.COAL)
                .name("&5暗聚块")
                .lore(
                        "&7死亡后保留",
                        "",
                        "&c邪术收藏品"
                )
                .canSaveToEnderChest(true)
                .canTrade(true)
                .internalName(getInternalName())
                .build();
    }

    public static String getInternalName() {
        return "chunk_of_vile_item";
    }
}

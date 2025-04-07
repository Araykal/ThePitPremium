package cn.charlotte.pit.util;

import cn.charlotte.pit.item.IMythicItem;
import org.bukkit.inventory.ItemStack;

public class MythicUtil {

    /**
     * @param itemStack
     * @return 返回物品读取为IMythicItem后的形式, 如此物品不是MythicItem则返回null
     * (此物品需要能被附魔)
     */
    public static IMythicItem getMythicItem(ItemStack itemStack) {
        return Utils.getMythicItem(itemStack);
    }


}

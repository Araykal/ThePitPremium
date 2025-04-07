package cn.charlotte.pit.item.type.mythic;

import cn.charlotte.pit.item.IMythicItem;
import org.bukkit.Material;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 12:25
 */
public class MythicBowItem extends IMythicItem {
    @Override
    public String getInternalName() {
        return "mythic_bow";
    }

    @Override
    public String getItemDisplayName() {
        return "神话之弓";
    }

    @Override
    public Material getItemDisplayMaterial() {
        return Material.BOW;
    }
}

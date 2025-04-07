package cn.charlotte.pit.item.type.mythic;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.IMythicSword;
import org.bukkit.Material;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 11:38
 */
public class MythicSwordItem extends IMythicItem implements IMythicSword {
    @Override
    public String getInternalName() {
        return "mythic_sword";
    }

    @Override
    public String getItemDisplayName() {
        return "神话之剑";
    }

    @Override
    public Material getItemDisplayMaterial() {
        return Material.GOLD_SWORD;
    }

    @Override
    public double getItemDamage() {
        return 6.5;
    }
}

package cn.charlotte.pit.util.item;

import cn.charlotte.pit.UtilKt;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 1:37
 */
public class ItemUtil {



    public static String getUUID(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return null;
        }

        if (!extra.hasKey("uuid")) {
            return null;
        }

        return extra.getString("uuid");
    }




    public static boolean isIllegalItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return true;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return true;
        }

        return !extra.hasKey("internal") || getInternalName(item).endsWith("_reward");
    }

    public static boolean canDrop(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("tradeAllow") && extra.getBoolean("tradeAllow");
    }

    public static boolean isHealingItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("isHealingItem") && extra.getBoolean("isHealingItem");
    }

    public static boolean canTrade(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return (extra.hasKey("canTrade") && extra.getBoolean("canTrade")) || (getInternalName(item) != null && getInternalName(item).startsWith("mythic_"));
    }

    public static boolean canSaveEnderChest(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("enderChest") && extra.getBoolean("enderChest");
    }

    public static boolean isDefaultItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("defaultItem") && extra.getBoolean("defaultItem");
    }

    public static boolean isDeathDrop(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("deathDrop") && extra.getBoolean("deathDrop");
    }

    public static boolean isRemovedOnJoin(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return false;
        }

        return extra.hasKey("removeOnJoin") && extra.getBoolean("removeOnJoin");
    }

    public static String getInternalName(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return null;
        }

        if (!extra.hasKey("internal")) {
            return null;
        }

        return extra.getString("internal");
    }

    public static Integer getItemIntData(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return null;
        }

        if (!extra.hasKey(key)) {
            return null;
        }

        return extra.getInt(key);
    }

    public static String getItemStringData(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return null;
        }

        if (!extra.hasKey(key)) {
            return null;
        }

        return extra.getString(key);
    }

}

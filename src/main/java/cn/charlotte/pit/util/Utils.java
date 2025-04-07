package cn.charlotte.pit.util;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.item.type.AngelChestplate;
import cn.charlotte.pit.item.type.ArmageddonBoots;
import cn.charlotte.pit.item.type.GoldenHelmet;
import cn.charlotte.pit.item.type.LuckyChestplate;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Utils {


    /**
     * 返回-1为没有
     *
     * @param item
     * @param enchantName
     * @return
     */
    public static int getEnchantLevel(ItemStack item, String enchantName) {
        final IMythicItem mythicItem = getMythicItem(item);
        if (mythicItem == null) {
            return -1;
        }

        for (Map.Entry<AbstractEnchantment, Integer> entry : mythicItem.getEnchantments().entrySet()) {
            if (entry.getKey().getNbtName().equals(enchantName)) {
                return entry.getValue();
            }
        }

        return -1;
    }
    /**
     * 超级高效的split方法。
     *
     * @param line string
     * @return a array of strings
     */
    public static String[] splitByCharAt(final String line, final char delimiter)
    {
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter); // first substring

        while (j >= 0)
        {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }

    /**
     * 手写算法
     * @param ment
     * @param nbtTagList
     */

    public static void readEnchantments(Map<AbstractEnchantment,Integer> ment, NBTTagList nbtTagList) {
        int size = nbtTagList.size();
        for (int i = 0; i < size; i++) {
            String s = nbtTagList.getString(i);
            final int offset = 2;
            final int strLen = s.length();
            for (int length = strLen - offset; length > 0; length--) {
                char splitArg = s.charAt(length);
                boolean fastEqual = splitArg == ':'; //check equal
                if (fastEqual) { //nano respond 10x faster
                    int lengthUnsigned = length + 1;
                    int level;
                    if (lengthUnsigned + 1 == strLen) {
                        level = Character.getNumericValue(s.charAt(length + 1)); //read level as char
                    } else {
                        String substring = s.substring(length + 1, strLen);
                        level = Integer.parseInt(substring); //read Level as string
                    }
                    String substring = s.substring(0, length);
                    AbstractEnchantment enchantment = ThePit.getInstance()
                            .getEnchantmentFactor()
                            .getEnchantmentMap()
                            .get(substring);
                    if (enchantment == null) {
                        return;
                    }
                    ment.put(enchantment, level);
                }
            }
        }
    }

    public static boolean isAddon() {
        return checkClassExists("com.araykal.pit.Extend");
    }

    public static boolean checkClassExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static IMythicItem getMythicItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;

        final String internalName = ItemUtil.getInternalName(item);
        IMythicItem mythicItem = null;
        if ("mythic_sword".equals(internalName)) {
            mythicItem = new MythicSwordItem();
        } else if ("mythic_bow".equals(internalName)) {
            mythicItem = new MythicBowItem();
        } else if ("mythic_leggings".equals(internalName)) {
            mythicItem = new MythicLeggingsItem();
        } else if ("angel_chestplate".equals(internalName)) {
            mythicItem = new AngelChestplate();
        } else if ("armageddon_boots".equals(internalName)) {
            mythicItem = new ArmageddonBoots();
        } else if ("kings_helmet".equals(internalName)) {
            mythicItem = new GoldenHelmet();
        } else if ("lucky_chestplate".equals(internalName)) {
            mythicItem = new LuckyChestplate();
        } else {
            return null;
        }

        mythicItem.loadFromItemStack(item);

        return mythicItem;
    }

    public static boolean canUseGen(ItemStack item, boolean is) {
        if (item == null) {
            return false;
        }

        final IMythicItem mythicItem = FuncsKt.toMythicItem(item);
        if (mythicItem == null || !mythicItem.isEnchanted() || mythicItem.isBoostedByGem() || mythicItem.isBoostedByGlobalGem()) {
            return false;
        }

        if (mythicItem.getColor() == MythicColor.DARK) {
            return false;
        }

        for (Map.Entry<AbstractEnchantment, Integer> entry : mythicItem.getEnchantments().entrySet()) {
            if ((is == (entry.getKey().getRarity().getParentType() == EnchantmentRarity.RarityType.RARE)) && entry.getValue() < entry.getKey().getMaxEnchantLevel()) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack subtractLive( ItemStack item) {
        if (item == null) {
            return null;
        }

        final IMythicItem mythicLeggings = MythicUtil.getMythicItem(item);
        if (mythicLeggings == null) return null;
        if (mythicLeggings.isEnchanted()) {
            if (mythicLeggings.getLive() <= 1) {
                return new ItemStack(Material.AIR);
            } else {
                mythicLeggings.setLive(mythicLeggings.getLive() - 1);
                return mythicLeggings.toItemStack();
            }
        }
        return item;
    }

}

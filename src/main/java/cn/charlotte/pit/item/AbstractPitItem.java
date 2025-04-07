package cn.charlotte.pit.item;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.sub.EnchantmentRecord;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.chat.RomanUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:18
 * 4
 */
public abstract class AbstractPitItem {
    protected Map<AbstractEnchantment, Integer> enchantments = new HashMap<>();

    protected List<EnchantmentRecord> enchantmentRecords = new ArrayList<>();

    public AbstractPitItem() {
    }

    public abstract String getInternalName();

    public abstract String getItemDisplayName();

    public abstract Material getItemDisplayMaterial();

    public boolean isEnchanted() {
        return !enchantments.isEmpty();
    }


    protected void getEnchantLore(List<String> lore, Map.Entry<AbstractEnchantment, Integer> entry, Set<Map.Entry<AbstractEnchantment, Integer>> entries) {
        if (lore == null || entry == null || entry.getKey() == null) {
            return;
        }
        lore.add(entry.getKey().getRarity().getPrefix() + "&9"
                + (entry.getKey().getRarity() == EnchantmentRarity.DISABLED || entry.getKey().getRarity() == EnchantmentRarity.REMOVED ? "&m" : "")
                + entry.getKey().getEnchantName() + " " + (entry.getValue() >= 2 ? RomanUtil.convert(entry.getValue()) : "") + "&r");
        if (entries.size() < 6) {
            String[] split = entry.getKey().getUsefulnessLore(entry.getValue()).split("/s");
            if (entry.getKey().getRarity() != EnchantmentRarity.REMOVED) {
                for (String s : split) {
                    lore.add("&7" + s);
                }
            } else {
                lore.add("&7此附魔已被移除. | " + ThePit.getApi().getWaterMark());
            }
            if (entry.getKey().getRarity() == EnchantmentRarity.DISABLED) {
                lore.add("&7此附魔暂时被管理员停用. | " + ThePit.getApi().getWaterMark());
            }
            lore.add(" ");
        }
    }

    public abstract ItemStack toItemStack();

    public abstract void loadFromItemStack(ItemStack item);

    public List<String> getEnchantLore() {
        List<String> lore = new ArrayList<>();
        if (!isEnchanted()) {
            return lore;
        }

        for (Map.Entry<AbstractEnchantment, Integer> entry : enchantments.entrySet()) {
            getEnchantLore(lore, entry, enchantments.entrySet());
        }

        return lore;
    }


    public Map<AbstractEnchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    public void setEnchantments(Map<AbstractEnchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractPitItem)) return false;
        final AbstractPitItem other = (AbstractPitItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$enchantments = this.getEnchantments();
        final Object other$enchantments = other.getEnchantments();
        if (this$enchantments == null ? other$enchantments != null : !this$enchantments.equals(other$enchantments))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AbstractPitItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $enchantments = this.getEnchantments();
        result = result * PRIME + ($enchantments == null ? 43 : $enchantments.hashCode());
        return result;
    }

    public String toString() {
        return "AbstractPitItem(enchantments=" + this.getEnchantments() + ")";
    }

    public List<EnchantmentRecord> getEnchantmentRecords() {
        return enchantmentRecords;
    }
}

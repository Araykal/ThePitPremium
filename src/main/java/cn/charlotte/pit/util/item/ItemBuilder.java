package cn.charlotte.pit.util.item;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 1:04
 */

import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.data.sub.EnchantmentRecord;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {
    private static int dontStack = 0;
    private ItemStack is;

    public ItemBuilder(Material mat) {
        this.is = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder material(Material mat) {
        this.is = new ItemStack(mat);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder dontStack() {
        dontStack++;
        return changeNbt("uuid", dontStack);
    }

    public ItemBuilder setLetherColor(Color color) {
        LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
        im.setColor(color);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(owner);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setSkullProperty(String texture) {
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", texture));
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gp);
        } catch (Exception ignored) {
        }
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name) {
        ItemMeta meta = this.is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int i) {
        this.is.addUnsafeEnchantment(enchantment, i);
        return this;
    }

    public ItemBuilder customName(String customName) {
        return this.changeNbt("customName", customName);
    }

    public ItemBuilder prefix(String prefix) {
        return this.changeNbt("prefix", prefix);
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.is.setDurability((short) durability);
        return this;
    }

    public ItemBuilder jewelSwordKills(int kills) {
        this.changeNbt("killed", kills);
        return this;
    }
    public ItemBuilder makeBoostedByGlobalGem(boolean boosted) {
        this.changeNbt("boostedByGlobalGem", boosted);
        return this;
    }
    public ItemBuilder makeBoostedByGem(boolean boosted) {
        this.changeNbt("boostedByGem", boosted);
        return this;
    }

    public ItemBuilder makeBoostedByBook(boolean boosted) {
        this.changeNbt("boostedByBook", boosted);
        return this;
    }

    public ItemBuilder recordEnchantments(List<EnchantmentRecord> records) {
        final StringBuilder builder = new StringBuilder();

        if (records.size() > 5) {
            records = records.subList(records.size() - 5, records.size());
        }

        for (EnchantmentRecord record : records) {
            if (builder.length() > 0) {
                builder.append(";");
            }

            builder.append(record.getEnchanter())
                    .append("|")
                    .append(record.getDescription())
                    .append("|")
                    .append(record.getTimestamp());
        }

        this.changeNbt("records", builder.toString());

        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder shiny() {
        return this.enchant(Enchantment.LURE, 1).flags(ItemFlag.values());
    }

    public ItemBuilder flags(ItemFlag... flags) {
        ItemMeta itemMeta = this.is.getItemMeta();
        itemMeta.addItemFlags(flags);
        this.is.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder type(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.is.getItemMeta();
        meta.setLore(new ArrayList<>());
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {

        for (Enchantment e : this.is.getEnchantments().keySet()) {
            this.is.removeEnchantment(e);
        }

        return this;
    }

    public ItemBuilder canDrop(boolean allow) {
        this.changeNbt("tradeAllow", allow);
        return this;
    }

    public ItemBuilder isHealingItem(boolean isHealingItem) {
        this.changeNbt("isHealingItem", isHealingItem);
        return this;
    }

    public ItemBuilder canTrade(boolean allow) {
        this.changeNbt("canTrade", allow);
        return this;
    }

    public ItemBuilder defaultItem() {
        this.changeNbt("defaultItem", true);
        return this;
    }

    public ItemBuilder canSaveToEnderChest(boolean allow) {
        this.changeNbt("enderChest", allow);
        return this;
    }

    public ItemBuilder changeNbt(String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        extra.setString(key, value);
        tag.set("extra", extra);

        nmsItem.setTag(tag);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemBuilder changeNbt(String key, boolean value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        extra.setBoolean(key, value);
        tag.set("extra", extra);

        nmsItem.setTag(tag);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemBuilder changeNbt(String key, int value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        extra.setInt(key, value);
        tag.set("extra", extra);

        nmsItem.setTag(tag);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemBuilder changeNbt(String key, double value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        extra.setDouble(key, value);
        tag.set("extra", extra);

        nmsItem.setTag(tag);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemStack buildWithUnbreakable() {

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setBoolean("Unbreakable", true);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public ItemBuilder internalName(String name) {
        this.changeNbt("internal", name);
        return this;
    }

    public ItemBuilder removeOnJoin(boolean remove) {
        this.changeNbt("removeOnJoin", remove);
        return this;
    }

    public ItemBuilder uuid(UUID uuid) {
        this.changeNbt("uuid", uuid.toString());
        return this;
    }

    public ItemStack build() {
        return is;
    }

    public ItemBuilder deathDrop(boolean drop) {
        this.changeNbt("deathDrop", drop);
        return this;
    }

    public ItemBuilder enchant(Map<AbstractEnchantment, Integer> enchant) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }
        NBTTagList nbtTagList = new NBTTagList();

        for (Map.Entry<AbstractEnchantment, Integer> entry : enchant.entrySet()) {
            NBTTagString nbtTagString = new NBTTagString(entry.getKey().getNbtName() + ":" + entry.getValue());
            nbtTagList.add(nbtTagString);
        }
        extra.set("ench", nbtTagList);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemBuilder maxLive(int live) {
        this.changeNbt("maxLive", live);
        return this;
    }

    public ItemBuilder dyeColor(String color) {
        this.changeNbt("dyeColor", color);
        return this;
    }

    public ItemBuilder tier(int tier) {
        this.changeNbt("tier", tier);
        return this;
    }

    public ItemBuilder live(int live) {
        this.changeNbt("live", live);
        return this;
    }

    public ItemBuilder itemDamage(double damageValue) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagList modifiers = new NBTTagList();
        NBTTagCompound damage = new NBTTagCompound();
        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
        damage.set("Name", new NBTTagString("generic.attackDamage"));
        damage.set("Amount", new NBTTagDouble(damageValue));
        damage.set("Operation", new NBTTagInt(0));
        damage.set("UUIDLeast", new NBTTagInt(894654));
        damage.set("UUIDMost", new NBTTagInt(2872));
        modifiers.add(damage);
        tag.set("AttributeModifiers", modifiers);
        nmsItem.setTag(tag);

        this.is = CraftItemStack.asBukkitCopy(nmsItem);

        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffect effect, boolean b) {
        if (this.is.getItemMeta() instanceof PotionMeta) {
            final PotionMeta meta = (PotionMeta) this.is.getItemMeta();
            meta.addCustomEffect(effect, b);
            this.is.setItemMeta(meta);
        }

        return this;
    }
}

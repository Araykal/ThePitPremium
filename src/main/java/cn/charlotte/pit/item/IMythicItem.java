package cn.charlotte.pit.item;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.data.sub.EnchantmentRecord;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.item.type.ArmageddonBoots;
import cn.charlotte.pit.item.type.mythic.MagicFishingRod;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.random.RandomUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/23 23:45
 */
public abstract class IMythicItem extends AbstractPitItem {
    protected int maxLive;
    protected int live;
    protected int tier;
    protected MythicColor color;
    protected DyeColor dyeColor;
    protected String prefix;

    protected boolean boostedByGem = false;
    protected String customName = null;
    @Getter
    private final static UUID defUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public boolean boostedByGlobalGem = false;
    public boolean boostedByBook = false;

    public UUID uuid;

    public void resetUUID() {
        this.uuid = defUUID;
    }

    public IMythicItem() {
    }

    @Override
    public ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        String name = getItemDisplayName();
        if (this.color == null) {
            this.color = (MythicColor) RandomUtil.helpMeToChooseOne(MythicColor.RED, MythicColor.ORANGE, MythicColor.BLUE, MythicColor.GREEN, MythicColor.YELLOW);
        }

        //Guardian Enchant for Archangel Chestplate

        if (isEnchanted()) {
            if (tier == 0) {
                tier = 3;
            }
        }

        if (this instanceof MythicLeggingsItem) {
            name = color.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + color.getDisplayName() + "色" + getItemDisplayName();
            if (dyeColor != null) {
                name = dyeColor.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + "染色神话之甲";
            }
            if (color == MythicColor.DARK) {
                name = color.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + "暗黑之甲";
            }
            if (color == MythicColor.RAGE) {
                name = color.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + "暴怒之甲";
            }
            if (color == MythicColor.DARK_GREEN) {
                name = color.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + "下水道之甲";
            }
        } else if (this instanceof MythicSwordItem) {
            name = (tier >= 3 ? "&c" : "&e") + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + getItemDisplayName();
        } else if (this instanceof MythicBowItem) {
            name = (tier >= 3 ? "&c" : "&b") + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + getItemDisplayName();
        } else if (this instanceof MagicFishingRod) {
            name = "&e" + getItemDisplayName();
        }

        int rareAmount = 0; //amount of rare enchant in this item
        int enchantTotalLevel = 0; //total level of enchant item
        for (AbstractEnchantment abstractEnchantment : enchantments.keySet()) {
            if (abstractEnchantment.getRarity() == EnchantmentRarity.RARE) {
                if (color == MythicColor.RAGE && abstractEnchantment.getMaxEnchantLevel() == enchantments.get(abstractEnchantment)) {
                    this.prefix = "不可思议的";
                }
                rareAmount++;
            }
            enchantTotalLevel += enchantments.get(abstractEnchantment);
        }
        if (enchantTotalLevel >= 8) {
            if (color == MythicColor.RAGE) {
                this.prefix = "狂躁的"; //Manic
            } else {
                this.prefix = "传说中的"; //Legendary
            }
        }
        if (this.maxLive >= 100) {
            if (color == MythicColor.DARK) {
                name = color.getChatColor() + (tier > 0 ? RomanUtil.convert(tier) + " 阶" : "") + "恶魔之甲";
            } else {
                this.prefix = "精制的"; //Artifact
            }
        }
        if (rareAmount >= 2) {
            this.prefix = "不凡的"; //ExtraOrdinary
        }

        if (this.prefix != null) {
            name = name.substring(0, 2) + this.prefix + " " + name;
        }

        if (this.customName != null) {
            name = customName;
        }

        if (maxLive != 0) {
            lore.add(("&7生命: " + (live / (maxLive * 1.0) <= 0.6 ? (live / (maxLive * 1.0) <= 0.3 ? "&c" : "&e") : "&a") + live + "&7/" + maxLive) + (isBoostedByGem() ? "&a ♦" : "") + (isBoostedByGlobalGem() ? "&b ♦" : "") + (boostedByBook ? "&6 ᥀" : ""));
            lore.add("");
        }

        if (isEnchanted()) {
            final AbstractEnchantment somber = ThePit.getInstance().getEnchantmentFactor().getEnchantmentMap().get("somber_enchant");
            if (color == MythicColor.DARK && !enchantments.containsKey(somber)) {
                enchantments.put(somber, 1);
            }
            boolean genesisFound = false;

            for (Map.Entry<AbstractEnchantment, Integer> entry : enchantments.entrySet()) {
                getEnchantLore(lore, entry, enchantments.entrySet());
                if (entry.getKey().getRarity() == EnchantmentRarity.GENESIS) {
                    genesisFound = true;
                }
            }

            if (this instanceof MythicLeggingsItem) {
                if (color != MythicColor.DARK) {
                    lore.add((dyeColor == null ? color.getChatColor() : dyeColor.getChatColor()) + "穿着时提供与铁护腿相同的伤害减免效果 &7| " + ThePit.getApi().getWaterMark());
                } else {
                    lore.add((dyeColor == null ? color.getChatColor() : dyeColor.getChatColor()) + "穿着时提供与皮革护腿相同的伤害减免效果 &7| " + ThePit.getApi().getWaterMark());
                }
            } else {
                lore.add(ThePit.getApi().getWaterMark());
            }

            if (genesisFound) {
                lore.add(color.getChatColor() + "阵营活动奖励");
            }

        } else {
            lore.add("&7死亡后保留");
            lore.add("");
            if (this instanceof MythicLeggingsItem) {
                lore.add((dyeColor == null ? color.getChatColor() : dyeColor.getChatColor()) + "在神话之井中附魔");
                lore.add((dyeColor == null ? color.getChatColor() : dyeColor.getChatColor()) + "同时,也是一种潮流的象征 | " + ThePit.getApi().getWaterMark());
            } else {
                lore.add("&7在神话之井中附魔 | " + ThePit.getApi().getWaterMark());
            }
            this.tier = 0;
        }

        if (dyeColor != null && this instanceof MythicLeggingsItem) {
            lore.add("&7原: " + color.getChatColor() + color.getDisplayName() + "色神话之甲");
        }
/*        if (Utils.isFireWell()) {
            if (Final.MythicUUID) {
                lore.add(defUUID.equals(uuid) ? "&8请放入附魔台再取出进行刷新操作" : "&8" + (uuid == null ? "refresh" : uuid));
            }
        }*/
        //Dark Pants

        ItemBuilder builder = new ItemBuilder(this.getItemDisplayMaterial());
        if (name != null) {
            builder.name(name);
        }
        if (customName != null) {
            builder.customName(customName);
        }
        if (this instanceof IMythicSword) {
            IMythicSword mythicSword = (IMythicSword) this;
            builder
                    .lore(lore)
                    .internalName(getInternalName())
                    .deathDrop(false)
                    .canSaveToEnderChest(true)
                    .removeOnJoin(false)
                    .uuid(uuid == null ? UUID.randomUUID() : uuid)
                    .canDrop(false)
                    .canTrade(true)
                    .enchant(enchantments)
                    .itemDamage(mythicSword.getItemDamage())
                    .maxLive(this.maxLive)
                    .tier(this.tier)
                    .makeBoostedByGem(this.boostedByGem)
                    .makeBoostedByGlobalGem(this.boostedByGlobalGem)
                    .makeBoostedByBook(boostedByBook)
                    .live(this.live)
                    .recordEnchantments(enchantmentRecords);
        } else {
            builder
                    .lore(lore)
                    .internalName(getInternalName())
                    .deathDrop(false)
                    .uuid(uuid == null ? UUID.randomUUID() : uuid)
                    .canDrop(false)
                    .canTrade(true)
                    .canSaveToEnderChest(true)
                    .removeOnJoin(false)
                    .enchant(enchantments)
                    .maxLive(this.maxLive)
                    .live(this.live)
                    .makeBoostedByGem(this.boostedByGem)
                    .makeBoostedByGlobalGem(this.boostedByGlobalGem)
                    .makeBoostedByBook(boostedByBook)
                    .tier(this.tier)
                    .recordEnchantments(enchantmentRecords);
        }

        if (dyeColor != null) {
            builder.dyeColor(dyeColor.name());
        }

        if (isEnchanted()) {
            builder.shiny();
        }
        this.setMythicColor(builder, color);
        if (this instanceof MythicLeggingsItem || this instanceof ArmageddonBoots) {
            builder.setLetherColor(dyeColor == null ? color.getLeatherColor() : dyeColor.getColor());
        }

        if (this.prefix != null) {
            builder.prefix(prefix);
        }

        return builder
                .buildWithUnbreakable();
    }

    @Override
    public void loadFromItemStack(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        if (nmsItem == null) return;
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return;
        }
        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            return;
        }

        if (extra.hasKey("prefix")) {
            this.prefix = extra.getString("prefix");
        }

        if (extra.hasKey("boostedByGem")) {
            this.boostedByGem = extra.getBoolean("boostedByGem");
        } else {
            this.boostedByGem = false;
        }
        if (extra.hasKey("boostedByGlobalGem")) {
            this.boostedByGlobalGem = extra.getBoolean("boostedByGlobalGem");
        } else {
            this.boostedByGlobalGem = false;
        }
        if (extra.hasKey("customName")) {
            this.customName = extra.getString("customName");
        }

        if (extra.hasKey("boostedByBook")) {
            this.boostedByBook = extra.getBoolean("boostedByBook");
        } else {
            this.boostedByBook = false;
        }

        if (extra.hasKey("dyeColor")) {
            this.dyeColor = DyeColor.valueOf(extra.getString("dyeColor"));
        }

        if (extra.hasKey("uuid")) {
            this.uuid = UUID.fromString(extra.getString("uuid"));
        }


        if (!extra.hasKey("mythic_color")) {
            this.color = (MythicColor) RandomUtil.helpMeToChooseOne(MythicColor.RED, MythicColor.ORANGE, MythicColor.BLUE, MythicColor.GREEN, MythicColor.YELLOW);
        } else {
            String internalColor = extra.getString("mythic_color");
            boolean colorFound = false;
            for (MythicColor value : MythicColor.values()) {
                if (value.getInternalName().equals(internalColor)) {
                    this.color = value;
                    colorFound = true;
                    break;
                }
            }
            if (!colorFound) {
                this.color = (MythicColor) RandomUtil.helpMeToChooseOne(MythicColor.RED, MythicColor.ORANGE, MythicColor.BLUE, MythicColor.GREEN, MythicColor.YELLOW);
            }
        }

        this.maxLive = extra.getInt("maxLive");
        this.live = extra.getInt("live");

        this.enchantments = new HashMap<>();

        if (!extra.hasKey("ench")) {
            return;
        }

        final String recordsString = extra.getString("records");
        if (recordsString != null) {
            for (String recordString : Utils.splitByCharAt(recordsString, ';')) {
                final String[] split = Utils.splitByCharAt(recordString, '|');
                if (split.length >= 3) {
                    enchantmentRecords.add(
                            new EnchantmentRecord(
                                    split[0],
                                    split[1],
                                    Long.parseLong(split[2])
                            )
                    );
                }
            }
        }

        NBTTagList ench = extra.getList("ench", 8);

/*        for (int i = 0; i < ench.size(); i++) {
            String[] split = ench.getString(i).split(":");

            AbstractEnchantment enchantment = ThePit.getInstance()
                    .getEnchantmentFactor()
                    .getEnchantmentMap()
                    .get(split[0]);

            if (enchantment == null) {
                continue;
            }

            int level;
            try {
                level = Integer.parseInt(split[1]);
            } catch (Exception ignore) {
                continue;
            }

            enchantments.put(enchantment, level);
        }*/
        Utils.readEnchantments(enchantments, ench);
        if (!extra.hasKey("tier") && isEnchanted()) {
            if (color == MythicColor.DARK) {
                this.tier = 2;
            } else {
                this.tier = 3;
            }
        } else {
            if (extra.hasKey("tier")) {
                if (!isEnchanted()) {
                    this.tier = 0;
                } else {
                    this.tier = extra.getInt("tier");
                }
            }
        }
    }

    protected void setMythicColor(ItemBuilder builder, MythicColor color) {
        builder.changeNbt("mythic_color", color.getInternalName());
    }

    public int getMaxLive() {
        return this.maxLive;
    }

    public void setMaxLive(int maxLive) {
        this.maxLive = maxLive;
    }

    public int getLive() {
        return this.live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getTier() {
        return this.tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public MythicColor getColor() {
        return this.color;
    }

    public void setColor(MythicColor color) {
        this.color = color;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public void setDyeColor(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String toString() {
        return "IMythicItem(maxLive=" + this.getMaxLive() + ", live=" + this.getLive() + ", tier=" + this.getTier() + ", color=" + this.getColor() + ", dyeColor=" + this.getDyeColor() + ", prefix=" + this.getPrefix() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof IMythicItem)) return false;
        final IMythicItem other = (IMythicItem) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        if (this.getMaxLive() != other.getMaxLive()) return false;
        if (this.getLive() != other.getLive()) return false;
        if (this.getTier() != other.getTier()) return false;
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        if (this$color == null ? other$color != null : !this$color.equals(other$color)) return false;
        final Object this$dyeColor = this.getDyeColor();
        final Object other$dyeColor = other.getDyeColor();
        if (this$dyeColor == null ? other$dyeColor != null : !this$dyeColor.equals(other$dyeColor)) return false;
        final Object this$prefix = this.getPrefix();
        final Object other$prefix = other.getPrefix();
        if (this$prefix == null ? other$prefix != null : !this$prefix.equals(other$prefix)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof IMythicItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        result = result * PRIME + this.getMaxLive();
        result = result * PRIME + this.getLive();
        result = result * PRIME + this.getTier();
        final Object $color = this.getColor();
        result = result * PRIME + ($color == null ? 43 : $color.hashCode());
        final Object $dyeColor = this.getDyeColor();
        result = result * PRIME + ($dyeColor == null ? 43 : $dyeColor.hashCode());
        final Object $prefix = this.getPrefix();
        result = result * PRIME + ($prefix == null ? 43 : $prefix.hashCode());
        return result;
    }

    public void setBoostedByGlobalGem(boolean boostedByGlobalGem) {
        this.boostedByGlobalGem = boostedByGlobalGem;
    }

    public boolean isBoostedByGem() {
        return boostedByGem;
    }

    public boolean isBoostedByGlobalGem() {
        return boostedByGlobalGem;
    }

    public void setBoostedByGem(boolean boostedByGem) {
        this.boostedByGem = boostedByGem;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
}

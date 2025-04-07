package cn.charlotte.pit.menu.admin.item.button;

import cn.charlotte.pit.data.sub.EnchantmentRecord;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.RodOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.item.type.mythic.MagicFishingRod;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.random.RandomUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 22:49
 */
public class EnchantButton extends Button {
    private final AbstractEnchantment enchantment;

    public EnchantButton(AbstractEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean sword = enchantment.getClass().isAnnotationPresent(WeaponOnly.class);
        boolean bow = enchantment.getClass().isAnnotationPresent(BowOnly.class);
        boolean armor = enchantment.getClass().isAnnotationPresent(ArmorOnly.class);
        boolean rod = enchantment.getClass().isAnnotationPresent(RodOnly.class);
        EnchantmentRarity rarity = enchantment.getRarity();

        String s = "&7可用于: " + (sword ? "剑," : "") + (bow ? "弓," : "") + (armor ? "盔甲," : "") + (rod ? "鱼竿," : "");

        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR) || ItemUtil.getInternalName(player.getItemInHand()) == null) {
            return new ItemBuilder(Material.AIR).build();
        }
        if ((ItemUtil.getInternalName(player.getItemInHand()).equalsIgnoreCase("mythic_sword") && !sword)
                || (ItemUtil.getInternalName(player.getItemInHand()).equalsIgnoreCase("mythic_bow") && !bow)
                || (ItemUtil.getInternalName(player.getItemInHand()).equalsIgnoreCase("mythic_leggings") && !armor)
                || (ItemUtil.getInternalName(player.getItemInHand()).equals("magic_fishing_rod") && !rod)) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&9" + enchantment.getEnchantName())
                    .lore("", s.substring(0, s.length() - 1))
                    .build();
        }
        ItemBuilder itemBuilder = new ItemBuilder(Material.WOOL)
                .name("&9" + enchantment.getEnchantName())
                .lore("", "&7点击附魔或者升级一级物品", s.substring(0, s.length() - 1))
                .durability(rarity.getItemColor());
        if (rarity.getParentType() == EnchantmentRarity.RarityType.RARE) {
            itemBuilder.shiny();
        }
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        ItemStack item = player.getItemInHand();
        boolean canApply = true;
        if ("mythic_sword".equals(ItemUtil.getInternalName(item))) {
            if (canApply) {
                MythicSwordItem swordItem = new MythicSwordItem();
                swordItem.loadFromItemStack(item);
                swordItem.setMaxLive(RandomUtil.random.nextInt(8) + 16);
                swordItem.setLive(swordItem.getMaxLive());
                int level;

                if (swordItem.getEnchantments().containsKey(enchantment)) {

                    level = swordItem.getEnchantments().get(enchantment) + ((clickType.isLeftClick() ? 1 : -1));
                    if (level <= 0) {
                        swordItem.getEnchantments().remove(enchantment);
                    } else {
                        if (level > enchantment.getMaxEnchantLevel() && !clickType.isShiftClick()) {
                            player.sendMessage(CC.translate("&c已到达最大等级!"));
                            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 99);
                            return;
                        }
                    }
                } else {

                    level = clickType.isLeftClick() ? 1 : -1;

                }

                swordItem.getEnchantmentRecords().add(
                        new EnchantmentRecord(
                                player.getName(),
                                "管理员ench",
                                System.currentTimeMillis()
                        )
                );
                if (level != -1) {
                    swordItem.getEnchantments().put(enchantment, level);
                }
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 99);
                player.sendMessage(CC.translate("&a成功"));
                player.setItemInHand(swordItem.toItemStack());
                return;
            }
        } else if ("mythic_bow".equals(ItemUtil.getInternalName(item))) {
            if (canApply) {
                MythicBowItem bowItem = new MythicBowItem();
                bowItem.loadFromItemStack(item);
                bowItem.setMaxLive(RandomUtil.random.nextInt(8) + 16);
                bowItem.setLive(bowItem.getMaxLive());
                int level;
                if (bowItem.getEnchantments().containsKey(enchantment)) {
                    level = bowItem.getEnchantments().get(enchantment) + ((clickType.isLeftClick() ? 1 : -1));
                    if (level <= 0) {
                        bowItem.getEnchantments().remove(enchantment);
                    } else {
                        if (level > enchantment.getMaxEnchantLevel() && !clickType.isShiftClick()) {
                            player.sendMessage(CC.translate("&c已到达最大等级!"));
                            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 99);
                            return;
                        }
                    }
                } else {
                    level = clickType.isLeftClick() ? 1 : -1;
                }
                bowItem.getEnchantmentRecords().add(
                        new EnchantmentRecord(
                                player.getName(),
                                "管理员ench",
                                System.currentTimeMillis()
                        )
                );
                if (level != -1) {
                    bowItem.getEnchantments().put(enchantment, level);
                }
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 99);
                player.sendMessage(CC.translate("&a成功"));
                player.setItemInHand(bowItem.toItemStack());
                return;
            }
        } else if ("mythic_leggings".equals(ItemUtil.getInternalName(item))) {
            if (canApply) {
                MythicLeggingsItem leggingsItem = new MythicLeggingsItem();
                leggingsItem.loadFromItemStack(item);
                leggingsItem.setMaxLive(40);
                leggingsItem.setLive(40);
                int level;
                if (leggingsItem.getEnchantments().containsKey(enchantment)) {
                    level = leggingsItem.getEnchantments().get(enchantment) + (clickType.isLeftClick() ? 1 : -1);
                    if (level <= 0) {
                        leggingsItem.getEnchantments().remove(enchantment);
                    } else {
                        if (level > enchantment.getMaxEnchantLevel() && !clickType.isShiftClick()) {
                            player.sendMessage(CC.translate("&c已到达最大等级!"));
                            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 99);
                            return;
                        }
                    }
                } else {
                    level = clickType.isLeftClick() ? 1 : -1;
                }
                leggingsItem.getEnchantmentRecords().add(
                        new EnchantmentRecord(
                                player.getName(),
                                "管理员ench",
                                System.currentTimeMillis()
                        )
                );
                if (level != -1) {
                    leggingsItem.getEnchantments().put(enchantment, level);
                }
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 99);
                player.sendMessage(CC.translate("&a成功"));
                player.setItemInHand(leggingsItem.toItemStack());
                return;
            }
        } else if ("magic_fishing_rod".equals(ItemUtil.getInternalName(item))) {
            if (canApply) {
                MagicFishingRod fishingRod = new MagicFishingRod();
                fishingRod.loadFromItemStack(item);
                fishingRod.setMaxLive(40);
                fishingRod.setLive(40);
                int level;
                if (fishingRod.getEnchantments().containsKey(enchantment)) {
                    level = fishingRod.getEnchantments().get(enchantment) + (clickType.isLeftClick() ? 1 : -1);
                    if (level <= 0) {
                        fishingRod.getEnchantments().remove(enchantment);
                    } else {
                        if (level > enchantment.getMaxEnchantLevel() && !clickType.isShiftClick()) {
                            player.sendMessage(CC.translate("&c已到达最大等级!"));
                            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 99);
                            return;
                        }
                    }
                } else {
                    level = clickType.isLeftClick() ? 1 : -1;
                }

                if (level != -1) {
                    fishingRod.getEnchantments().put(enchantment, level);
                }
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 99);
                player.sendMessage(CC.translate("&a成功"));
                player.setItemInHand(fishingRod.toItemStack());
                return;
            }
        }
        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 99);
        player.sendMessage(CC.translate("&c附魔失败!你使用了错误的附魔在这个物品上"));
    }
}

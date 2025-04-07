package cn.charlotte.pit.menu.admin.item.button;

import cn.charlotte.pit.item.AbstractPitItem;
import cn.charlotte.pit.item.DyeColor;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.type.AngelChestplate;
import cn.charlotte.pit.item.type.ArmageddonBoots;
import cn.charlotte.pit.item.type.LuckyChestplate;
import cn.charlotte.pit.item.type.SpireSword;
import cn.charlotte.pit.item.type.mythic.MagicFishingRod;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 22:42
 */
public class MythicItemButton extends Button {
    private final int i;

    public MythicItemButton(int i) {
        this.i = i;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (i == 0) {
            return new MythicSwordItem().toItemStack();
        } else if (i == 1) {
            return new MythicBowItem().toItemStack();
        } else if (i == 2) {
            return new MythicLeggingsItem().toItemStack();
        } else if (i == 3) {
            return new MagicFishingRod().toItemStack();
        } else if (i == 4) {
            ItemStack itemStack = new MythicLeggingsItem().toItemStack();
            itemStack = new ItemBuilder(itemStack).changeNbt("mythic_color", "dark").build();
            AbstractPitItem mythicItem = new MythicLeggingsItem();
            mythicItem.loadFromItemStack(itemStack);
            return mythicItem.toItemStack();
        } else if (i == 5) {
            ItemStack itemStack = new AngelChestplate().toItemStack();
            IMythicItem mythicItem = new AngelChestplate();
            mythicItem.loadFromItemStack(itemStack);
            mythicItem.setLive(12);
            return mythicItem.toItemStack();
        } else if (i == 6) {
            ItemStack itemStack = new ArmageddonBoots().toItemStack();
            IMythicItem mythicItem = new ArmageddonBoots();
            mythicItem.loadFromItemStack(itemStack);
            mythicItem.setLive(3);
            return mythicItem.toItemStack();
        } else if (i == 7) {
            return new SpireSword(Material.DIAMOND_SWORD).toItemStack();
        } else if (i == 8) {
            ItemStack itemStack = new MythicLeggingsItem().toItemStack();
            itemStack = new ItemBuilder(itemStack).changeNbt("dyeColor", DyeColor.WHITE.name()).build();
            AbstractPitItem mythicItem = new MythicLeggingsItem();
            mythicItem.loadFromItemStack(itemStack);
            return mythicItem.toItemStack();
        } else if (i == 9) {
            return new LuckyChestplate().toItemStack();
        }
        return new ItemBuilder(Material.AIR).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        player.getInventory().addItem(currentItem);
    }


}

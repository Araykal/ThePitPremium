package cn.charlotte.pit.menu.workshop.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.item.DyeColor;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DyeButton extends Button {

    private final DyeColor dyeColor;

    public DyeButton(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        String current = "";
        if (profile.getCoins() < 200) {
            current = "&c你的硬币不足!";
        } else {
            current = "&e点击染色!";
        }
        return new ItemBuilder(Material.LEATHER_LEGGINGS)
                .name("&7将神话之井中物品染色为: " + dyeColor.getChatColor() + dyeColor.name())
                .lore(
                        "&7价格: &6200 硬币",
                        "",
                        current
                )
                .setLetherColor(dyeColor.getColor())
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getCoins() < 200) {
            return;
        }
        profile.setCoins(profile.getCoins() - 200);
        ItemStack itemStack = new ItemBuilder(InventoryUtil.deserializeItemStack(profile.getEnchantingItem())).changeNbt("dyeColor", dyeColor.name()).build();

        profile.setEnchantingItem(InventoryUtil.serializeItemStack(ThePit.getApi().reformatPitItem(itemStack)));
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

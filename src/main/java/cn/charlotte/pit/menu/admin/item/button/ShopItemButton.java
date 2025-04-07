package cn.charlotte.pit.menu.admin.item.button;

import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 22:44
 */
public class ShopItemButton extends Button {
    private final Material material;
    private final String internalName;
    private int amount = 1;

    public ShopItemButton(Material material, String internalName, int amount) {
        this.material = material;
        this.internalName = internalName;
        this.amount = amount;
    }

    public ShopItemButton(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.internalName = ItemUtil.getInternalName(itemStack);
        this.amount = itemStack.getAmount();
    }

    public ShopItemButton(Material material, String internalName) {
        this.material = material;
        this.internalName = internalName;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .amount(amount)
                .internalName(internalName)
                .canDrop(true)
                .canSaveToEnderChest(true)
                .removeOnJoin(false)
                .deathDrop(false)
                .canTrade(false)
                .buildWithUnbreakable();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        player.getInventory().addItem(getButtonItem(player));
    }
}

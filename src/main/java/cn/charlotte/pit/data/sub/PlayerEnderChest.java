package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.deserializer.EnderChestDeserializer;
import cn.charlotte.pit.data.serializer.EnderChestSerializer;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author EmptyIrony, Yurinan
 * @since 2020/12/29 23:05
 */

@JsonSerialize(using = EnderChestSerializer.class)
@JsonDeserialize(using = EnderChestDeserializer.class)
public class PlayerEnderChest {

    private Inventory inventory;

    public PlayerEnderChest() {
        inventory = Bukkit.createInventory(null, 54, "末影箱");
    }

    public static PlayerEnderChest deserialization(String string) {
        PlayerEnderChest enderChest = new PlayerEnderChest();
        enderChest.getInventory().setContents(InventoryUtil.stringToItems(string));

        return enderChest;
    }

    public void openEnderChest(Player player) {
        player.closeInventory();

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        int row = profile.getEnderChestRow();
        int limit = row * 9;

        for (int i = limit; i < this.inventory.getContents().length; i++) {
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name("&c未解锁的槽位").durability(14).lore("", "&7前面的区域, 以后再来探索吧!").internalName("not_unlock_slot").build());
        }
        for (int i = 27; i < this.inventory.getContents().length; i++) {
            if (i < limit && inventory.getItem(i) != null && ItemUtil.getInternalName(inventory.getItem(i)) != null && ItemUtil.getInternalName(inventory.getItem(i)).equalsIgnoreCase("not_unlock_slot")) {
                inventory.setItem(i, new ItemBuilder(Material.AIR).build());
            }
        }

        player.openInventory(inventory);
    }

    public String serialize() {
        int limit = 6 * 9;

        ItemStack[] itemStacks = new ItemStack[limit];
        for (int i = 0; i < limit; i++) {
            itemStacks[i] = inventory.getItem(i);
        }

        return InventoryUtil.itemsToString(itemStacks);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerEnderChest)) {
            return false;
        }
        final PlayerEnderChest other = (PlayerEnderChest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$inventory = this.getInventory();
        final Object other$inventory = other.getInventory();
        return Objects.equals(this$inventory, other$inventory);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerEnderChest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $inventory = this.getInventory();
        result = result * PRIME + ($inventory == null ? 43 : $inventory.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PlayerEnderChest(inventory=" + this.getInventory() + ")";
    }

}

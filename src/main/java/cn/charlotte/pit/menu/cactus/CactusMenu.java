package cn.charlotte.pit.menu.cactus;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Araykal
 * @since 2025/1/16
 */
public class CactusMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "哲学仙人掌 (选择其一)";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            button.put(i, getPantsButton());
        }
        return button;
    }

    public Button getPantsButton() {
        return new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ThePit.getApi().generateItem("Leggings");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                if (InventoryUtil.isInvFull(player)) {
                    player.sendMessage(CC.translate("&c你的背包已满!"));
                    return;
                }
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR || !"cactus".equalsIgnoreCase(ItemUtil.getInternalName(player.getItemInHand()) == null ? "" : ItemUtil.getInternalName(player.getItemInHand()))) {
                    player.sendMessage(CC.translate("&c请手持哲学仙人掌后重试!"));
                    return;
                }
                if (currentItem == null) return;


                PlayerUtil.takeOneItemInHand(player);
                player.getInventory().addItem(currentItem);
                player.closeInventory();
                player.sendMessage(CC.translate("&a&l奖励已领取! " + currentItem.getItemMeta().getDisplayName()));
            }
        };
    }
}

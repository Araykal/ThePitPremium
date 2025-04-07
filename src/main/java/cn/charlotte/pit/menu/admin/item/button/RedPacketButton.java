package cn.charlotte.pit.menu.admin.item.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 22:38
 */
public class RedPacketButton extends Button {
    private int money;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE_BLOCK)
                .name("&c红包")
                .lore("", "&7里面会有什么呢", "")
                .internalName("red_packet")
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        ThePit.getInstance()
                .getSignGui()
                .open(player, new String[]{"", "~~~~~~~~~~~~~", "请在此输入", "红包金额"}, (receiver, lines) -> {
                    player.closeInventory();
                    int money;
                    try {
                        money = Integer.parseInt(lines[0]);
                    } catch (Exception ignore) {
                        return;
                    }
                    if (money <= 0) {
                        return;
                    }

                    player.getInventory().addItem(new ItemBuilder(this.getButtonItem(player))
                            .changeNbt("money", money)
                            .changeNbt("sender", player.getUniqueId().toString())
                            .build());

                });
    }
}

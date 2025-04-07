package cn.charlotte.pit.menu.admin.item;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.menu.admin.item.button.EnchantButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 22:48
 */
public class AdminEnchantMenu extends Menu {
    private int page = 0;

    public AdminEnchantMenu() {
    }

    @Override
    public String getTitle(Player player) {
        return "附魔";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        List<AbstractEnchantment> enchantments = ThePit.getInstance().getEnchantmentFactor().getEnchantments();

        int num = 45 * page;

        int slot = 0;
        for (int i = 0; i < 45; i++) {
            if (enchantments.size() <= i + num) {
                break;
            }
            buttonMap.put(slot, new EnchantButton(enchantments.get(i + num)));
            slot++;
        }

        buttonMap.put(45, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("&f上一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                if (page == 0) {
                    return;
                }
                page--;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        buttonMap.put(53, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("&f下一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                page++;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        return buttonMap;
    }
}

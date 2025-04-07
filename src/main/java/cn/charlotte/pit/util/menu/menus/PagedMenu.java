package cn.charlotte.pit.util.menu.menus;

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
 * @Author: Misoryan
 * @Created_In: 2021/4/6 16:47
 */
public class PagedMenu extends Menu {

    private final String title;
    private final List<Button> buttons;
    private final Map<Integer, Button> extraButtons;
    private final int[] displaySlots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    private int page = 0;

    public PagedMenu(String title, List<Button> buttons) {
        this.title = title;
        this.buttons = buttons;
        this.extraButtons = new HashMap<>();
    }

    public PagedMenu(String title, List<Button> buttons, Map<Integer, Button> extraButtons) {
        this.title = title;
        this.buttons = buttons;
        this.extraButtons = extraButtons;
    }


    @Override
    public String getTitle(Player player) {
        String title = this.title;
        if (buttons.size() > 28) {
            title = "(" + (page + 1) + "/" + (int) (Math.floor(buttons.size() / 28) + 1) + ") " + title;
        }
        if (title.length() > 28) {
            title = title.substring(0, 27) + "...";
        }
        return title;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> button = new HashMap<>();
        int num = 28 * page;

        int slot = 0;

        for (int i = 0; i < 28; i++) {
            if (buttons.size() > num + i) {
                button.put(displaySlots[slot], buttons.get(num + i));
            }
            slot++;
        }

        if (page > 0) {
            button.put(45, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.ARROW)
                            .name("&a上一页")
                            .lore("&e页码 " + page)
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
        }

        if (buttons.size() > (num + 28)) {
            button.put(53, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.ARROW)
                            .name("&a下一页")
                            .lore("&e页码 " + (page + 2))
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
        }

        extraButtons.keySet().forEach(extra -> button.put(extra, extraButtons.get(extra)));

        return button;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }
}

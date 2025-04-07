package cn.charlotte.pit.menu.cdk.view;

import cn.charlotte.pit.data.CDKData;
import cn.charlotte.pit.menu.cdk.view.button.CDKButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/27 19:32
 */
public class CDKViewMenu extends Menu {
    private int total;
    private int current;

    @Override
    public String getTitle(Player player) {
        Map<String, CDKData> dataMap = new HashMap<>(CDKData.getCachedCDK());
        this.total = dataMap.size() / 45;

        return "CDK View (" + current + "/" + total + " Page)";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<String, CDKData> dataMap = new HashMap<>(CDKData.getCachedCDK());
        List<CDKData> dataList = new ArrayList<>(dataMap.values());

        Map<Integer, Button> map = new HashMap<>();
        final int size = dataList.size();
        for (int i = 0; i < 45; i++) {
            if (i + current * 45 >= size) {
                break;
            }
            final CDKData data = dataList.get(i + current * 45);
            map.put(i, new CDKButton(data));
        }

        map.put(48, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("&f上一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                current--;
                if (current < 0) {
                    current = 0;
                }
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        map.put(50, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("&f下一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                current++;
                if (current >= total) {
                    current = total;
                }
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        return map;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}

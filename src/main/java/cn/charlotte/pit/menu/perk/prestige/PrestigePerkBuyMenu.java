package cn.charlotte.pit.menu.perk.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.GlassPaneButton;
import cn.charlotte.pit.menu.perk.prestige.button.PrestigePerkBuyButton;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/4 18:37
 */
public class PrestigePerkBuyMenu extends Menu {
    private int page = 0;

    public PrestigePerkBuyMenu() {
    }


    @Override
    public String getTitle(Player player) {
        return "精通天赋 (第" + (page + 1) + "页)";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();

        List<AbstractPerk> perks = ThePit.getInstance()
                .getPerkFactory()
                .getPerks()
                .stream()
                .sorted(Comparator.comparing(perk -> perk.requirePrestige() * 1000 + perk.requireLevel()))
                .collect(Collectors.toList());

        //如果是普通天赋 则移除
        perks.removeIf(perk -> perk.requirePrestige() == 0);

        perks.removeIf(perk -> perk.getPerkType() != PerkType.PERK);


        int num = 36 * page;

        int slot = 0;

        for (int i = 0; i < 36; i++) {
            if (perks.size() > i + num) {
                button.put(slot, new PrestigePerkBuyButton(perks.get(page * 36 + i)));
            }
            slot++;
        }

        for (int i = 0; i < 9; i++) {
            button.put(36 + i, new GlassPaneButton());
        }

        if (page > 0) {
            button.put(45, new Button() {
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
        }

        if (perks.size() > 36 * (page + 1)) {
            button.put(53, new Button() {
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
        }


        return button;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}

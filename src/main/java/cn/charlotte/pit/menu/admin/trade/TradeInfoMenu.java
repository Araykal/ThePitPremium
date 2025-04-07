package cn.charlotte.pit.menu.admin.trade;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.menu.GlassPaneButton;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.BackButton;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/31 23:32
 */
public class TradeInfoMenu extends Menu {
    private static final int[] myselfSlots = {0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30};
    private static final int[] targetSlots = {5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35};
    private static final int[] glassSlots = {4, 13, 22, 31, 40};

    private final PlayerProfile profile;
    private final TradeData data;
    private final TradeTrackerMenu menu;

    public TradeInfoMenu(PlayerProfile profile, TradeData data, TradeTrackerMenu menu) {
        this.profile = profile;
        this.data = data;
        this.menu = menu;
    }


    @Override
    public String getTitle(Player player) {
        return data.getPlayerAName() + " 与 " + data.getPlayerBName() + " 的交易回放";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        for (int glassSlot : glassSlots) {
            buttonMap.put(glassSlot, new GlassPaneButton());
        }

        final BackButton backButton = new BackButton(menu);
        for (int i = 45; i < 54; i++) {
            buttonMap.put(i, backButton);
        }

        if (data.getAPaidCoin() > 0) {
            buttonMap.put(0, new DisplayButton(new ItemBuilder(Material.GOLD_NUGGET).name("&6" + data.getAPaidCoin()).shiny().build(), true));

            for (int i = 1; i < myselfSlots.length; i++) {
                try {
                    ItemStack item = data.getAPaidItem().getContents()[i];
                    if (item != null) {
                        buttonMap.put(myselfSlots[i], new DisplayButton(item, false));
                    }
                } catch (Exception e) {
                    CC.printError(player, e);
                }
            }
        } else {
            for (int i = 0; i < myselfSlots.length; i++) {
                try {
                    ItemStack item = data.getAPaidItem().getContents()[i];
                    if (item != null) {
                        buttonMap.put(myselfSlots[i], new DisplayButton(item, false));
                    }
                } catch (Exception e) {
                    CC.printError(player, e);
                }
            }
        }

        if (data.getBPaidCoin() > 0) {
            buttonMap.put(5, new DisplayButton(new ItemBuilder(Material.GOLD_NUGGET).name("&6" + data.getBPaidCoin()).shiny().build(), false));

            for (int i = 1; i < targetSlots.length; i++) {
                ItemStack item = data.getBPaidItem().getContents()[i];
                if (item != null) {
                    buttonMap.put(targetSlots[i], new DisplayButton(item, false));
                }
            }
        } else {
            for (int i = 0; i < targetSlots.length; i++) {
                ItemStack item = data.getBPaidItem().getContents()[i];
                if (item != null) {
                    buttonMap.put(targetSlots[i], new DisplayButton(item, false));
                }
            }
        }

        return buttonMap;
    }
}

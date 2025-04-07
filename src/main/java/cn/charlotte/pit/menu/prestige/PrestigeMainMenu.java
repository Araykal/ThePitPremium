package cn.charlotte.pit.menu.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.prestige.button.PrestigeShopButton;
import cn.charlotte.pit.menu.prestige.button.PrestigeStatusButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/4 19:48
 */
public class PrestigeMainMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "精通";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        button.put(11, new PrestigeShopButton());
        if (!ThePit.isDEBUG_SERVER()) {
            button.put(15, new PrestigeStatusButton());
        }
        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}

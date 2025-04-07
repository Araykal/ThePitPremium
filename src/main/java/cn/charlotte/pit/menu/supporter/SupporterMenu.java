package cn.charlotte.pit.menu.supporter;

import cn.charlotte.pit.menu.option.button.SupportStarOptionButton;
import cn.charlotte.pit.menu.workshop.button.WorkshopEntranceButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Creator Misoryan
 * @Date 2021/5/4 10:16
 */
public class SupporterMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "天坑乱斗会员";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        button.put(11, new SupportStarOptionButton());
        button.put(15, new WorkshopEntranceButton());
        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}

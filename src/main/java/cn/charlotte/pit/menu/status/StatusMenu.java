package cn.charlotte.pit.menu.status;

import cn.charlotte.pit.menu.status.button.DefensiveStatusButton;
import cn.charlotte.pit.menu.status.button.OffensiveStatusButton;
import cn.charlotte.pit.menu.status.button.PerformanceStatusButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/3 17:11
 */
public class StatusMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "统计信息";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        button.put(11, new OffensiveStatusButton());
        button.put(13, new DefensiveStatusButton());
        button.put(15, new PerformanceStatusButton());
        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}

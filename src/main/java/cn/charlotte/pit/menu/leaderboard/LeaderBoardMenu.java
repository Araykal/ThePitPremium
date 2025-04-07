package cn.charlotte.pit.menu.leaderboard;

import cn.charlotte.pit.menu.leaderboard.button.LeaderEventsButton;
import cn.charlotte.pit.menu.leaderboard.button.LeaderLevelButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Starry_Killer
 * @Date: 2024/4/19
 */
public class LeaderBoardMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "排行榜小帮手";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        button.put(11, new LeaderLevelButton());
        button.put(15, new LeaderEventsButton());

        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

}

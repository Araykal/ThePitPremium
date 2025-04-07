package cn.charlotte.pit.menu.event.previewer;

import cn.charlotte.pit.menu.event.previewer.button.EventPreviewButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Creator Misoryan
 * @Date 2021/5/5 12:13
 */
public class EventPreviewerMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "天坑乱斗会员: /events";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (int i = 0; i < 18; i++) {
            buttons.put(i, new EventPreviewButton(i, false));
        }
        for (int i = 0; i < 9; i++) {
            buttons.put(18 + i, new EventPreviewButton(i, true));
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}

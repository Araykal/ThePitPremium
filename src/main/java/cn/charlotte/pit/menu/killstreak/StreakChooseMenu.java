package cn.charlotte.pit.menu.killstreak;

import cn.charlotte.pit.menu.perk.normal.choose.button.PerkButton;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/20 20:40
 */
public class StreakChooseMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "连杀天赋";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        if (PlayerUtil.isPlayerUnlockedPerk(player, "extra_kill_streak_slot_perk")) {
            for (int i = 0; i < 4; i++) {
                button.put(10 + 2 * i, new PerkButton(5 + i));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                button.put(11 + 2 * i, new PerkButton(5 + i));
            }
        }
        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}

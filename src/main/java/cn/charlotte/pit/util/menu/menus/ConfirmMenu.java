package cn.charlotte.pit.util.menu.menus;

import cn.charlotte.pit.util.callback.Callback;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.ConfirmationButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfirmMenu extends Menu {

    private final String title;
    private final Callback<Boolean> response;
    private final boolean closeAfterResponse;
    private final Button[] centerButtons;
    private final int delaySec;
    private final Button yes;
    private final Button no;

    public ConfirmMenu(String title, Callback<Boolean> response, boolean closeAfter, int delaySec, Button... centerButtons) {
        this.title = title;
        this.response = response;
        this.closeAfterResponse = closeAfter;
        this.centerButtons = centerButtons;
        this.delaySec = delaySec;
        this.yes = new ConfirmationButton(true, response, closeAfterResponse, delaySec);
        this.no = new ConfirmationButton(false, response, closeAfterResponse, delaySec);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, yes);
        buttons.put(15, no);

        if (centerButtons != null) {
            for (int i = 0; i < centerButtons.length; i++) {
                if (centerButtons[i] != null) {
                    buttons.put(getSlot(4, i), centerButtons[i]);
                }
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public String getTitle(Player player) {
        return title;
    }


}

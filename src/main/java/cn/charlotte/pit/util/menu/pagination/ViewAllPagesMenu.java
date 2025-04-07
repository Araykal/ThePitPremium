package cn.charlotte.pit.util.menu.pagination;

import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.BackButton;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu {

    @NonNull
    PaginatedMenu menu;

    public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
        this.menu = menu;
    }

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(menu));

        int index = 10;

        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public @NonNull PaginatedMenu getMenu() {
        return this.menu;
    }
}

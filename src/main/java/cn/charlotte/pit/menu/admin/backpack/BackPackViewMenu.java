package cn.charlotte.pit.menu.admin.backpack;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/4 20:43
 */
public class BackPackViewMenu extends Menu {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final PlayerProfile profile;
    private final int page = 0;

    public BackPackViewMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return profile.getPlayerName() + " 的背包备份 (" + (page + 1) + ")";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return null;
    }
}

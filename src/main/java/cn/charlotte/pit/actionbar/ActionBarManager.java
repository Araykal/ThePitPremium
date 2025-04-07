package cn.charlotte.pit.actionbar;

import cn.charlotte.pit.util.chat.ActionBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ActionBarManager implements IActionBarManager{
    Map<UUID, String> multiMap = new ConcurrentHashMap<>();

    public String addToQueue(Player player, String arg) {
        UUID uniqueId = player.getUniqueId();
        multiMap.put(uniqueId, arg);
        return arg;
    }

    public void tick() {
        Iterator<Map.Entry<UUID, String>> iterator = multiMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, String> next = iterator.next();
            String value = next.getValue();
            UUID key = next.getKey();

            Player player = Bukkit.getPlayer(key);
            if(value != null && key != null && player != null) {
                try {
                    ActionBarUtil.sendActionBar0(player, value);
                } catch(Exception ignored) {
                }
            }
            iterator.remove();

        }
    }
}

package cn.charlotte.pit.menu.trade;

import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 21:46
 */
@AutoRegister
public class TradeListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogout(PlayerQuitEvent event) {
        if (TradeManager.trades.containsKey(event.getPlayer().getUniqueId())) {
            TradeManager.trades.get(event.getPlayer().getUniqueId()).onPlayerCancel(event.getPlayer());
        }
    }
}

package cn.charlotte.pit.listener;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.chat.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoRegister
public class SafetyJoinListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (ThePit.getApi() == null || !ThePit.getApi().isLoaded()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate("&c天坑仍然在启动中...如长时间提示该消息则无法连接至天坑验证服务器"));
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (ThePit.getApi() == null || !ThePit.getApi().isLoaded()) {
          event.getPlayer().kickPlayer(CC.translate("&c天坑仍然在启动中...如长时间提示该消息则无法连接至天坑验证服务器"));
        }
    }
}

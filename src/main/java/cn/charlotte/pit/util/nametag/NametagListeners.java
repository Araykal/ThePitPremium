package cn.charlotte.pit.util.nametag;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class NametagListeners implements Listener {

    private NametagHandler handler;

    /**
     * Nametag Listeners.
     *
     * @param handler instance.
     */
    public NametagListeners(NametagHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getHandler().getBoards().putIfAbsent(event.getPlayer().getUniqueId(), new NametagBoard(event.getPlayer(), getHandler()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        NametagBoard board = getHandler().getBoards().get(event.getPlayer().getUniqueId());

        if (board == null) {
            return;
        }

        board.cleanup();
        getHandler().getBoards().remove(event.getPlayer().getUniqueId());
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

}

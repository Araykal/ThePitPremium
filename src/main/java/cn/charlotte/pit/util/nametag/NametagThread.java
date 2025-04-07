package cn.charlotte.pit.util.nametag;

import cn.charlotte.pit.ThePit;
import org.bukkit.entity.Player;

public class NametagThread extends Thread {

    private NametagHandler handler;

    /**
     * Nametag Thread.
     *
     * @param handler instance.
     */
    public NametagThread(NametagHandler handler) {
        this.handler = handler;
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!ThePit.getInstance().isEnabled()) {
                    return;
                }
                tick();
                sleep(50 * handler.getTicks());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread Tick Logic.
     */
    private void tick() {
        if (this.handler.getAdapter() == null) {
            return;
        }

        for (Player player : this.handler.getPlugin().getServer().getOnlinePlayers()) {
            NametagBoard board = this.handler.getBoards().get(player.getUniqueId());

            // This shouldn't happen, but just in case.
            if (board != null) {
                board.update();
            }
        }
    }
}

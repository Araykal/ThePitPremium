package cn.charlotte.pit.util.scoreboard.events;

import cn.charlotte.pit.util.scoreboard.AssembleBoard;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardCreatedEvent extends Event {

    public static HandlerList handlerList = new HandlerList();
    private final AssembleBoard board;
    private boolean cancelled = false;

    /**
     * Assemble Board Created Event.
     *
     * @param board of player.
     */
    public AssembleBoardCreatedEvent(AssembleBoard board) {
        this.board = board;
    }

    public static HandlerList getHandlerList() {
        return AssembleBoardCreatedEvent.handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public AssembleBoard getBoard() {
        return this.board;
    }
}

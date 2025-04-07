package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 1:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PitEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public boolean callEvent() {
        Bukkit.getPluginManager()
                .callEvent(this);
        return false;
    }
}

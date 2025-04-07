package spg.lgdev;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import spg.lgdev.handler.MovementHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EmptyIrony on 2021/6/20.
 */
public class iSpigot implements Listener {
    public static iSpigot INSTANCE;
    private final List<MovementHandler> movementHandlers = new ArrayList<>();
    public iSpigot() {
        INSTANCE = this;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        for (MovementHandler move : movementHandlers) {
            if (from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) {
                move.handleUpdateRotation(event.getPlayer(), from, to, null);
            }
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                move.handleUpdateLocation(event.getPlayer(), from, to, null);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        for (MovementHandler move : movementHandlers) {
            if (from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) {
                move.handleUpdateRotation(event.getPlayer(), from, to, null);
            }
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                move.handleUpdateLocation(event.getPlayer(), from, to, null);
            }
        }
    }

    public void addMovementHandler(MovementHandler var1) {
        this.movementHandlers.add(var1);
    }


}

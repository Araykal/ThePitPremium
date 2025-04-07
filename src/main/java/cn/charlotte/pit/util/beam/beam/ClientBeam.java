/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Jaxon A Brown
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.charlotte.pit.util.beam.beam;

import cn.charlotte.pit.ThePit;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Creates a guardian beam between two locations.
 * This uses ProtocolLib to send two entities: A guardian and a squid.
 * The guardian is then set to target the squid.
 *
 * @author Jaxon A Brown
 */
public class ClientBeam {
    private final UUID worldUID;
    private final double viewingRadiusSquared;
    private final long updateDelay;
    private final LocationTargetBeam beam;
    private boolean isActive;
    private Location startingPosition, endingPosition;
    private Player player;
    private boolean isViewing;

    private BukkitRunnable runnable;

    /**
     * Create a guardian beam for a specific player. This sets up the packets.
     *
     * @param player           Player who will see the beam.
     * @param startingPosition Position to start the beam, or the position which the effect 'moves towards'.
     * @param endingPosition   Position to stop the beam, or the position which the effect 'moves away from'.
     */
    public ClientBeam(Player player, Location startingPosition, Location endingPosition) {
        this(player, startingPosition, endingPosition, 100D, 5);
    }

    /**
     * Create a guardian beam for a specific player. This sets up the packets.
     *
     * @param player           Player who will see the beam.
     * @param startingPosition Position to start the beam, or the position which the effect 'moves towards'.
     * @param endingPosition   Position to stop the beam, or the position which the effect 'moves away from'.
     * @param viewingRadius    Radius from either node of the beam from which it can be seen.
     * @param updateDelay      Delay between checking if the beam should be hidden or shown to the player.
     */
    public ClientBeam(Player player, Location startingPosition, Location endingPosition, double viewingRadius, long updateDelay) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "The player must be online");
        Preconditions.checkNotNull(startingPosition, "startingPosition cannot be null");
        Preconditions.checkNotNull(endingPosition, "endingPosition cannot be null");
        Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()), "startingPosition and endingPosition must be in the same world");
        Preconditions.checkArgument(viewingRadius > 0, "viewingRadius must be positive");
        Preconditions.checkArgument(updateDelay >= 1, "viewingRadius must be a natural number");

        this.worldUID = startingPosition.getWorld().getUID();
        this.viewingRadiusSquared = viewingRadius * viewingRadius;
        this.updateDelay = updateDelay;

        this.isActive = false;
        this.beam = new LocationTargetBeam(startingPosition, endingPosition);
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;

        this.player = player;
        this.isViewing = false;
    }

    /**
     * Send the packets to create the beam to the player, if applicable.
     * This also starts the runnable which will make the effect visible if it becomes applicable later.
     */
    public void start() {
        Preconditions.checkState(!this.isActive, "The beam must be disabled in order to start it");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");

        this.isActive = true;
        (this.runnable = new ClientBeamUpdater()).runTaskTimer(ThePit.getInstance(), 0, this.updateDelay);
    }

    /**
     * Send the packets to remove the beam from the player, if applicable.
     * This also stops the runnable.
     */
    public void stop() {
        Preconditions.checkState(this.isActive, "The beam must be enabled in order to stop it");

        this.isActive = false;
        this.isViewing = false;
        if (this.player != null && !this.player.isOnline()) {
            this.player = null;
        }
        this.runnable.cancel();
        this.runnable = null;
    }

    /**
     * Sets the starting position of the beam, or the position which the effect 'moves towards'.
     *
     * @param location the starting position.
     */
    public void setStartingPosition(Location location) {
        Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");

        this.startingPosition = location;
        this.beam.setStartingPosition(this.player, location);
    }

    /**
     * Sets the ending position of the beam, or the position which the effect 'moves away from'.
     *
     * @param location the ending position.
     */
    public void setEndingPosition(Location location) {
        Preconditions.checkArgument(location.getWorld().getUID().equals(this.worldUID), "location must be in the same world as this beam");
        Preconditions.checkState(this.player != null && !this.player.isOnline(), "The player must be online");

        this.endingPosition = location;
        this.beam.setEndingPosition(this.player, location);
    }

    /**
     * Checks if any packets need to be sent to show or hide the beam. Stops the beam if the player is offline.
     */
    public void update() {
        if (this.player == null || !this.player.isOnline()) {
            stop();
        }
        if (this.isActive) {
            if (!this.player.getWorld().getUID().equals(this.worldUID)) {
                stop();
            }

            if (isCloseEnough(player.getLocation())) {
                if (!this.isViewing) {
                    this.beam.start(player);
                    this.isViewing = true;
                }
            } else if (this.isViewing) {
                this.beam.cleanup(player);
                this.isViewing = false;
            }
        }
    }

    /**
     * Checks if the beam is active (will show when applicable).
     *
     * @return True if active.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Checks if the player is currently viewing the beam (can the player see it).
     *
     * @return True if viewing.
     */
    public boolean isViewing() {
        return this.isViewing;
    }

    private boolean isCloseEnough(Location location) {
        return startingPosition.distanceSquared(location) <= viewingRadiusSquared ||
                endingPosition.distanceSquared(location) <= viewingRadiusSquared;
    }

    private class ClientBeamUpdater extends BukkitRunnable {
        @Override
        public void run() {
            ClientBeam.this.update();
        }
    }
}

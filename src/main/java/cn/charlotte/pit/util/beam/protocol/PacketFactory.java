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
package cn.charlotte.pit.util.beam.protocol;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;

import static com.comphenix.protocol.PacketType.Play.Server.*;

/**
 * The PacketFactory generates and modifies the packets for the library to use.
 * If you are looking into making your own Guardian Beam type, this class is for you.
 *
 * @author Jaxon A Brown
 */
public class PacketFactory {
    /**
     * Creates a packet to spawn a squid at the location.
     *
     * @param location location to spawn the squid.
     * @return beam packet used to spawn for players.
     */
    public static WrappedBeamPacket createPacketSquidSpawn(Location location) {

        PacketContainer container = new PacketContainer(SPAWN_ENTITY_LIVING);
        container.getIntegers().write(0, EIDGen.generateEID());
        container.getIntegers().write(1, 94);
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 32);
        container.getDataWatcherModifier().write(0, watcher);
        return new WrappedBeamPacket(container);
    }

    /**
     * Creates a packet to spawn a guardian at the location.
     *
     * @param location    location to spawn the guardian.
     * @param squidPacket squid the guardian will target.
     * @return beam packet used to spawn for players.
     */
    public static WrappedBeamPacket createPacketGuardianSpawn(Location location, WrappedBeamPacket squidPacket) {
        PacketContainer container = new PacketContainer(SPAWN_ENTITY_LIVING);
        container.getIntegers().write(0, EIDGen.generateEID());
        container.getIntegers().write(1, 68);
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 32);
        watcher.setObject(16, 0);
        watcher.setObject(17, squidPacket.getHandle().getIntegers().read(0));
        container.getDataWatcherModifier().write(0, watcher);
        return new WrappedBeamPacket(container);
    }

    /**
     * Modifies location information of the given Spawn Packet.
     *
     * @param entitySpawnPacket SquidSpawn or GuardianSpawn packet for the entity.
     * @param location          location the entity should be spawned at.
     * @return beam packet used to spawn for players.
     */
    public static WrappedBeamPacket modifyPacketEntitySpawn(WrappedBeamPacket entitySpawnPacket, Location location) {
        PacketContainer container = entitySpawnPacket.getHandle();
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        return entitySpawnPacket;
    }

    /**
     * Creates a packet to move an entity. Doesn't include where to move it to.
     *
     * @param entityPacket SquidSpawn or GuardianSpawn packet for the entity.
     * @return Skeleton packet for the given entity.
     */
    public static WrappedBeamPacket createPacketEntityMove(WrappedBeamPacket entityPacket) {
        PacketContainer container = new PacketContainer(ENTITY_TELEPORT);
        container.getIntegers().write(0, entityPacket.getHandle().getIntegers().read(0));
        return new WrappedBeamPacket(container);
    }

    /**
     * Adds location information to a packet to move an entity.
     *
     * @param entityMovePacket EntityMove packet to add location information to.
     * @param location         location to move the entity to.
     * @return Finished packet to teleport the given entity.
     */
    public static WrappedBeamPacket modifyPacketEntityMove(WrappedBeamPacket entityMovePacket, Location location) {
        PacketContainer container = entityMovePacket.getHandle();
        container.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
        container.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0D));
        container.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        return entityMovePacket;
    }

    /**
     * Creates a packet to remove the guardian and squid entities.
     *
     * @param squidPacket    SquidSpawn of the entity to remove
     * @param guardianPacket GuardianSpawn of the entity to remove
     * @return Packet to remove the guardian and squid when sent to a player.
     */
    public static WrappedBeamPacket createPacketRemoveEntities(WrappedBeamPacket squidPacket, WrappedBeamPacket guardianPacket) {
        PacketContainer container = new PacketContainer(ENTITY_DESTROY);
        container.getIntegerArrays().write(0, new int[]{
                squidPacket.getHandle().getIntegers().read(0),
                guardianPacket.getHandle().getIntegers().read(0)
        });
        return new WrappedBeamPacket(container);
    }
}

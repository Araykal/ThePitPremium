package spg.lgdev.handler;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by EmptyIrony on 2021/6/20.
 */
public interface MovementHandler {

    void handleUpdateLocation(Player var1, Location var2, Location var3, PacketPlayInFlying var4);

    void handleUpdateRotation(Player var1, Location var2, Location var3, PacketPlayInFlying var4);

}

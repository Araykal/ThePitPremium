package cn.charlotte.pit.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:01
 */
public abstract class AbstractHologram {

    public abstract String getInternalName();

    public abstract List<String> getText(Player player);

    public abstract boolean shouldLoop();

    public abstract int loopTicks();

    public abstract double getHologramHighInterval();

    public abstract Location getLocation();
}

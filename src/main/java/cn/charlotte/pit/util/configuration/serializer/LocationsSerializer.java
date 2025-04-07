package cn.charlotte.pit.util.configuration.serializer;

import cn.charlotte.pit.util.configuration.AbstractSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 22:06
 */
public class LocationsSerializer extends AbstractSerializer<List<Location>> {
    @Override
    public String toString(List<Location> var1) {
        StringBuilder builder = new StringBuilder();
        for (Location location : var1) {
            builder.append(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ() + "|" + location.getYaw() + "|" + location.getPitch())
                    .append(";");
        }
        builder.substring(0, builder.length() - 1);
        return builder.toString();
    }

    @Override
    public List<Location> fromString(String var1) {
        String[] strings = var1.split(";");
        List<Location> locations = new ArrayList<>();

        for (String string : strings) {
            String[] parts = string.split("\\|");
            if (parts.length != 6) {
                continue;
            }
            Location location = new Location(Bukkit.getWorld(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
            locations.add(location);
        }

        return locations;
    }
}

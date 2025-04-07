package cn.charlotte.pit.util;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/22 11:56
 * 4
 */
public class VectorUtil {
    private static final Random random = new Random();

    public static Item itemDrop(Player player, ItemStack itemStack) {
        return itemDrop(player, itemStack, 0.0, 0.4);
    }

    public static Item itemDrop(Player player, ItemStack itemStack, double bulletSpread, double radius) {
        Location location = player.getLocation().add(0.0D, 1.5D, 0.0D);
        Item item = player.getWorld().dropItem(location, itemStack);
        double yaw = Math.toRadians(-player.getLocation().getYaw() - 90.0F);
        double pitch = Math.toRadians(-player.getLocation().getPitch());
        double x;
        double y;
        double z;
        if (bulletSpread > 0.0D) {
            double[] spread = new double[]{1.0D, 1.0D, 1.0D};

            IntStream.range(0, 3).forEach((t) -> spread[t] = (random.nextDouble() - random.nextDouble()) * bulletSpread * 0.1D);
            x = Math.cos(pitch) * Math.cos(yaw) + spread[0];
            y = Math.sin(pitch) + spread[1];
            z = -Math.sin(yaw) * Math.cos(pitch) + spread[2];
        } else {
            x = Math.cos(pitch) * Math.cos(yaw);
            y = Math.sin(pitch);
            z = -Math.sin(yaw) * Math.cos(pitch);
        }

        Vector dirVel = new Vector(x, y, z);
        item.setVelocity(dirVel.normalize().multiply(radius));
        return item;
    }

    public static void entityPush(Entity entity, Location to, double velocity) {
        Location from = entity.getLocation();
        Vector vector = getPushVector(from, to, velocity);
        if (vector.getX() != 0 && vector.getY() != 0 && vector.getZ() != 0) {
            entity.setVelocity(vector);
        }
    }

    public static Vector getPushVector(Location from, Location to, double velocity) {
        Vector test = to.clone().subtract(from).toVector();
        double elevation = test.getY();
        Double launchAngle = calculateLaunchAngle(from, to, velocity, elevation);
        double distance = Math.sqrt(Math.pow(test.getX(), 2.0D) + Math.pow(test.getZ(), 2.0D));
        if (distance != 0.0D) {
            if (launchAngle == null) {
                launchAngle = Math.atan((40.0D * elevation + Math.pow(velocity, 2.0D)) / (40.0D * elevation + 2.0D * Math.pow(velocity, 2.0D)));
            }
            double hangTime = calculateHangTime(launchAngle, velocity, elevation);
            test.setY(Math.tan(launchAngle) * distance);
            test = normalizeVector(test);
            Vector noise = Vector.getRandom();
            noise = noise.multiply(0.1D);
            test.add(noise);
            velocity = velocity + 1.188D * Math.pow(hangTime, 2.0D) + (random.nextDouble() - 0.8D) / 2.0D;
            test = test.multiply(velocity / 20.0D);
            return test;
        }
        return new Vector(0, 0, 0);
    }

    private static double calculateHangTime(double launchAngle, double v, double elev) {
        double a = v * Math.sin(launchAngle);
        double b = -2.0D * 20.0 * elev;
        return Math.pow(a, 2.0D) + b < 0.0D ? 0.0D : (a + Math.sqrt(Math.pow(a, 2.0D) + b)) / 20.0;
    }

    private static Vector normalizeVector(Vector victor) {
        double mag = Math.sqrt(Math.pow(victor.getX(), 2.0D) + Math.pow(victor.getY(), 2.0D) + Math.pow(victor.getZ(), 2.0D));
        return mag != 0.0D ? victor.multiply(1.0D / mag) : victor.multiply(0);
    }

    private static Double calculateLaunchAngle(Location from, Location to, double v, double elevation) {
        Vector vector = from.clone().subtract(to).toVector();
        double distance = Math.sqrt(Math.pow(vector.getX(), 2.0D) + Math.pow(vector.getZ(), 2.0D));
        double v2 = Math.pow(v, 2.0D);
        double v4 = Math.pow(v, 4.0D);
        double check = 20.0 * (20.0 * Math.pow(distance, 2.0D) + 2.0D * elevation * v2);
        return v4 < check ? null : Math.atan((v2 - Math.sqrt(v4 - check)) / (20.0 * distance));
    }
}


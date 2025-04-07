package cn.charlotte.pit.util.random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:47
 * 4
 */
public class RandomUtil {
    public static final Random random;

    static {
        random = new Random();
    }

    /**
     * 参数范围为0-1
     *
     * @param chance 百分数概率，大于等于1永远返回true
     * @return 是否成功
     */
    public static boolean hasSuccessfullyByChance(double chance) {
        if (chance >= 1) {
            return true;
        }
        if (chance <= 0) {
            return false;
        }
        double i = (random.nextInt(1000000000) / 1000000000D);

        return i <= chance;
    }


    public static Object helpMeToChooseOne(Object... entry) {
        return entry[random.nextInt(entry.length)];
    }

    public static Location generateRandomLocation() {
        int x = RandomUtil.random.nextInt(180) - 90;
        int z = RandomUtil.random.nextInt(180) - 90;
        World world = Bukkit.getWorlds().get(0);
        return world.getHighestBlockAt(x, z).getLocation().clone().add(0, 1, 0);
    }
}

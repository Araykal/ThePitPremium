package cn.charlotte.pit.runnable;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import real.nanoneko.register.IMagicLicense;
import sinc.Native;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Araykal
 * @since 2025/1/25
 */
@Native
public class AntiCollateralRunnable extends BukkitRunnable {

    public static boolean checkAddon = false;

    @Override
    public void run() {
        Predicate<AbstractEnchantment> filterPredicate = createEnchantmentFilter();
        Map<String, AbstractEnchantment> enchantmentMap = ThePit.getInstance().getEnchantmentFactor().getEnchantmentMap();

        List<AbstractEnchantment> filteredEnchantments = enchantmentMap.values().stream()
                .filter(filterPredicate)
                .collect(Collectors.toList());

        filteredEnchantments.forEach(enchantment -> {
            enchantmentMap.remove(enchantment.getNbtName());
            ThePit.getInstance().getEnchantmentFactor().getEnchantments().remove(enchantment);
            logMessage("§c检测到非法附属附魔 " + enchantment.getNbtName() + " 已被移除.");
        });
    }

    private Predicate<AbstractEnchantment> createEnchantmentFilter() {
        return enchantment -> {
            ClassLoader classLoader = enchantment.getClass().getClassLoader();
            String className = classLoader.getClass().getSimpleName();
            String packageName = classLoader.getClass().getPackageName();

            return !(enchantment instanceof IMagicLicense) && !className.equals("MagicLicense") && !packageName.equals("cn.charlotte.pit.license");
        };
    }

    private void shutdownServer(String message) {
        logMessage(message);
        Bukkit.shutdown();
    }

    private void logMessage(String message) {
        ThePit.getInstance().getLogger().info(message);
    }
}

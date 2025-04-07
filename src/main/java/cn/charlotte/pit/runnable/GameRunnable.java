package cn.charlotte.pit.runnable;
import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.data.temp.TradeRequest;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/5 0:30
 */
public class GameRunnable extends BukkitRunnable {
    @Getter
    private final static List<TradeRequest> tradeRequests = new ArrayList<>();


    private long tick = 0;

    @Override
    public void run() {

        final Map<String, ITickTask> ticksPerk = ThePit.getInstance().getPerkFactory().getTickTasks();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

            for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
                final ITickTask task = ticksPerk.get(entry.getValue().getPerkInternalName());
                if (task != null) {
                    if (tick % task.loopTick(entry.getValue().getLevel()) == 0) {
                        task.handle(entry.getValue().getLevel(), player);
                    }
                }
            }

            for (Map.Entry<String, PerkData> entry : profile.getUnlockedPerkMap().entrySet()) {
                final ITickTask task = ticksPerk.get(entry.getValue().getPerkInternalName());
                if (task != null) {
                    if (tick % task.loopTick(entry.getValue().getLevel()) == 0) {
                        task.handle(entry.getValue().getLevel(), player);
                    }
                }
            }

            final Map<String, ITickTask> enchantTicks = ThePit.getInstance().getEnchantmentFactor().getTickTasks();

            //裤子
            final ItemStack leggings = player.getInventory().getLeggings();
            if (leggings != null) {
                final IMythicItem mythicLeggings = Utils.getMythicItem(leggings);
                if (mythicLeggings != null) {
                    for (Map.Entry<AbstractEnchantment, Integer> entry : mythicLeggings.getEnchantments().entrySet()) {
                        final ITickTask task = enchantTicks.get(entry.getKey().getNbtName());
                        if (task == null) {
                            continue;
                        }

                        final Integer level = entry.getValue();
                        if (tick % task.loopTick(level) == 0) {
                            task.handle(level, player);
                        }
                    }
                }
            }

            ItemStack itemInHand = player.getInventory().getItemInHand();
            if (itemInHand != null && itemInHand.getType() != Material.AIR && itemInHand.getType() != Material.LEATHER_LEGGINGS && itemInHand.getType() != Material.PAPER) {
                final IMythicItem mythicHeld = Utils.getMythicItem(itemInHand);
                if (mythicHeld != null) {
                    for (Map.Entry<AbstractEnchantment, Integer> entry : mythicHeld.getEnchantments().entrySet()) {
                        final ITickTask task = enchantTicks.get(entry.getKey().getNbtName());
                        if (task == null) continue;
                        final Integer level = entry.getValue();
                        if (tick % task.loopTick(level) == 0) {
                            task.handle(level, player);
                        }
                    }
                }
            }

            List<TradeRequest> collect = tradeRequests.stream()
                    .filter(tradeRequest -> tradeRequest.getCooldown().hasExpired())
                    .collect(Collectors.toList());

            tradeRequests.removeAll(collect);
        }

        tick++;
    }


    private boolean isNewYear() {
        return java.time.Month.JANUARY.equals(java.time.Month.JANUARY) && java.time.LocalTime.now().equals(java.time.LocalTime.of(12, 0));
    }
}
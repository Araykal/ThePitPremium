package cn.charlotte.pit.game.runnable;

import cn.charlotte.pit.data.temp.TradeRequest;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author: EmptyIrony
 * @Date: 2021/1/5 0:30
 */
public class TradeMonitorRunnable extends BukkitRunnable {
    @Getter
    private final static List<TradeRequest> tradeRequests = new ArrayList<>();

    @Override
    public void run() {
        tradeRequests.removeIf(tradeRequest -> tradeRequest.getCooldown().hasExpired());
    }
}

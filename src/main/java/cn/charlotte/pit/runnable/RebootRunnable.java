package cn.charlotte.pit.runnable;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.TitleUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/24 13:04
 */
@Getter
public class RebootRunnable extends BukkitRunnable {
    private final long serverStartTime;
    private RebootTask currentTask;

    public RebootRunnable() {
        this.serverStartTime = System.currentTimeMillis();
    }

    @Override
    public void run() {

        if (currentTask != null) {
            if (currentTask.endTime <= System.currentTimeMillis()) {
                Bukkit.shutdown();
                return;
            }

            if (currentTask.endTime <= System.currentTimeMillis() + 5 * 1000) {
                final EventFactory factory = ThePit.getInstance().getEventFactory();
                if (factory.getActiveEpicEvent() != null) {
                    CC.boardCast("&4&l注意! &7因存在一个即将执行的重启任务,已&c强制结算&7事件.");
                    factory.inactiveEvent(factory.getActiveEpicEvent());
                }
                if (factory.getActiveNormalEvent() != null) {
                    CC.boardCast("&4&l注意! &7因存在一个即将执行的重启任务,已&c强制结算&7事件.");
                    factory.inactiveEvent(factory.getActiveNormalEvent());
                }

                CC.boardCast("&4&l注意! &7因存在一个即将执行的重启任务,已&c强制结算&7事件.");
            }
        }
    }

    public void addRebootTask(RebootTask task) {
        if (this.currentTask != null) {
            return;
        }
        this.currentTask = task;
        long l = task.endTime - System.currentTimeMillis();
        String time = TimeUtil.millisToRoundedTime(l);
        CC.boardCast("&4&l注意! &7一个计划中的重启即将在 &c" + time + "&7 后执行&8[#" + task.reason + "]");
        for (Player player : Bukkit.getOnlinePlayers()) {
            TitleUtil.sendTitle(player, CC.translate("&c服务器即将在 &b" + time + " &c后重启"), "", 20, 100, 20);
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class RebootTask {
        private final String reason;
        private final long endTime;
    }
}

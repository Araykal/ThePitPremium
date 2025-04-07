package cn.charlotte.pit.util.thread;

import cn.charlotte.pit.ThePit;
import org.bukkit.Bukkit;

public interface ThreadHelper {

    static void Async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(ThePit.getInstance(), runnable);
    }

    static void Sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(ThePit.getInstance(), runnable);
    }

    static void Sync(Runnable runnable, int tick) {
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), runnable, tick);
    }

    default void async(Runnable runnable) {
        ThreadHelper.Async(runnable);
    }

    default void sync(Runnable runnable) {
        ThreadHelper.Sync(runnable);
    }

    default void sync(Runnable runnable, int tick) {
        ThreadHelper.Sync(runnable, tick);
    }
}

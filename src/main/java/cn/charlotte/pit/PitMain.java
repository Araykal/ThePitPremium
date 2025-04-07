package cn.charlotte.pit;

import cn.charlotte.pit.impl.PitInternalImpl;

import org.bukkit.Bukkit;

public class PitMain {
    private static PitHook hook;

    public static void start() {
        Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
            ThePit.getInstance().loadListener();
            ThePit.setApi(PitInternalImpl.INSTANCE);
            hook = PitHook.INSTANCE;
            hook.init();
            PitInternalImpl.INSTANCE.setLoaded(true);
        });
    }

}

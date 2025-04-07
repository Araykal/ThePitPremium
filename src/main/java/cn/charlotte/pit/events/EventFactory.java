package cn.charlotte.pit.events;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Sound.BURP;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/9 12:59
 */
public class EventFactory {
    private final List<INormalEvent> normalEvents;
    private final List<IEpicEvent> epicEvents;



    private INormalEvent activeNormalEvent;
    private IEpicEvent activeEpicEvent;
    private Cooldown normalEnd;

    private long lastNormalEvent;

    private IEpicEvent nextEpicEvent;
    private Cooldown nextEpicEventTimer;

    public EventFactory() {
        this.normalEvents = new ArrayList<>();
        this.epicEvents = new ArrayList<>();
        this.normalEnd = new Cooldown(0);
    }

    public void pushEvent(IEpicEvent event) {
        pushEvent(event, false);
    }

    public void pushEvent(IEpicEvent event, boolean force) {
        if (Bukkit.getOnlinePlayers().size() >= ((IEvent) event).requireOnline()) {
            readyEpicEvent(event);
        }
    }

    public void pushEvent(INormalEvent event) {
        pushEvent(event, false);
    }

    public void pushEvent(INormalEvent event, boolean force) {
        if (Bukkit.getOnlinePlayers().size() >= ((IEvent) event).requireOnline()) {
            activeEvent(event);
        }
    }

    @SneakyThrows
    public void init(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (IEpicEvent.class.isAssignableFrom(clazz)) {
                try {
                    this.epicEvents.add((IEpicEvent) clazz.newInstance());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (INormalEvent.class.isAssignableFrom(clazz)) {
                try {
                    this.normalEvents.add((INormalEvent) clazz.newInstance());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(ThePit.getInstance(), new EventTimer(), 20, 20);

    }

    public String getActiveEpicEventName() {
        if (activeEpicEvent == null) {
            return null;
        }
        return ((IEvent) activeEpicEvent).getEventInternalName();
    }

    public String getActiveNormalEventName() {
        if (activeNormalEvent == null) {
            return null;
        }
        return ((IEvent) activeNormalEvent).getEventInternalName();
    }

    public void activeEvent(INormalEvent event) {
        this.lastNormalEvent = System.currentTimeMillis();
        this.activeNormalEvent = event;
        IEvent iEvent = (IEvent) event;
        this.normalEnd = new Cooldown(5, TimeUnit.MINUTES);
        iEvent.onActive();

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (!event.equals(activeNormalEvent)) {
                    ThePit.getInstance()
                            .getBossBar()
                            .getBossBar()
                            .setTitle("");
                    cancel();
                    return;
                }

                if (normalEnd.hasExpired()) {
                    ThePit.getInstance()
                            .getBossBar()
                            .getBossBar()
                            .setTitle("");
                    cancel();
                    return;
                }

                if (tick >= 15) {
                    tick = 0;
                }
                String start;
                if (tick % 2 == 0) {
                    start = "&a&l小型事件! ";
                } else {
                    start = "&2&l小型事件! ";
                }

                final String title = CC.translate(start + "&6&l" + iEvent.getEventName() + " &7将在 &e" + TimeUtil.millisToTimer(normalEnd.getRemaining()) + " &7后结束!");

                ThePit.getInstance()
                        .getBossBar()
                        .getBossBar()
                        .setTitle(title);
                ThePit.getInstance()
                        .getBossBar()
                        .getBossBar()
                        .setProgress(normalEnd.getRemaining() / (5 * 1000 * 60d));

                tick++;
            }
        }.runTaskTimerAsynchronously(ThePit.getInstance(), 10, 10);
    }

    public void inactiveEvent(INormalEvent event) {
        if (activeNormalEvent != event) return;

        this.activeNormalEvent = null;
        IEvent iEvent = (IEvent) event;
        iEvent.onInactive();
    }

    public void readyEpicEvent(IEpicEvent event) {
        this.nextEpicEvent = event;
        this.nextEpicEventTimer = new Cooldown(5, TimeUnit.MINUTES);
        IEvent iEvent = (IEvent) event;

        if (event instanceof IPrepareEvent) {
            ((IPrepareEvent) event).onPreActive();
        }

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (nextEpicEventTimer.hasExpired()) {
                    ThePit.getInstance()
                            .getBossBar()
                            .getBossBar()
                            .setTitle("");
                    Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> activeEvent(event));
                    cancel();
                    return;
                }

                if (tick >= 15) {
                    tick = 0;
                }
                String start;
                if (tick % 2 == 0) {
                    start = "&5&l大型事件! ";
                } else {
                    start = "&d&l大型事件! ";
                }

                final String title = CC.translate(start + "&6&l" + iEvent.getEventName() + " &7将在 &e" + TimeUtil.millisToTimer(nextEpicEventTimer.getRemaining()) + " &7后开始!");

                ThePit.getInstance()
                        .getBossBar()
                        .getBossBar()
                        .setTitle(title);
                ThePit.getInstance()
                        .getBossBar()
                        .getBossBar()
                        .setProgress(nextEpicEventTimer.getRemaining() / (5 * 1000 * 60d));

                tick++;
            }
        }.runTaskTimerAsynchronously(ThePit.getInstance(), 10, 10);
    }

    public void activeEvent(IEpicEvent event) {
        this.nextEpicEvent = null;
        this.activeEpicEvent = event;
        IEvent iEvent = (IEvent) event;
        this.normalEnd = new Cooldown(5, TimeUnit.MINUTES);
        iEvent.onActive();
    }

    public void inactiveEvent(IEpicEvent event) {
        if (activeEpicEvent != event) return;

        this.activeEpicEvent = null;
        IEvent iEvent = (IEvent) event;

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), BURP, 1, 0.72F));
        iEvent.onInactive();
    }

    public List<INormalEvent> getNormalEvents() {
        return normalEvents;
    }

    public List<IEpicEvent> getEpicEvents() {
        return epicEvents;
    }

    public INormalEvent getActiveNormalEvent() {
        return activeNormalEvent;
    }

    public IEpicEvent getActiveEpicEvent() {
        return activeEpicEvent;
    }

    public Cooldown getNormalEnd() {
        return normalEnd;
    }

    public void setNormalEnd(Cooldown cooldown) {
        this.normalEnd = cooldown;
    }

    public long getLastNormalEvent() {
        return lastNormalEvent;
    }

    public IEpicEvent getNextEpicEvent() {
        return nextEpicEvent;
    }

    public Cooldown getNextEpicEventTimer() {
        return nextEpicEventTimer;
    }
}

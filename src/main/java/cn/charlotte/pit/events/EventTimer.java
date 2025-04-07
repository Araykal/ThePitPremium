package cn.charlotte.pit.events;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.cooldown.Cooldown;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/9 13:14
 */
public class EventTimer implements Runnable {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
    private static Cooldown cooldown = new Cooldown(0);

    @Override
    public void run() {

        if (!cooldown.hasExpired()) {
            return;
        }

        final String format = DATE_FORMAT.format(System.currentTimeMillis());
        final String[] split = format.split(":");
        final String minString = split[4];
        final EventFactory factory = ThePit.getInstance().getEventFactory();

        final int min = Integer.parseInt(minString);

        if (min == 55) {
            String major = EventsHandler.INSTANCE.nextEvent(true);
            cooldown = new Cooldown(1, TimeUnit.MINUTES);

            factory.getEpicEvents()
                    .stream()
                    .map(event -> (IEvent) event)
                    .filter(iEvent -> iEvent.getEventInternalName().equals(major))
                    .findFirst()
                    .ifPresent(iEvent -> factory.pushEvent((IEpicEvent) iEvent));
        }

        if (min != 55 && min != 50 && min % 5 == 0) {
            if (factory.getActiveEpicEvent() == null && factory.getNextEpicEvent() == null && System.currentTimeMillis() - factory.getLastNormalEvent() >= 3 * 60 * 1000) {
                if (factory.getActiveNormalEvent() == null) {
                    String mini = EventsHandler.INSTANCE.nextEvent(false);
                    cooldown = new Cooldown(1, TimeUnit.MINUTES);

                    factory.getNormalEvents()
                            .stream()
                            .map(event -> (IEvent) event)
                            .filter(iEvent -> iEvent.getEventInternalName().equals(mini))
                            .findFirst()
                            .ifPresent(iEvent -> factory.pushEvent((INormalEvent) iEvent));
                }
            }
        }

    }
}

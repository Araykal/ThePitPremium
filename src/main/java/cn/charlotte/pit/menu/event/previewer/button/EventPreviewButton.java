package cn.charlotte.pit.menu.event.previewer.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.events.EventsHandler;
import cn.charlotte.pit.events.IEpicEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.INormalEvent;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @Creator Misoryan
 * @Date 2021/5/5 12:13
 */
@RequiredArgsConstructor
public class EventPreviewButton extends Button {

    private final int page;
    private final boolean major;

    private static String getNormalEventNameByInternalName(String internal) {
        for (INormalEvent normalEvent : ThePit.getInstance().getEventFactory().getNormalEvents()) {
            IEvent event = (IEvent) normalEvent;
            if (event.getEventInternalName().equalsIgnoreCase(internal)) {
                return event.getEventName();
            }
        }
        return "&c???";
    }

    private static String getEpicEventNameByInternalName(String internal) {
        for (IEpicEvent epicEvent : ThePit.getInstance().getEventFactory().getEpicEvents()) {
            IEvent event = (IEvent) epicEvent;
            if (event.getEventInternalName().equalsIgnoreCase(internal)) {
                return event.getEventName();
            }
        }
        return "&c???";
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        EventsHandler instance = EventsHandler.INSTANCE;
        Queue<String> queues = (major ? instance.getEpicQueue() : instance.getNormalQueue());
        for (int i = 0; i < 5; i++) {
            int index = 0;
            try {
                for (String queue : queues) {
                    if (index == page * 5 + i) {
                        String tag;
                        if (major) {
                            Calendar now = Calendar.getInstance();
                            now.setTime(new Date());
                            int minute = now.get(Calendar.MINUTE);
                            if (minute >= 55) {
                                Calendar nextHour = Calendar.getInstance();
                                nextHour.add(Calendar.HOUR, index + 1);
                                nextHour.set(Calendar.MINUTE, 0);
                                nextHour.set(Calendar.SECOND, 0);
                                nextHour.set(Calendar.MILLISECOND, 0);
                                tag = "&e" + TimeUtil.millisToRoundedTime(nextHour.getTimeInMillis() - System.currentTimeMillis() + 55 * 60 * 1000).replace(" ", "") + "后: " + getEpicEventNameByInternalName(queue);
                            } else {
                                now.set(Calendar.MINUTE, 55);
                                now.add(Calendar.HOUR, index);
                                tag = "&e" + TimeUtil.millisToRoundedTime(now.getTimeInMillis() - System.currentTimeMillis()).replace(" ", "") + "后: " + getEpicEventNameByInternalName(queue);
                            }
                        } else {
                            tag = "&e" + (page * 5 + i + 1) + ". " + getNormalEventNameByInternalName(queue);
                        }
                        lines.add(tag);
                    }
                    index++;
                }
            } catch (Exception ignored) {
                return new ItemBuilder(Material.BARRIER)
                        .name("&c正在刷新数据中,请稍后..")
                        .build();
            }
        }
        ItemBuilder itemBuilder = new ItemBuilder(Material.SIGN)
                .amount(page + 1)
                .name("&7即将到来的" + (major ? "大" : "小") + "型事件")
                .lore(lines);
        if (major) itemBuilder.shiny();
        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

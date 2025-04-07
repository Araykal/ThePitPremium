package cn.charlotte.pit.menu.main.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.events.impl.AuctionEvent;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/25 18:44
 */
public class BidHistoryButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() != null && eventFactory.getActiveNormalEventName().equals("auction")) {
            AuctionEvent event = (AuctionEvent) eventFactory.getActiveNormalEvent();
            lines.add("&7累计竞价次数: &a" + event.getBidHistories().size() + " 次");
            final List<AuctionEvent.BidHistory> finalBidHistories = event.getBidHistories();
            finalBidHistories.sort(new Comparator<AuctionEvent.BidHistory>() {
                @Override
                public int compare(AuctionEvent.BidHistory o1, AuctionEvent.BidHistory o2) {
                    //o1 - o2 means min--->max
                    return (int) (o2.getTime() - o1.getTime());
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });
            if (finalBidHistories.size() > 0) {
                lines.add(" ");
                lines.add("&8&m" + CC.stripColor(CC.MENU_BAR));
            }
            for (int i = 0; i < Math.min(6, finalBidHistories.size()); i++) {
                AuctionEvent.BidHistory bid = finalBidHistories.get(i);
                lines.add("&7出价: &6" + ((int) bid.getCoins()) + " 硬币");
                lines.add("&7来自: " + PlayerProfile.getPlayerProfileByUuid(bid.getUuid()).getFormattedNameWithRoman());
                lines.add("&b" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - bid.getTime()) + "前");
                lines.add("&8&m" + CC.stripColor(CC.MENU_BAR));
            }

        } else {
            return new ItemStack(Material.AIR);
        }
        return new ItemBuilder(Material.MAP)
                .name("&e竞拍记录")
                .lore(lines)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}


package cn.charlotte.pit.menu.main.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.events.impl.AuctionEvent;
import cn.charlotte.pit.menu.main.AuctionMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/25 19:15
 */
public class AuctionBidButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() != null && eventFactory.getActiveNormalEventName().equals("auction")) {
            AuctionEvent event = (AuctionEvent) eventFactory.getActiveNormalEvent();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            lines.add("");
            AuctionEvent.BidHistory highestBid = event.getHighestBidHistory();

            if (highestBid != null) {
                lines.add("&7当前最高出价: &6" + ((int) highestBid.getCoins()) + " 硬币");
                lines.add("&7来自: " + PlayerProfile.getPlayerProfileByUuid(highestBid.getUuid()).getFormattedNameWithRoman());
                lines.add("&7结束: &a" + TimeUtil.millisToTimer(event.getTimer().getRemaining()));
                lines.add("");
                lines.add("&7叫价: &6" + (int) (event.getRate() * highestBid.getCoins()) + " 硬币");
                if (event.getHighestBidHistory(player.getUniqueId()) != null) {
                    AuctionEvent.BidHistory playerHighestBid = event.getHighestBidHistory(player.getUniqueId());
                    lines.add("&7你的当前出价: &6" + (int) playerHighestBid.getCoins() + " 硬币");
                }
            } else {
                lines.add("&7起价: &6" + (int) event.getLots().getStartPrice() + " 硬币");
                lines.add("&7结束: &a" + TimeUtil.millisToTimer(event.getTimer().getRemaining()));
            }
            lines.add("");
            lines.add("&8拍卖结束后,若你没有成功拍下物品,");
            lines.add("&8参与竞拍的硬币会全部退回.");
            lines.add("");
            //if player hasn't bid before
            boolean canAfford = false;
            if (event.getHighestBidHistory(profile.getPlayerUuid()) == null) {
                //if player has enough coins
                canAfford = profile.getCoins() >= event.getRate() * (highestBid == null ? 0 : highestBid.getCoins());
            } else { //if player has bid before
                AuctionEvent.BidHistory playerHighestBid = event.getHighestBidHistory(profile.getPlayerUuid());
                canAfford = profile.getCoins() + playerHighestBid.getCoins() >= event.getRate() * (highestBid == null ? 0 : highestBid.getCoins());
            }
            //check if player is allowed to attend auction
            if (event.isAllowedToParticipate(player.getUniqueId())) {
                if (event.getHighestBidHistory() != null && event.getHighestBidHistory().getUuid().equals(player.getUniqueId())) {
                    lines.add("&c你已经是最高出价者了!");
                } else {
                    if (canAfford) {
                        lines.add("&b右键设置竞价金额!");
                        lines.add("&e点击快速竞价!");
                    } else {
                        lines.add("&c你的硬币不足!");
                    }
                }
            } else {
                lines.add("&c拍卖开始时你不在线!");
            }
        } else {
            return new ItemStack(Material.AIR);
        }
        return new ItemBuilder(Material.GOLD_BARDING)
                .name("&6参与竞拍")
                .lore(lines)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() != null && eventFactory.getActiveNormalEventName().equals("auction")) {
            AuctionEvent event = (AuctionEvent) eventFactory.getActiveNormalEvent();
            if (!event.isAllowedToParticipate(player.getUniqueId())) {
                return;
            }
            if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                ThePit.getInstance()
                        .getSignGui()
                        .open(player, new String[]{"", "~~~~~~~~~~~~~", "请在此输入", "你的出价"}, (player1, lines) -> {
                            player.closeInventory();
                            try {
                                final int bidPrice = Integer.parseInt(lines[0]);
                                event.playerBid(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()), bidPrice);
                            } catch (Exception ignore) {
                                player.sendMessage(CC.translate("&c你只能输入一个整数!"));
                            }
                            new AuctionMenu().openMenu(player);
                        });
            } else {
                event.playerBid(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()));
            }
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

}

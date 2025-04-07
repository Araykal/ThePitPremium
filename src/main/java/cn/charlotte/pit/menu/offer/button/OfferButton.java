package cn.charlotte.pit.menu.offer.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.data.sub.OfferData;
import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.medal.impl.challenge.FirstTradeMedal;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 13:58
 */
@RequiredArgsConstructor
public class OfferButton extends Button {
    private final Player target;

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PlayerProfile targetProfile = PlayerProfile.getOrLoadPlayerProfileByUuid(target.getUniqueId());
        List<String> lines = new ArrayList<>();
        lines.add("&7来自: " + targetProfile.getFormattedName());
        lines.add("");
        lines.add("&7价格: &6" + (int) targetProfile.getOfferData().getPrice() + " 硬币");
        lines.add("&7过期: &e" + (TimeUtil.millisToRoundedTime(targetProfile.getOfferData().getEndTime() - System.currentTimeMillis()).replace(" ", "") + "后"));
        lines.add("");
        ItemBuilder builder = new ItemBuilder(Material.STAINED_CLAY)
                .name("&e交易报价");
        if (targetProfile.getOfferData().getPrice() > profile.getCoins()) {
            lines.add("&c你的硬币不足!");
            builder.durability(14);
        } else {
            lines.add("&e点击接受!");
            builder.durability(5);
        }
        return builder.lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (InventoryUtil.isInvFull(player)) {
            player.sendMessage(CC.translate("&c你的背包已满!"));
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PlayerProfile targetProfile = PlayerProfile.getPlayerProfileByUuid(target.getUniqueId());
        if (!profile.isLoaded() || !targetProfile.isLoaded()) {
            player.sendMessage(CC.translate("&c当前无法完成此交易报价!"));
            return;
        }
        if (targetProfile.getOfferData().getPrice() > profile.getCoins()) {
            return;
        }
        player.closeInventory();
        InventoryUtil.addInvReverse(player.getInventory(), targetProfile.getOfferData().getItemStack());
        profile.setCoins(profile.getCoins() - targetProfile.getOfferData().getPrice());
        targetProfile.setCoins(targetProfile.getCoins() + targetProfile.getOfferData().getPrice());
        profile.getTradeLimit().setAmount(profile.getTradeLimit().getTimes() + targetProfile.getOfferData().getPrice());
        profile.getTradeLimit().setTimes(profile.getTradeLimit().getTimes() + 1);
        targetProfile.getTradeLimit().setAmount(targetProfile.getTradeLimit().getTimes() + targetProfile.getOfferData().getPrice());
        targetProfile.getTradeLimit().setTimes(targetProfile.getTradeLimit().getTimes() + 1);
        //save trade log start
        TradeData tradeData = new TradeData(player.getUniqueId().toString(), target.getUniqueId().toString(), player.getName(), target.getName());
        tradeData.setBPaidCoin(0);
        List<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(targetProfile.getOfferData().getItemStack());
        PlayerInv bInv = new PlayerInv();
        bInv.setContents(itemStacks.toArray(new ItemStack[36]));
        tradeData.setBPaidItem(bInv);
        tradeData.setAPaidCoin(Double.valueOf(targetProfile.getOfferData().getPrice()).longValue());
        PlayerInv aInv = new PlayerInv();
        aInv.setContents(new ItemStack[36]);
        tradeData.setAPaidItem(aInv);
        tradeData.save();
        //save trade log end
        player.sendMessage(CC.translate("&a交易成功!"));
        target.sendMessage(CC.translate("&a&l交易成功! " + profile.getFormattedName() + " &7接受了你的交易报价,硬币已发送到你的账户中."));
        new FirstTradeMedal().addProgress(profile, 1);
        new FirstTradeMedal().addProgress(targetProfile, 1);
        targetProfile.setOfferData(new OfferData());
    }
}

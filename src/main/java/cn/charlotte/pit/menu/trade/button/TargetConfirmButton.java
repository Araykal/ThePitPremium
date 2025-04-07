package cn.charlotte.pit.menu.trade.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.rank.RankUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 21:22
 */
public class TargetConfirmButton extends Button {
    private final TradeManager tradeManager;

    public TargetConfirmButton(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean confirm;
        String display_name;
        PlayerProfile targetProfile;
        if (tradeManager.getPlayerA().equals(player)) {
            confirm = tradeManager.isBConfirm();
            targetProfile = PlayerProfile.getPlayerProfileByUuid(tradeManager.getPlayerB().getUniqueId());
            display_name = LevelUtil.getLevelTag(targetProfile.getPrestige(), targetProfile.getLevel()) + " " + RankUtil.getPlayerColoredName(tradeManager.getPlayerB().getUniqueId());
        } else {
            targetProfile = PlayerProfile.getPlayerProfileByUuid(tradeManager.getPlayerA().getUniqueId());
            display_name = LevelUtil.getLevelTag(targetProfile.getPrestige(), targetProfile.getLevel()) + " " + RankUtil.getPlayerColoredName(tradeManager.getPlayerA().getUniqueId());
            confirm = tradeManager.isAConfirm();
        }

        if (confirm) {
            return new ItemBuilder(Material.INK_SACK)
                    .durability(10)
                    .name("&a对方已确认!")
                    .lore("&7你正在与玩家 " + display_name + " &7进行交易.", "&7正在等待你确认交易内容...")
                    .build();
        } else {
            return new ItemBuilder(Material.INK_SACK)
                    .durability(8)
                    .name("&e对方暂未确认..")
                    .lore("&7你正在与玩家 " + display_name + " &7进行交易.", "&7对方当前没有确认交易内容...")
                    .build();
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

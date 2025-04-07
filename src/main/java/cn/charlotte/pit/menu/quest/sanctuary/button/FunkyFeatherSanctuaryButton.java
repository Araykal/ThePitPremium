package cn.charlotte.pit.menu.quest.sanctuary.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/25 15:43
 */
public class FunkyFeatherSanctuaryButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("&e特殊物品");
        lore.add("&7放于物品栏时,可以保护");
        lore.add("&7背包内的神话物品不会在死亡后扣除生命.");
        lore.add("&7&o此物品会在死亡后消耗");
        lore.add(" ");
        lore.add("&7价格: &e225 行动赏金");
        lore.add("&7你当前持有: &e" + profile.getActionBounty() + " 行动赏金");
        lore.add(" ");
        if (225 > profile.getActionBounty()) {
            lore.add("&c你没有足够的行动赏金!");
        } else {
            lore.add("&e点击购买!");
        }
        return new ItemBuilder(Material.FEATHER).name("&3时髦的羽毛").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (InventoryUtil.isInvFull(player)) {
            player.sendMessage(CC.translate("&c你的背包已满!"));
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getActionBounty() >= 225) {
            profile.setActionBounty(profile.getActionBounty() - 225);
            List<String> lore = new ArrayList<>();
            lore.add("&e特殊物品");
            lore.add("&7放于物品栏时,可以保护");
            lore.add("&7背包内的神话物品不会在死亡后扣除生命.");
            lore.add("&7&o此物品会在死亡后消耗");
            player.getInventory().addItem(new ItemBuilder(Material.FEATHER).name("&3时髦的羽毛").lore(lore).internalName("funky_feather").canTrade(true).canSaveToEnderChest(true).build());
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

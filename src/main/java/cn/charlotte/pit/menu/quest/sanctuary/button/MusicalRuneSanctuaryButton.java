package cn.charlotte.pit.menu.quest.sanctuary.button;

import cn.charlotte.pit.ThePit;
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

public class MusicalRuneSanctuaryButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("&e特殊物品");
        lore.add("&7为神话之甲增加一个指定的DJ附魔");
        lore.add("&7一件物品只能使用一次");
        lore.add(" ");
        lore.add("&7价格: &e1200 行动赏金");
        lore.add("&7你当前持有: &e" + profile.getActionBounty() + " 行动赏金");
        lore.add(" ");
        if (1200 > profile.getActionBounty()) {
            lore.add("&c你没有足够的行动赏金！");
        } else {
            lore.add("&e点击购买！");
        }
        return new ItemBuilder(Material.JUKEBOX).name("&6音乐符文").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (InventoryUtil.isInvFull(player)) {
            player.sendMessage(CC.translate("&c你的背包已满！"));
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getActionBounty() >= 1200) {
            profile.setActionBounty(profile.getActionBounty() - 1200);
            player.getInventory().addItem(this.toItemStack());
        }
    }
    public ItemStack toItemStack() {
        return ThePit.getApi().getMythicItemItemStack("musical_rune");
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

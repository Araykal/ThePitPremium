package cn.charlotte.pit.menu.pack.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.impl.CarePackageEvent;
import cn.charlotte.pit.menu.pack.PackageMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 17:20
 */
public class ItemButton extends DisplayButton {

    public ItemButton(ItemStack itemStack) {
        super(itemStack, true);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        CarePackageEvent.ChestData chestData = CarePackageEvent.getChestData();
        if (chestData == null) {
            return;
        }

        if (clickType != ClickType.LEFT) {
            return;
        }

        if (chestData.getRewarded().contains(player.getUniqueId())) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            return;
        }
        ItemStack itemStack = PackageMenu.getItems().remove(slot);
        if (itemStack != null) {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if ("xp_reward".equals(ItemUtil.getInternalName(itemStack))) {
                profile.setExperience(profile.getExperience() + 1000);

                player.sendMessage(CC.translate("&6&l空投! &7你从空投箱中找到了 &b1000 &7经验"));
            } else if ("coin_reward".equals(ItemUtil.getInternalName(itemStack))) {
                profile.setCoins(profile.getCoins() + 1000);
                player.sendMessage(CC.translate("&6&l空投! &7你从空投箱中找到了 &61000 &7硬币"));
            } else if ("renown_reward".equals(ItemUtil.getInternalName(itemStack))) {
                profile.setRenown(profile.getRenown() + 2);
                player.sendMessage(CC.translate("&6&l空投! &7你从空投箱中找到了 &e2 &7声望"));
            } else {
                player.getInventory().addItem(itemStack);
                CC.boardCast(MessageType.EVENT, "&6&l空投! " + profile.getFormattedName() + "&7 从空投箱中找到了 " + itemStack.getItemMeta().getDisplayName());
            }
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            chestData.getRewarded().add(player.getUniqueId());
        }
        if (PackageMenu.getItems().isEmpty()) {
            ThePit.getInstance()
                    .getEventFactory()
                    .inactiveEvent(ThePit.getInstance().getEventFactory().getActiveNormalEvent());
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

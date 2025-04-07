package cn.charlotte.pit.menu.trade;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerInvBackup;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/4 20:46
 */
public class ShowInvBackupButton extends DisplayButton {
    private final PlayerInvBackup backup;
    private final PlayerProfile profile;

    public ShowInvBackupButton(ItemStack itemStack, PlayerInvBackup backup, PlayerProfile profile) {
        super(itemStack, true);
        this.backup = backup;
        this.profile = profile;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        ThePit.api.openBackupShowMenu(player, profile, backup, clickType.isRightClick());
    }
}

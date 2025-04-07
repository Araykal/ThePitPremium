package cn.charlotte.pit.util.menu.buttons;

import cn.charlotte.pit.util.callback.Callback;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmationButton extends Button {

    private final boolean confirm;
    private final Callback<Boolean> callback;
    private final boolean closeAfterResponse;
    private int delaySec;

    public ConfirmationButton(boolean confirm, Callback<Boolean> callback, boolean closeAfterResponse, int delaySec) {
        this.confirm = confirm;
        this.callback = callback;
        this.closeAfterResponse = closeAfterResponse;
        this.delaySec = delaySec;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack;
        if (delaySec <= 0) {
            itemStack = new ItemStack(Material.STAINED_CLAY, 1, this.confirm ? ((byte) 5) : ((byte) 14));
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(this.confirm ? CC.GREEN + "确认" : CC.RED + "取消");
            itemStack.setItemMeta(itemMeta);

        } else {
            itemStack = new ItemStack(Material.STAINED_CLAY, 1, this.confirm ? ((byte) 4) : ((byte) 14));
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(CC.translate(this.confirm ? CC.GREEN + "你确定吗? &7(" + delaySec + ")" : CC.RED + "取消"));
            itemStack.setItemMeta(itemMeta);

        }
        delaySec--;
        return itemStack;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb, ItemStack currentItem) {
        if (delaySec >= 0 && confirm) {
            return;
        }

        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20f, 0.1f);
        } else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20f, 0.1F);
        }

        if (this.closeAfterResponse) {
            player.closeInventory();
        }

        this.callback.call(this.confirm);
    }

}

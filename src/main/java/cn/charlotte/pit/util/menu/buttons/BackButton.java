package cn.charlotte.pit.util.menu.buttons;

import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BackButton extends Button {

    private final Menu back;

    public BackButton(Menu back) {
        this.back = back;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.BED);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.RED + "返回上一级菜单");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb, ItemStack currentItem) {
        Button.playNeutral(player);

        this.back.openMenu(player);
    }

}

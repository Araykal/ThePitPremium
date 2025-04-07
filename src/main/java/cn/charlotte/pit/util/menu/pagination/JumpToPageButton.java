package cn.charlotte.pit.util.menu.pagination;

import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class JumpToPageButton extends Button {

    private final int page;
    private final PaginatedMenu menu;
    private final boolean current;

    public JumpToPageButton(int page, PaginatedMenu menu, boolean current) {
        this.page = page;
        this.menu = menu;
        this.current = current;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.YELLOW + "Page " + this.page);

        if (this.current) {
            itemMeta.setLore(Arrays.asList(
                    "",
                    CC.GREEN + "Current page"
            ));
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb, ItemStack currentItem) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }

}

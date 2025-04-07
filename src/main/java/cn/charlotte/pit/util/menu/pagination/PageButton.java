package cn.charlotte.pit.util.menu.pagination;

import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PageButton extends Button {

    private final int mod;
    private final PaginatedMenu menu;

    public PageButton(int mod, PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.CARPET);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.hasNext(player)) {
            itemMeta.setDisplayName(this.mod > 0 ? CC.GREEN + "Next page" : CC.RED + "Previous page");
        } else {
            itemMeta.setDisplayName(CC.GRAY + (this.mod > 0 ? "Last page" : "First page"));
        }

        itemMeta.setLore(Arrays.asList(
                "",
                CC.YELLOW + "Right click to",
                CC.YELLOW + "jump to a page"
        ));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb, ItemStack currentItem) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            playNeutral(player);
        } else {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }

}

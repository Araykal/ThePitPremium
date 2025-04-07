package cn.charlotte.pit.item.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.item.AbstractPitItem;
import cn.charlotte.pit.menu.rune.MusicalRuneMenu;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class MusicalRune extends AbstractPitItem implements Listener {

    @Override
    @NotNull
    public String getInternalName() {
        return "musical_rune";
    }

    @Override
    @NotNull
    public String getItemDisplayName() {
        return "&6音乐符文";
    }

    @Override
    @NotNull
    public Material getItemDisplayMaterial() {
        return Material.NETHER_STAR;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null) {
            if ("musical_rune".equals(ItemUtil.getInternalName(item))) {
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(ThePit.getInstance(),() ->{
                    new MusicalRuneMenu().openMenu(event.getPlayer());
                });
            }
        }
    }

    public ItemStack toItemStack() {
        return new ItemBuilder(getItemDisplayMaterial())
                .name(this.getItemDisplayName())
                .lore("&7死亡时保留", "", "&7为神话之甲增加一个指定的DJ附魔", "&8一件物品只能使用一次", "", "&e右键以使用")
                .internalName(this.getInternalName())
                .canSaveToEnderChest(true)
                .removeOnJoin(false)
                .deathDrop(false)
                .canDrop(false)
                .canTrade(false)
                .shiny()
                .build();
    }


    @Override
    public void loadFromItemStack(ItemStack item) {

    }
}

package cn.charlotte.pit.minigame.type.menu.button;

import cn.charlotte.pit.minigame.type.FourInARow;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/25 13:09
 */
public class ChessButton extends Button {
    private final FourInARow gameInstance;
    private final FourInARow.PosInfo posInfo;
    private final boolean canPlay;

    public ChessButton(FourInARow gameInstance, FourInARow.PosInfo posInfo, boolean canPlay) {
        this.gameInstance = gameInstance;
        this.posInfo = posInfo;
        this.canPlay = canPlay;
    }

    @Override
    public ItemStack getButtonItem(Player player) {

        if (posInfo.getMarkedInfo() == FourInARow.Marked.NULL) {
            final ItemBuilder builder = new ItemBuilder(canPlay ? Material.WOOD_BUTTON : Material.STONE_BUTTON)
                    .name(canPlay ? "&a点击落子" : "&c等待对手下棋...");

            if (canPlay) {
                builder.shiny();
            }

            return builder.build();
        }

        final ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM);

        builder.durability(3)
                .setSkullProperty(posInfo.getMarkedInfo() == FourInARow.Marked.WHITE ? "ewogICJ0aW1lc3RhbXAiIDogMTYwMjYxOTc3MTM1NCwKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFiNjE2OTZjOTZiM2U3ODczMDk1OTc5MTkyNDYzNWQxMTM4ODM0YTJmMDFiNWQzMzNkNTNhY2JmYzUxYWExMSIKICAgIH0KICB9Cn0" :
                        "eyJ0aW1lc3RhbXAiOjE1MjY2MTI4ODk4MjIsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U3NzYxODIxMjUyOTc5OGM3MTliNzE2MmE0NzNhNjg1YzQzNTczMjBhODY5NjE2NWU3OTY3OTBiOTBmYmE2NyJ9fX0");
        if (posInfo.getMarkedInfo() == FourInARow.Marked.BLACK) {
            builder.name("&8黑棋");
        } else {
            builder.name("&f白棋");
        }


        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (gameInstance.getCurrentState() == FourInARow.GameState.END) {
            return;
        }
        if (!canPlay) {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            player.sendMessage(CC.translate("&c请等待对手落子"));
            return;
        }
        if (posInfo.getMarkedInfo() == FourInARow.Marked.NULL) {
            gameInstance.tryPlay(player, posInfo);
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
        } else {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
            player.sendMessage(CC.translate("&c你不能下在那里"));
        }
    }
}

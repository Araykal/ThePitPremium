package cn.charlotte.pit.menu.option.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.chat.CC;
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
 * @Created_In: 2021/3/10 19:29
 */
public class BountyHiddenWhenNearOptionButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        try {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            lines.add("&7靠近附带悬赏特效的玩家时,选择特效是否隐藏.");
            lines.add(" ");
            lines.add("&7当前: " + (profile.getPlayerOption().isBountyHiddenWhenNear() ? "&c靠近时隐藏" : "&a始终显示"));
            lines.add("&e点击切换此选项!");
            return new ItemBuilder(Material.GOLD_INGOT).name("&a画面选项: 赏金").lore(lines).build();
        } catch (Exception e) {
            CC.printError(player, e);
        }
        return new ItemBuilder(Material.GOLD_INGOT).name("&a画面选项: 赏金").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getPlayerOption().setBountyHiddenWhenNear(!profile.getPlayerOption().isBountyHiddenWhenNear());
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

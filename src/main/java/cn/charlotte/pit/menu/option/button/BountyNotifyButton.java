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
 * @Created_In: 2021/1/13 22:38
 */
public class BountyNotifyButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        try {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            lines.add("&7接收除你以外玩家的赏金相关消息,");
            lines.add("&7包括悬赏,悬赏收取等类型.");
            lines.add(" ");
            lines.add("&7当前: " + (profile.getPlayerOption().isBountyNotify() ? "&a开启" : "&c关闭"));
            lines.add("&e点击切换此选项!");
            return new ItemBuilder(Material.GOLD_NUGGET).name("&a消息选项: 赏金").lore(lines).build();
        } catch (Exception e) {
            CC.printError(player, e);
        }
        return new ItemBuilder(Material.GOLD_NUGGET).name("&a消息选项: 赏金").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getPlayerOption().setBountyNotify(!profile.getPlayerOption().isBountyNotify());
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

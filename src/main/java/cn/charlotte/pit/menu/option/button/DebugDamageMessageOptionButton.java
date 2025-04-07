package cn.charlotte.pit.menu.option.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.PlayerUtil;
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
 * @Created_In: 2021/3/6 22:09
 */
public class DebugDamageMessageOptionButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!PlayerUtil.isStaff(player)) {
            profile.getPlayerOption().setDebugDamageMessage(false);
        }
        lines.add("&7显示伤害测试消息.");
        lines.add(" ");
        lines.add("&7当前: " + (profile.getPlayerOption().isDebugDamageMessage() ? "&a允许" : "&c不允许"));
        lines.add(PlayerUtil.isStaff(player) ? "&e&e点击切换此选项!" : "&c需要用户组&9志愿者");
        return new ItemBuilder(Material.DIAMOND_SPADE).name("&a测试选项: 伤害测试显示").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (!PlayerUtil.isStaff(player)) {
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getPlayerOption().setDebugDamageMessage(!profile.getPlayerOption().isDebugDamageMessage());
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

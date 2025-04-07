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
 * @Creator Misoryan
 * @Date 2021/5/4 19:04
 */
public class SupportStarOptionButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&7后缀: &e✬");
        lines.add("&7调整是否在名称前后显示此后缀.");
        if (profile.isNicked()) {
            lines.add(" ");
            lines.add("&8后缀将在匿名模式启用时隐藏!");
        }
        lines.add(" ");
        lines.add("&7当前: " + (profile.getPlayerOption().isSupporterStarDisplay() ? "&a允许" : "&c不允许"));
        lines.add((profile.isSupporter() || PlayerUtil.isStaff(player) ? "&e&e点击切换此选项!" : "&c需要&e天坑乱斗会员"));
        return new ItemBuilder(Material.BEACON).name("&a后缀显示选项: 会员星标").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isSupporter() && !PlayerUtil.isStaff(player)) {
            return;
        }
        profile.getPlayerOption().setSupporterStarDisplay(!profile.getPlayerOption().isSupporterStarDisplay());
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

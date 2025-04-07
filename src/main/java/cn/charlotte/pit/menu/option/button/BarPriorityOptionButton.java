package cn.charlotte.pit.menu.option.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PlayerOption;
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
 * @Created_In: 2021/3/10 19:36
 */
public class BarPriorityOptionButton extends Button {
    public static PlayerOption.BarPriority switchBarPriority(PlayerOption.BarPriority barPriority) {
        switch (barPriority) {
            case DAMAGE_ONLY:
                return PlayerOption.BarPriority.ENCHANT_ONLY;
            case ENCHANT_ONLY:
                return PlayerOption.BarPriority.DAMAGE_PRIORITY;
            case DAMAGE_PRIORITY:
                return PlayerOption.BarPriority.DAMAGE_ONLY;
        }
        return PlayerOption.BarPriority.DAMAGE_PRIORITY;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        try {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            lines.add("&7调整多种类型信息出现在提示栏(物品栏上方)时的显示优先级.");
            lines.add(" ");
            lines.add("&7当前: &e" + profile.getPlayerOption().getBarPriority().getDescription());
            lines.add("&e点击切换此选项!");
            return new ItemBuilder(Material.EMPTY_MAP).name("&a信息选项: 提示栏").lore(lines).build();
        } catch (Exception e) {
            CC.printError(player, e);
        }
        return new ItemBuilder(Material.EMPTY_MAP).name("&a画面选项: 提示栏").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getPlayerOption().setBarPriority(switchBarPriority(profile.getPlayerOption().getBarPriority()));
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

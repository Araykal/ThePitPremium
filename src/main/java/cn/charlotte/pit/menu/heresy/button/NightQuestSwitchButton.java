package cn.charlotte.pit.menu.heresy.button;

import cn.charlotte.pit.data.PlayerProfile;
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
 * @Created_In: 2021/4/5 18:37
 */
public class NightQuestSwitchButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        lines.add("&7在此模式下,你可以在夜晚通过完成专属");
        lines.add("&7的 &9夜间任务 &7以获得特殊物品 &5暗聚块 &7.");
        lines.add(" ");
        lines.add("&7夜幕模式的开启与否是可选的.");
        lines.add(" ");
        lines.add("&7当前状态: " + (profile.isNightQuestEnable() ? "&a开启" : "&c关闭"));
        /*
        lines.add(" ");
        lines.add("&7注意: 你也可以在日间完成任务以");
        lines.add("&7获得 &5暗聚块 &7!");

         */
        lines.add(" ");
        if (profile.getCurrentQuest() != null) {
            lines.add("&c请先结束当前进行中的任务后重试!");
        } else {
            lines.add("&e点击切换!");
        }

        return new ItemBuilder(Material.LAPIS_BLOCK)
                .name("&9夜幕模式")
                .lore(lines)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getCurrentQuest() != null) {
            return;
        }
        profile.setNightQuestEnable(!profile.isNightQuestEnable());
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

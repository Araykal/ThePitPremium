package cn.charlotte.pit.menu.medal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.MedalFactory;
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
 * @Date 2021/6/10 9:19
 */
public class MedalEntranceButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        MedalFactory medalFactory = ThePit.getInstance().getMedalFactory();
        ItemBuilder builder = new ItemBuilder(Material.DIAMOND);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        builder.name("&a天坑乱斗成就");
        List<String> lines = new ArrayList<>();
        lines.add("&7已解锁: &b" + profile.getMedalData().getUnlockedMedals().size() + "&7/&b" + medalFactory.getMedalAmount());
        lines.add("");
        lines.add("&c此玩法正在开发与完善当中,");
        lines.add("&c因此部分成就已被禁用&隐藏!");
        lines.add("");
        lines.add("&e点击查看成就!");
        builder.lore(lines);
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        new MedalMainMenu().openMenu(player);
    }
}

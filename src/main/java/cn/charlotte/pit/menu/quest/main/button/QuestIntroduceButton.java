package cn.charlotte.pit.menu.quest.main.button;

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
 * @Created_In: 2021/1/21 20:54
 */
public class QuestIntroduceButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        lore.add("&7在这里查看日常,周常任务");
        lore.add("&7与天坑乱斗挑战任务.");
        lore.add(" ");
        lore.add("&7你可以在若干个挑战中选择其一");
        lore.add("&7进行,挑战成功可以获得奖励.");
        lore.add(" ");
        lore.add("&7行动赏金: &e" + profile.getActionBounty());
        lore.add("&7行动赏金可在兑换所内购买物资与加成!");
        return new ItemBuilder(Material.DIRT).name("&a任务 & 挑战").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

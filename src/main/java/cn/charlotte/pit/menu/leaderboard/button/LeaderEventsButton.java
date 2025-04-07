package cn.charlotte.pit.menu.leaderboard.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LeaderEventsButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lore.add("&7在乱斗事件中,！");
        lore.add("&7通过获得最高分数来证明你的竞技能力！");
        lore.add(" ");
        lore.add("&8每3个月重置,只包括近一周上线过的玩家。");
        lore.add(" ");
        lore.add("&7赛季: &c未开始");
        lore.add(" ");
        lore.add("&c未启用");
        return new ItemBuilder(Material.BANNER).name("&5事件排行榜").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT,1,1);
        player.sendMessage(CC.translate("&c因为此选项并未启用,因此你无法进行选择！"));
    }
}

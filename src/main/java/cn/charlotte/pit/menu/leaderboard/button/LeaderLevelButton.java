package cn.charlotte.pit.menu.leaderboard.button;

import cn.charlotte.pit.data.LeaderBoardEntry;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.rank.RankUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/21 20:54
 */
public class LeaderLevelButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> hologramText = new ArrayList<>();
        hologramText.add("&7天坑乱斗等级排名");
        hologramText.add("");
        hologramText.add("&7获得最多&b经验&7的玩家。");
        hologramText.add("");


        List<LeaderBoardEntry> entries = new ArrayList<>(LeaderBoardEntry.getLeaderBoardEntries());
        for (int b = 0; b < 10; b++) {
            if (b + 1 > entries.size()) {
                hologramText.add("&e" + (b + 1) + "&7. &7暂无");
            } else {
                LeaderBoardEntry entry = entries.get(b);

                int prestige = entry.getPrestige();
                double experience = entry.getExperience();
                for (int i = 0; i < prestige; i++) {
                    experience = experience + LevelUtil.getLevelTotalExperience(i, 120);
                }
                String levelTag = LevelUtil.getLevelTagWithRoman(entry.getPrestige(), entry.getExperience());
                String formattedExp = StringUtil.getFormatLong((long) experience);

                hologramText.add("&e" + entry.getRank() + "&7." + " " + levelTag + " " + RankUtil.getPlayerRealColoredName(entry.getUuid()) + " &7- &b" + formattedExp + " 经验值");
            }
        }

        Optional<LeaderBoardEntry> first = LeaderBoardEntry.getLeaderBoardEntries()
                .stream()
                .filter(leaderBoardEntry -> leaderBoardEntry.getUuid().equals(player.getUniqueId()))
                .findFirst();

        if (first.isPresent()) {
            LeaderBoardEntry entry = first.get();
            double top = 100D * entry.getRank() / LeaderBoardEntry.getLeaderBoardEntries().size();
            int prestige = entry.getPrestige();
            double experience = entry.getExperience();
            for (int i = 0; i < prestige; i++) {
                experience = experience + LevelUtil.getLevelTotalExperience(i, 120);
            }
            String formattedExp = StringUtil.getFormatLong((long) experience);

            hologramText.add("");
            hologramText.add("&7你的经验值: &b" + formattedExp + " / Exp");
            hologramText.add("&7排名: " + "&e#" + entry.getRank() + " &7(前&e" + new DecimalFormat("0.0").format(top) + "%&7)");
        } else {
            hologramText.add("");
            hologramText.add("&c还没有你的排行数据！");
        }
        hologramText.add("");
        hologramText.add("&a&l已选择！");
        return new ItemBuilder(Material.DIAMOND).name("&b&l顶级活跃玩家").lore(hologramText).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        player.closeInventory();
        player.sendMessage(CC.translate("&a&l已选择！ &7现在显示&b天坑乱斗等级&7的排行榜！"));
    }
}

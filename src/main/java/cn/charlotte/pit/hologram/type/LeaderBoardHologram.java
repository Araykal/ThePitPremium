package cn.charlotte.pit.hologram.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.LeaderBoardEntry;
import cn.charlotte.pit.hologram.AbstractHologram;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.rank.RankUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:04
 */
public class LeaderBoardHologram extends AbstractHologram {
    @Override
    public String getInternalName() {
        return "leader_board_hologram";
    }

    @Override
    public List<String> getText(Player player) {
        List<String> hologramText = new ArrayList<>();
        hologramText.add("&b&l顶级活跃玩家");
        hologramText.add("&7天坑乱斗等级排名");
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

                hologramText.add("&e" + entry.getRank() + "&7." + " " + levelTag + " " + RankUtil.getPlayerRealColoredName(entry.getName()) + " &7- &b" + formattedExp + " 经验值");
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
            hologramText.add("&7你的经验值: &b" + formattedExp + " 经验值");
            hologramText.add("&7排名: " + "&e#" + entry.getRank() + " &7(前&e" + new DecimalFormat("0.0").format(top) + "%&7)");
            hologramText.add("&7本排名仅显示 &f7天 &7内登录过的玩家 (&f" + entries.size() + "&7名)");
        } else {
            hologramText.add("");
            hologramText.add("&7&o还没有你的排行数据,请等会再来...");
        }

        return hologramText;
    }

    @Override
    public boolean shouldLoop() {
        return true;
    }

    @Override
    public int loopTicks() {
        return 20;
    }

    @Override
    public double getHologramHighInterval() {
        return 0.3;
    }

    @Override
    public Location getLocation() {
        return ThePit.getInstance().getPitConfig().getLeaderBoardHologram();
    }
}

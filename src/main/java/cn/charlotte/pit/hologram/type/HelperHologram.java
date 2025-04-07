package cn.charlotte.pit.hologram.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.hologram.AbstractHologram;
import cn.charlotte.pit.util.level.LevelUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/4 14:35
 */
public class HelperHologram extends AbstractHologram {
    @Override
    public String getInternalName() {
        return "Helper";
    }

    @Override
    public List<String> getText(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> hologramText = new ArrayList<>();
        hologramText.add("&e&l已解锁的功能");
        hologramText.add("");
        hologramText.add(LevelUtil.getLevelTag(0, 1) + " &b/spawn 回城");
        if (profile.getPrestige() > 0 || profile.getLevel() >= 5) {
            hologramText.add(LevelUtil.getLevelTag(0, 5) + " &b/settings 游戏选项");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 5) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 10) {
            hologramText.add(LevelUtil.getLevelTag(0, 10) + " &b天赋与商店");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 10) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 15) {
            hologramText.add(LevelUtil.getLevelTag(0, 15) + " &b末影箱");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 15) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 30) {
            hologramText.add(LevelUtil.getLevelTag(0, 30) + " &b任务系统");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 30) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 50) {
            hologramText.add(LevelUtil.getLevelTag(0, 50) + " &b统计信息");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 50) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 60) {
            hologramText.add(LevelUtil.getLevelTag(0, 60) + " &b/trade 交易");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 60) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 60) {
            hologramText.add(LevelUtil.getLevelTag(0, 60) + " &b/offer 交易报价");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 60) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 70) {
            hologramText.add(LevelUtil.getLevelTag(0, 70) + " &b/view 查看玩家档案");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 70) + " &c???");
        }
        if (profile.getPrestige() > 0 || profile.getLevel() >= 120) {
            hologramText.add(LevelUtil.getLevelTag(0, 120) + " &b精通");
        } else {
            hologramText.add(LevelUtil.getLevelTag(0, 120) + " &c???");
        }
        hologramText.add("&7提升等级以解锁更多游戏内容...");
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
        return ThePit.getInstance().getPitConfig().getHelperHologramLocation();
    }
}

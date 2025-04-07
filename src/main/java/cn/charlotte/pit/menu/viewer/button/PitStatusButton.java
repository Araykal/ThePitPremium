package cn.charlotte.pit.menu.viewer.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.rank.RankUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 16:04
 */
public class PitStatusButton extends Button {
    private final PlayerProfile profile;

    public PitStatusButton(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lores = new ArrayList<>();
        lores.add("&7硬币: &6" + new DecimalFormat("0.00").format(profile.getCoins()));
        int prestige = profile.getPrestige();
        double experience = profile.getExperience();
        for (int i = 0; i < prestige; i++) {
            experience = experience + LevelUtil.getLevelTotalExperience(i, 120);
        }
        lores.add("&7总经验值: &b" + StringUtil.getFormatLong(new Double(experience).longValue()));
        lores.add(" ");
        lores.add("&7总击杀: &c" + profile.getKills());
        if (Bukkit.getOfflinePlayer(profile.getPlayerUuid()).isOnline()) {
            lores.add("&7总游玩时间: &a" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getLastLoginTime() + profile.getTotalPlayedTime()));
            lores.add("&7最后登入: &c" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getLastLoginTime()) + "前");
        } else {
            lores.add("&7总游玩时间: &a" + TimeUtil.millisToRoundedTime(profile.getTotalPlayedTime()));
            lores.add("&7最后登出: &c" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getLastLogoutTime()) + "前");
        }
        if (profile.getPrestige() > 0) {
            lores.add(" ");
            lores.add("&7声望: &e" + profile.getRenown());
        }
        String name = RankUtil.getPlayerRealColoredName(profile.getPlayerUuid());
        if (!profile.getPlayerOption().isProfileVisibility() && !PlayerUtil.isStaff(player)) {
            lores.clear();
            lores.add("&c此玩家隐藏了他的档案信息.");
            return new ItemBuilder(Material.NAME_TAG).name(" ").lore(lores).build();
        }
        return new ItemBuilder(Material.NAME_TAG).name(LevelUtil.getLevelTagWithRoman(profile.getPrestige(), profile.getLevel()) + " " + name + (profile.isBanned() ? " &c&l封禁中" : "")).lore(lores).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

package cn.charlotte.pit.menu.status.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/3 18:15
 */
public class PerformanceStatusButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        int prestige = profile.getPrestige();
        double experience = profile.getExperience();
        for (int i = 0; i < prestige; i++) {
            experience = experience + LevelUtil.getLevelTotalExperience(i, 120);
        }
        List<String> lores = new ArrayList<>();
        lores.add("&7总经验值: &b" + StringUtil.getFormatLong(new Double(experience).longValue()));
        lores.add("&7注册时间: &d" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getRegisterTime()) + "前");
        if (TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getRegisterTime()).equalsIgnoreCase("55 年")) {
            lores.add("&7debug: &d" + profile.getRegisterTime());
        }
        lores.add("&7总游玩时间: &a" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - profile.getLastLoginTime() + profile.getTotalPlayedTime()));
        lores.add(" ");
        lores.add("&7击杀/死亡: &a" + (profile.getDeaths() == 0 ? profile.getKills() : new DecimalFormat("0.00").format((float) profile.getKills() / (float) profile.getDeaths())));
        lores.add("&7(击杀+助攻)/死亡: &a" + (profile.getDeaths() == 0 ? profile.getKills() + profile.getAssists() : new DecimalFormat("0.00").format((profile.getKills() + (float) profile.getAssists()) / (float) profile.getDeaths())));
        lores.add(" ");
        lores.add("&7造成伤害/受到伤害: &a" + (profile.getHurtDamage() == 0 ? profile.getTotalDamage() : new DecimalFormat("0.00").format((float) profile.getTotalDamage() / (float) profile.getHurtDamage())));
        lores.add("&7弓箭命中/弓箭射出: &a" + (profile.getShootAttack() == 0 ? 0 : new DecimalFormat("0.00").format((float) profile.getBowHit() / (float) profile.getShootAttack())));
        lores.add(" ");
        return new ItemBuilder(Material.WHEAT).name("&e个人表现数据").lore(lores).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}

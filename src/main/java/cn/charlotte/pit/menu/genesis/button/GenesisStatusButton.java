package cn.charlotte.pit.menu.genesis.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/4 11:40
 */

public class GenesisStatusButton extends Button {

    private final GenesisTeam displayTeam;
    private final PlayerProfile profile;
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public GenesisStatusButton(GenesisTeam displayTeam, PlayerProfile profile) {
        this.displayTeam = displayTeam;
        this.profile = profile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        String teamColor = "&f";
        Material material = Material.AIR;
        if (displayTeam == GenesisTeam.ANGEL) {
            teamColor = "&b";
            material = Material.QUARTZ_BLOCK;
        }
        if (displayTeam == GenesisTeam.DEMON) {
            teamColor = "&c";
            material = Material.NETHERRACK;
        }
        lines.add("&8阵营");
        lines.add(" ");
        if (profile.getGenesisData().getTeam() == GenesisTeam.NONE) {
            lines.add("&7向此阵营宣誓你的忠诚.");
            lines.add("&7参与阵营活动,解锁");
            lines.add("&7专属活动奖励!");
            lines.add(" ");
            lines.add(teamColor + "点击加入此阵营!");
        } else if (profile.getGenesisData().getTeam() == displayTeam) {
            lines.add("&7你已经向此阵营宣誓了你的忠诚.");
            lines.add(" ");
            lines.add("&7你的阵营点数: " + teamColor + profile.getGenesisData().getPoints() + " 活动点数");
            lines.add(" ");
            lines.add(teamColor + "1 对立阵营玩家击杀 = 2 活动点数");
            lines.add(teamColor + "1 相同阵营玩家击杀 = 1 活动点数");
            lines.add(" ");
            if (ThePit.getInstance().getPitConfig().isGenesisEnable()) {
                lines.add("&7本期活动结束时间: &f" + df.format(new Date(ThePit.getInstance().getPitConfig().getGenesisEndTime())));
                lines.add("&7活动点数与奖励进度(不包括永久加成)会在活动结束后重置!");
            }
        } else if (profile.getGenesisData().getPoints() < 500) {
            lines.add("&7你已经向敌方阵营宣誓了你的忠诚,");
            lines.add("&7但你的活动点数高于500前,");
            lines.add("&7你仍有叛变的机会.");
            lines.add(" ");
            lines.add(teamColor + "叛变会重置你的点数");
            lines.add(teamColor + "以及奖励进度.");
            lines.add(" ");
            lines.add("&e点击叛变至当前阵营!");
        } else {
            lines.add("&7你已经向敌方阵营宣誓了你的忠诚.");
            lines.add("&7这里不属于你.");
        }
        return new ItemBuilder(material).name(teamColor + "宣誓效忠").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (profile.getGenesisData().getTeam() != GenesisTeam.NONE && profile.getGenesisData().getTeam() != displayTeam && profile.getGenesisData().getPoints() <= 500) {
            profile.getGenesisData().setTeam(displayTeam);
            profile.getGenesisData().setPoints(0);
            profile.getGenesisData().setTier(0);
            player.sendMessage(CC.translate("&a&l切换阵营! &7成功切换阵营."));
            return;
        }
        if (profile.getGenesisData().getTeam() == GenesisTeam.NONE) {
            profile.getGenesisData().setTeam(displayTeam);
            player.sendMessage(CC.translate("&a&l加入阵营! &7成功加入阵营."));
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

package cn.charlotte.pit.listener;


import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.parm.AutoRegister;

import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.rank.RankUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 11:31
 */
@AutoRegister
public class ChatListener implements Listener {

    public static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() && !PlayerUtil.isStaff(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&c慢速模式已开启,再次发送聊天信息前请等待3秒!"));
            player.sendMessage(CC.translate("&c请注意,短时间内重复发送相同聊天信息会被禁止发言!"));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        String tag = profile.getFormattedLevelTagWithRoman();
        if (ThePit.getInstance().getPitConfig().isGenesisEnable()) {
            if (profile.getGenesisData().getTeam() == GenesisTeam.ANGEL) {
                tag = "&b♆ " + tag;
            }
            if (profile.getGenesisData().getTeam() == GenesisTeam.DEMON) {
                tag = "&c♨ " + tag;
            }
        }

        String rank = RankUtil.getPlayerRank(player.getName());

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!CC.canPlayerSeeMessage(p, MessageType.CHAT)) {
                event.getRecipients().remove(p);
                if (p.getName().equals(player.getName())) {
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&c你关闭了聊天消息显示,因此你也无法发送聊天消息!"));
                    player.sendMessage(CC.translate("&c使用指令 &f/settings &c调整游戏选项."));
                }
            }
        }

        profile.setLastActionTimestamp(System.currentTimeMillis());

        cooldown.put(player.getUniqueId(), new Cooldown(3, TimeUnit.SECONDS));
        if (NewConfiguration.INSTANCE.getCustomChatFormatEnable()) {
            event.setFormat(CC.translate(PlaceholderAPI
                    .setPlaceholders(player, NewConfiguration.INSTANCE.getCustomChatFormat()
                            .replace("%s", "$s"))
                    .replace("$s", "%s")
            ));
        } else {
            if ("&7".equalsIgnoreCase(rank)) {
                event.setFormat(CC.translate(tag
                        + (profile.isSupporter() && profile.getPlayerOption().isSupporterStarDisplay() && !profile.isNicked() ? " &e✬ " : " ")
                        + rank
                        + RankUtil.getPlayerRankColor(player.getName()) + "%s: "
                        + (RankUtil.getPlayerRankColor(player.getUniqueId()).equalsIgnoreCase(CC.translate("&7")) ? "&7" : "&f")
                        + (player.hasPermission("thepit.admin") ? CC.translate("%s") : "%s")));
            } else {
                event.setFormat(CC.translate(tag
                        + (profile.isSupporter() && profile.getPlayerOption().isSupporterStarDisplay() && !profile.isNicked() ? " &e✬ " : " ")
                        + rank
                        + " "
                        + RankUtil.getPlayerRankColor(player.getName()) + "%s: "
                        + (RankUtil.getPlayerRankColor(player.getUniqueId()).equalsIgnoreCase(CC.translate("&7")) ? "&7" : "&f")
                        + (player.hasPermission("thepit.admin") ? CC.translate("%s") : "%s")));
            }
        }
    }

}

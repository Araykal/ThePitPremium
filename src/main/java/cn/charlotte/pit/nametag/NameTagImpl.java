package cn.charlotte.pit.nametag;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.events.impl.major.HamburgerEvent;
import cn.charlotte.pit.events.impl.major.RedVSBlueEvent;
import cn.charlotte.pit.events.impl.major.SpireEvent;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.nametag.BufferedNametag;
import cn.charlotte.pit.util.nametag.NametagAdapter;
import cn.charlotte.pit.util.rank.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 15:56
 */
public class NameTagImpl implements NametagAdapter {
    @Override
    public List<BufferedNametag> getPlate(Player player) {
        List<BufferedNametag> tags = new ArrayList<>();
        List<PlayerProfile> profiles = new ArrayList<>();

        for (Player target : Bukkit.getOnlinePlayers()) {
            profiles.add(PlayerProfile.getPlayerProfileByUuid(target.getUniqueId()));
        }

        if (ThePit.getInstance().getEventFactory() == null) {
            return tags;
        }

        String activeEpicEventName = ThePit.getInstance().getEventFactory().getActiveEpicEventName();
        profiles.sort((profile1, profile2) -> {
            int priority1 = profile1.isNicked() ? profile1.getNickPrestige() + 1000 * profile1.getNickLevel() : profile1.getPrestige() + 1000 * profile1.getLevel();
            int priority2 = profile2.isNicked() ? profile2.getNickPrestige() + 1000 * profile2.getNickLevel() : profile2.getPrestige() + 1000 * profile2.getLevel();

            if ("red_vs_blue".equals(activeEpicEventName)) {
                RedVSBlueEvent activeEpicEvent = (RedVSBlueEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent();
                if (activeEpicEvent.isRedTeam(profile1.getPlayerUuid())) {
                    priority1 += 10000000;
                }
                if (activeEpicEvent.isRedTeam(profile2.getPlayerUuid())) {
                    priority2 += 10000000;
                }
            }

            if ("spire".equals(activeEpicEventName)) {
                final SpireEvent event = (SpireEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent();
                priority1 += event.getRank(Bukkit.getPlayer(profile1.getPlayerUuid())) * 10000000;
                priority2 += event.getRank(Bukkit.getPlayer(profile2.getPlayerUuid())) * 10000000;
            }
            return Integer.compare(priority1, priority2);
        });
        int i = 200;
        for (PlayerProfile profile : profiles) {
            String displayName;
            if ("spire".equals(activeEpicEventName)) {
                final SpireEvent event = (SpireEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent();
                final Player target = Bukkit.getPlayer(profile.getPlayerUuid());
                final int rank = event.getRank(target);
                if (rank == -1) {
                    displayName = CC.translate("&7[???&7] " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                } else {
                    displayName = CC.translate("&7[&e#" + rank + "&7] " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                }
            } else if ("red_vs_blue".equals(activeEpicEventName)) {
                RedVSBlueEvent activeEpicEvent = (RedVSBlueEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent();
                displayName = activeEpicEvent.isRedTeam(profile.getPlayerUuid()) ? CC.translate(profile.getFormattedLevelTag() + " &c") : CC.translate(profile.getFormattedLevelTag() + " &9");
            } else {
                displayName = CC.translate(profile.getFormattedLevelTag() + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                if (profile.getChosePerk().get(5) != null) {
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("over_drive") && profile.getStreakKills() >= 50) {
                        displayName = CC.translate("&e&l超速传动" + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("high_lander") && profile.getStreakKills() >= 50) {
                        displayName = CC.translate("&6&l尊贵血统" + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("beast_mode_mega_streak") && profile.getStreakKills() >= 50) {
                        displayName = CC.translate("&a&l野兽模式" + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("hermit") && profile.getStreakKills() >= 50) {
                        displayName = CC.translate("&9&l隐士" + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("uber_streak") && profile.getStreakKills() >= 100) {
                        final int level = Math.min(400, (((int) profile.getStreakKills()) / 100) * 100);
                        displayName = CC.translate("&d&l登峰造极" + level + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                    if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase("to_the_moon") && profile.getStreakKills() >= 100) {
                        displayName = CC.translate("&b&l月球之旅" + " " + RankUtil.getPlayerRankColor(profile.getPlayerUuid()));
                    }
                }
            }
            StringBuilder suffix = new StringBuilder();
            if ("spire".equals(activeEpicEventName)) {
                final SpireEvent event = (SpireEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent();
                final String displayFloor = event.getDisplayFloor(profile.getPlayerUuid());
                suffix.append(" ").append(displayFloor);

            } else if ("ham".equals(activeEpicEventName)) {
                final HamburgerEvent event = (HamburgerEvent) ThePit.getInstance()
                        .getEventFactory()
                        .getActiveEpicEvent();
                final HamburgerEvent.PizzaData data = event.getPizzaDataMap().get(profile.getPlayerUuid());
                if (data != null) {
                    suffix.append(" &c")
                            .append(data.getHamburger())
                            .append("ஐ &6")
                            .append(data.getMoney())
                            .append("$");
                }
            } else {
                if (profile.isSupporter() && profile.getPlayerOption().isSupporterStarDisplay() && !profile.isNicked()) {
                    suffix.append(" &e✬");
                }
                if (ThePit.getInstance().getPitConfig().isGenesisEnable()) {
                    if (profile.getGenesisData().getTeam() == GenesisTeam.ANGEL) {
                        //♆♨
                        suffix.append(" &b");
                    }
                    if (profile.getGenesisData().getTeam() == GenesisTeam.DEMON) {
                        //♨
                        suffix.append(" &c");
                    }
                    if (profile.getGenesisData().getTeam() == GenesisTeam.NONE) {
                        suffix.append(" &6");
                    }
                    if (profile.getBounty() != 0) {
                        suffix.append("&l").append(profile.getBounty()).append("g");
                    } else {
                        if (profile.getGenesisData().getTeam() == GenesisTeam.ANGEL) {
                            suffix.append("♆");
                        }
                        if (profile.getGenesisData().getTeam() == GenesisTeam.DEMON) {
                            suffix.append("♨");
                        }
                    }
                } else if (profile.getBounty() != 0) {
                    suffix.append("&6&l ").append(profile.getBounty()).append("g");
                } else {
                    suffix.append(" ");
                }
            }

            tags.add(new BufferedNametag(
                    i + "",
                    //&7 refers to Prefix
                    displayName,
                    CC.translate(suffix.toString()),
                    false,
                    Bukkit.getPlayer(profile.getPlayerUuid())));
            i--;

        }

        return tags;
    }

    @Override
    public boolean showHealthBelowName(Player player) {
        return ThePit.getInstance().getEventFactory() != null && "spire".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName());
    }
}

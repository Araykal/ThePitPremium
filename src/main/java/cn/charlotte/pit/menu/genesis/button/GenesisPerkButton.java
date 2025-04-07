package cn.charlotte.pit.menu.genesis.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.type.AngelChestplate;
import cn.charlotte.pit.item.type.ArmageddonBoots;
import cn.charlotte.pit.medal.impl.challenge.GenesisTierMedal;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/4 11:37
 */

public class GenesisPerkButton extends Button {

    private final GenesisTeam displayTeam;
    private final PlayerProfile profile;
    private final int tier;

    public GenesisPerkButton(GenesisTeam displayTeam, PlayerProfile profile, int tier) {
        this.displayTeam = displayTeam;
        this.profile = profile;
        this.tier = tier;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        HashMap<Integer, String> tierDescription = new HashMap<>();
        HashMap<Integer, Integer> tierRequirement = new HashMap<>();
        String teamColor = "&f";
        int teamUnlockedDurability = 0;
        int teamLockedDurability = 0;
        int durability = 0;
        if (displayTeam == GenesisTeam.ANGEL) {
            teamColor = "&b";
            teamUnlockedDurability = 3;
        }
        if (displayTeam == GenesisTeam.DEMON) {
            teamColor = "&c";
            teamLockedDurability = 12;
            teamUnlockedDurability = 14;
        }
        //tierDescription.put(1, "&7激活效果: &e天地之印");
        tierDescription.put(1, "&7对不同阵营的目标造成的基础伤害 &c+1❤");
        tierRequirement.put(1, 30);

        tierDescription.put(2, "&7解锁功能: 挑战任务的重置等待时间 " + teamColor + "-20% &7."
                + "/s&7解锁功能: " + teamColor + "阵营出生点 &7.");
        tierRequirement.put(2, 100);

        if (displayTeam == GenesisTeam.ANGEL) {
            tierDescription.put(3, "&7商店内的 &b钻石类物品 &7的售价 &6-65% &7.");
        }
        if (displayTeam == GenesisTeam.DEMON) {
            tierDescription.put(3, "&7在 &d神话之井 &7进行附魔需要的硬币 &6-65% &7.");
        }
        tierRequirement.put(3, 250);

        if (displayTeam == GenesisTeam.ANGEL) {
            tierDescription.put(4, "&7攻击穿着 &6皮革装备 &7的玩家造成的基础伤害 &c+0.5❤ &7.");
        }
        if (displayTeam == GenesisTeam.DEMON) {
            tierDescription.put(4, "&7攻击穿着 &b钻石装备 &7的玩家造成的基础伤害 &c+0.5❤ &7.");
        }
        tierRequirement.put(4, 700);

        tierDescription.put(5, "&7击杀赏金的赏金获得量 &6+50% &7,大型事件中获得声望时获得量 &e+1 &7.");
        tierRequirement.put(5, 1500);

        if (displayTeam == GenesisTeam.ANGEL) {
            tierDescription.put(6, "&7获得活动限定物品: &f天使之甲"
                    + "/s&8<限定物品每期活动轮换>");
        } else {
            tierDescription.put(6, "&7获得活动限定物品: &c终末之靴"
                    + "/s&8<限定物品每期活动轮换>");
        }
        tierRequirement.put(6, 4000);

        if (displayTeam == GenesisTeam.ANGEL) {
            tierDescription.put(7, "&7每次升级:"
                    + "/s  &7获得永久加成: &7击杀/助攻获得的经验值 &b+1% &7(当前: &b+$boostTier%&7)"
                    + "/s  &8<可叠加,最高15层,每期活动只能升级一次>"
                    + "/s  &8<此增益在活动关闭期间仍然有效>");
        }
        if (displayTeam == GenesisTeam.DEMON) {
            tierDescription.put(7, "&7每次升级:"
                    + "/s  &7获得永久加成: &7击杀/助攻获得的硬币 &6+1% &7(当前: &6+$boostTier%&7)"
                    + "/s  &8<可叠加,最高15层,每期活动只能升级一次>"
                    + "/s  &8<此增益在活动关闭期间仍然有效>");
        }
        tierRequirement.put(7, 7000);
        List<String> lines = new ArrayList<>();
        lines.add("&8阵营奖励");
        lines.add(" ");
        String[] split = tierDescription.get(tier).split("/s");
        for (String s : split) {
            if (profile.getGenesisData().getTeam() != displayTeam) {
                lines.add("&7" + s.replace("$boostTier", "0"));
            } else {
                lines.add("&7" + s.replace("$boostTier", profile.getGenesisData().getBoostTier() + ""));
            }
        }
        lines.add(" ");
        if (profile.getGenesisData().getTeam() == displayTeam) {
            if (profile.getGenesisData().getPoints() < tierRequirement.get(tier)) {
                durability = teamLockedDurability;
                lines.add(teamColor + "所需点数不足 (" + profile.getGenesisData().getPoints() + "/" + tierRequirement.get(tier) + ")");
            } else {
                if (profile.getGenesisData().getTier() >= tier) {
                    durability = teamUnlockedDurability;
                    lines.add("&a此奖励已解锁!");
                } else if (profile.getGenesisData().getTier() == tier - 1) {
                    durability = teamLockedDurability;
                    if (tier == 7) {
                        lines.add("&a点击升级!");
                    } else {
                        lines.add("&a点击解锁!");
                    }
                } else {
                    durability = teamLockedDurability;
                    lines.add(teamColor + "请先解锁上一阶段奖励... (" + profile.getGenesisData().getPoints() + "/" + tierRequirement.get(tier) + ")");
                }
            }
        } else if (profile.getGenesisData().getTeam() == GenesisTeam.NONE) {
            durability = teamLockedDurability;
            lines.add(teamColor + "加入此阵营后解锁...");
        } else {
            durability = teamLockedDurability;
            lines.add(teamColor + "你处于对立阵营中!");
        }
        return new ItemBuilder(Material.STAINED_CLAY).name(teamColor + "阵营奖励 - 阶段 " + RomanUtil.convert(tier)).lore(lines).durability(durability).amount(tier).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (!ThePit.getInstance().getPitConfig().isGenesisEnable() && !ThePit.isDEBUG_SERVER() && !PlayerUtil.isStaff(player))
            return;
        if (tier > 7) return;
        HashMap<Integer, Integer> tierRequirement = new HashMap<>();
        tierRequirement.put(1, 30);
        tierRequirement.put(2, 100);
        tierRequirement.put(3, 250);
        tierRequirement.put(4, 700);
        tierRequirement.put(5, 1500);
        tierRequirement.put(6, 4000);
        tierRequirement.put(7, 7000);
        if (profile.getGenesisData().getTeam() == displayTeam) {
            if (profile.getGenesisData().getPoints() >= tierRequirement.get(tier)) {
                if (profile.getGenesisData().getTier() == tier - 1) {
                    profile.getGenesisData().setTier(tier);
                    if (tier == 7) {
                        profile.getGenesisData().setTier(7);
                        profile.getGenesisData().setBoostTier(Math.min(15, profile.getGenesisData().getBoostTier() + 1));
                        if (profile.getGenesisData().getBoostTier() >= 2) {
                            new GenesisTierMedal().addProgress(profile, 1);
                        }
                    }
                    if (tier == 6) {
                        if (InventoryUtil.isInvFull(player)) {
                            profile.getGenesisData().setTier(tier - 1);
                            player.sendMessage(CC.translate("你的背包已满,因此解锁请求被撤回."));
                            return;
                        }
                        final IMythicItem item;
                        if (displayTeam == GenesisTeam.ANGEL) {
                            item = new AngelChestplate();
                        } else if (displayTeam == GenesisTeam.DEMON) {
                            item = new ArmageddonBoots();
                        } else {
                            return;
                        }
                        player.getInventory().addItem(item.toItemStack());
                    }
                    player.sendMessage(CC.translate("&a解锁成功!"));
                }
            }
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

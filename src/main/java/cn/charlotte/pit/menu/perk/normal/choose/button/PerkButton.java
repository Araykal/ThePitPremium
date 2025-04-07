package cn.charlotte.pit.menu.perk.normal.choose.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.menu.perk.normal.buy.PerkBuyMenu;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:47
 */
public class PerkButton extends Button {
    private final int page;

    public PerkButton(int page) {
        this.page = page;
    }

    //<0的page为加成page 0~4为perk 5->megaStreak 6~8 killStreak
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PerkData data = profile.getChosePerk().get(page);
        if (page == 2) {
            if (profile.getLevel() < 35) {
                return new ItemBuilder(Material.BEDROCK)
                        .name("&c天赋栏 #" + page)
                        .lore("&7解锁等级: " + LevelUtil.getLevelTag(profile.getPrestige(), 35))
                        .amount(page)
                        .build();
            }
        }
        if (page == 3) {
            if (profile.getLevel() < 70) {
                return new ItemBuilder(Material.BEDROCK)
                        .name("&c天赋栏 #" + page)
                        .lore("&7解锁等级: " + LevelUtil.getLevelTag(profile.getPrestige(), 70))
                        .amount(page)
                        .build();
            }
        }
        if (page == 4) {
            if (profile.getLevel() < 100) {
                return new ItemBuilder(Material.BEDROCK)
                        .name("&c天赋栏 #" + page)
                        .lore("&7解锁等级: " + LevelUtil.getLevelTag(profile.getPrestige(), 100))
                        .amount(page)
                        .build();
            }
        }
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase("KeepSilence")) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c天赋栏 #" + page)
                    .lore("&7你正在进行一个挑战任务,", "&7因此天赋栏处于不可用状态!")
                    .amount(page)
                    .build();
        }
        if (data == null) {
            String type = (page > 4 ? (page > 5 ? "连杀" : "超级连杀") : "") + "天赋";
            int slot = page;
            if (page == 5) {
                slot = 1;
            }
            if (page > 5) {
                slot = page - 5;
            }
            return new ItemBuilder(page > 4 ? Material.GOLD_BLOCK : Material.DIAMOND_BLOCK)
                    .name("&e" + type + "栏 " + (page != 5 ? "#" + slot : ""))
                    .lore("&7选择一个" + type + "来填充", "&7这个" + type + "栏.", " ", "&e点击选择一个" + type + "!")
                    .amount(slot)
                    .build();
        }

        Optional<AbstractPerk> first = ThePit.getInstance()
                .getPerkFactory()
                .getPerks()
                .stream()
                .filter(perk -> perk.getInternalPerkName().equals(data.getPerkInternalName()))
                .findFirst();

        if (first.isPresent()) {
            AbstractPerk perk = first.get();
            List<String> lines = new ArrayList<>();
            lines.add("&7选定" + perk.getPerkType().getDisplayName() + ": &a" + perk.getDisplayName());
            lines.add(" ");
            lines.addAll(perk.getDescription(player));
            lines.add(" ");
            lines.add("&e点击更换" + perk.getPerkType().getDisplayName() + "!");

            String type = (page > 4 ? (page > 5 ? "连杀" : "超级连杀") : "") + "天赋";
            int slot = page;
            if (page == 5) {
                slot = 1;
            }
            if (page > 5) {
                slot = page - 5;
            }

            return perk.getIconWithNameAndLore("&e" + type + "栏 " + (page != 5 ? "#" + slot : ""), lines, 0, 1);
        } else {
            return new ItemBuilder(Material.BEDROCK)
                    .name("&c&l错误!")
                    .lore("&c加载此天赋栏失败,", "&c请联系管理员反馈此问题.")
                    .amount(page)
                    .build();
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (page == 2) {
            if (profile.getLevel() < 35) {
                return;
            }
        }
        if (page == 3) {
            if (profile.getLevel() < 70) {
                return;
            }
        }
        if (page == 4) {
            if (profile.getLevel() < 100) {
                return;
            }
        }
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase("KeepSilence")) {
            player.sendMessage(CC.translate("&c你有一个正在进行的任务! 如需解除不可用状态,请在任务NPC处放弃任务."));
            return;
        }
        PerkType perkType = null;
        if (page < 5) {
            perkType = PerkType.PERK;
        }
        if (page == 5) {
            perkType = PerkType.MEGA_STREAK;
        }
        if (page > 5) {
            perkType = PerkType.KILL_STREAK;
        }
        new PerkBuyMenu(page, perkType).openMenu(player);
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

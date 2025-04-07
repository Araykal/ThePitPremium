package cn.charlotte.pit.menu.quest.sanctuary.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/4 18:45
 */
public class PermanentBoostButton extends Button {
    private final AbstractPerk perk;
    private final int price;

    public PermanentBoostButton(AbstractPerk perk, int price) {
        this.perk = perk;
        this.price = price;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        List<String> lore = new ArrayList<>();


        PerkData data = profile.getUnlockedPerkMap().get(perk.getInternalPerkName());

        if (data != null) {
            if (data.getLevel() < 0) {
                data.setLevel(1);
            }
            if (perk.getMaxLevel() > 1) {
                lore.add("&7当前等级: &b" + RomanUtil.convert(data.getLevel()));
            }
        }
        if (perk.requireLevel() != 0) {
            lore.add("&7解锁等级: " + LevelUtil.getLevelTag(profile.getPrestige(), perk.requireLevel()));
            lore.add("&7价格: &6" + perk.requireCoins() + " 硬币");
            lore.add(" ");
            lore.add("&e" + perk.getDisplayName());
            lore.addAll(perk.getDescription(player));
            lore.add(" ");
            lore.add("&7&o这是一个天赋解锁类天赋,");
            lore.add("&7&o解锁该任务加成天赋相当于解锁其普通天赋的购买权,");
            lore.add("&7&o仍然需要达到等级要求后花费硬币购买&装备后才能生效.");
        } else {
            lore.addAll(perk.getDescription(player));
        }
        lore.add(" ");
        String color;
        if (data != null) {
            if (data.getLevel() >= perk.getMaxLevel()) {
                lore.add("&a此任务加成天赋已被提升至最大等级!");
                color = "&a";
            } else {
                lore.add("&7价格: &e" + price + " 行动赏金");
                if (price > profile.getActionBounty()) {
                    lore.add("&c你没有足够的行动赏金!");
                    color = "&c";
                } else {
                    lore.add("&e点击升级此任务加成天赋!");
                    color = "&e";
                }
            }
        } else {
            lore.add("&7价格: &e" + price + " 行动赏金");
            if (price > profile.getActionBounty()) {
                lore.add("&c你没有足够的行动赏金!");
                color = "&c";
            } else {
                lore.add("&e点击解锁此任务加成天赋!");
                color = "&e";
            }
        }
        if (perk.requireLevel() != 0) {
            return perk.getIconWithNameAndLore(color + "天赋解锁: " + perk.getDisplayName(), lore, 0, 1);
        } else {
            return perk.getIconWithNameAndLore(color + perk.getDisplayName(), lore, 0, 1);
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        PerkData data = profile.getUnlockedPerkMap().get(perk.getInternalPerkName());

        if (data != null) {
            if ((perk.getMaxLevel() > data.getLevel())) {
                if (price > profile.getActionBounty()) {
                    player.sendMessage(CC.translate("&c你没有足够的行动赏金!"));
                } else {
                    profile.setActionBounty(profile.getActionBounty() - price);
                    data.setLevel(data.getLevel() + 1);
                    player.sendMessage(CC.translate("&a&l天赋升级! &7你升级了任务加成天赋 &e" + perk.getDisplayName() + " &7至等级 &b" + RomanUtil.convert(data.getLevel()) + " &7."));
                }
            }
        } else {
            if (price > profile.getActionBounty()) {
                player.sendMessage(CC.translate("&c你没有足够的行动赏金!"));
            } else {
                profile.setActionBounty(profile.getActionBounty() - price);
                PerkData perkData = new PerkData();
                perkData.setPerkInternalName(perk.getInternalPerkName());
                perkData.setLevel(1);

                profile.getUnlockedPerkMap().put(perkData.getPerkInternalName(), perkData);
                player.sendMessage(CC.translate("&a&l天赋解锁! &7你解锁了任务加成天赋 &e" + perk.getDisplayName() + " &7."));
            }
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

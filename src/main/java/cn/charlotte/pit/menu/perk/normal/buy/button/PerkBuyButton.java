package cn.charlotte.pit.menu.perk.normal.buy.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.menu.perk.UnKnowButton;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 23:10
 */
public class PerkBuyButton extends Button {
    private final AbstractPerk perk;
    private final int page;
    private final PerkType perkType;

    public PerkBuyButton(AbstractPerk perk, int page, PerkType perkType) {
        this.perk = perk;
        this.page = page;
        this.perkType = perkType;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        DecimalFormat df = new DecimalFormat(",###,###,###,###");
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        boolean special = false;
        //精通天赋 or SpecialPerk
        if (perk.requirePrestige() > 0 || special || perk.getPerkType() == PerkType.KILL_STREAK || perk.getPerkType() == PerkType.MEGA_STREAK) {
            boolean unlocked = false;
            if (perk.getPerkType() == PerkType.PERK) {
                PerkData data = profile.getUnlockedPerkMap().get(perk.getInternalPerkName());
                if (data != null) {
                    unlocked = true;
                }
            }
            //killStreak purchase check
            if (perk.getPerkType() == PerkType.KILL_STREAK || perk.getPerkType() == PerkType.MEGA_STREAK) {
                unlocked = true;
                //highlander
                if (perk.getInternalPerkName().equalsIgnoreCase("high_lander") || perk.getInternalPerkName().equalsIgnoreCase("khanate_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("gold_nano_factory_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("wither_craft_kill_streak")) {
                    unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "highlander_bundle");
                } else if (perk.getInternalPerkName().equalsIgnoreCase("grand_finale") || perk.getInternalPerkName().equalsIgnoreCase("leech_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("assured_strike_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("rn_gesus_kill_streak")) {
                    unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "grand_finale_bundle");
                    //grand finale
                } else if (perk.getInternalPerkName().equalsIgnoreCase("monster") || perk.getInternalPerkName().equalsIgnoreCase("r_and_r") || perk.getInternalPerkName().equalsIgnoreCase("tough_skin") || perk.getInternalPerkName().equalsIgnoreCase("tactical_retreat") || perk.getInternalPerkName().equalsIgnoreCase("beast_mode_mega_streak")) {
                    unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "beast_mode_bundle");
                } else if (perk.getInternalPerkName().equalsIgnoreCase("hermit") || perk.getInternalPerkName().equalsIgnoreCase("pungent_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("aura_of_protection_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("glass_pickaxe_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("ice_cube_kill_streak")) {
                    unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "hermit_bundle");
                    //hermit
                } else if (perk.getInternalPerkName().equalsIgnoreCase("to_the_moon") || perk.getInternalPerkName().equalsIgnoreCase("super_streaker") || perk.getInternalPerkName().equalsIgnoreCase("xp_stack") || perk.getInternalPerkName().equalsIgnoreCase("gold_stack")) {
                    unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "to_the_moon_bundle");
                }
            }
            //if (未解锁 &&                       精通大于要求)                   ||
            if (!unlocked && ((perk.requirePrestige() <= profile.getPrestige()) || special)) {
                List<String> lore = new ArrayList<>(perk.getDescription(player));
                lore.add(" ");
                lore.add("&7价格: &6" + df.format(perk.requireCoins()) + "硬币");
                if (perk.requireLevel() > profile.getLevel()) {
                    lore.add("&c等级不足: " + LevelUtil.getLevelTag(Math.max(profile.getPrestige(), perk.requirePrestige()), perk.requireLevel()));
                }
                if (special) {
                    lore.add("&c此%type%只能通过特殊获取方式获得!".replace("%type%", perkType.getDisplayName()));
                } else {
                    lore.add("&c请先在精通商店中解锁此%type%的购买权!".replace("%type%", perkType.getDisplayName()));
                }
                if (special) {
                    return new ItemBuilder(Material.BEDROCK).name("&c" + perk.getDisplayName()).lore(lore).shiny().build();
                } else {
                    return new ItemBuilder(Material.BEDROCK).name("&c" + perk.getDisplayName()).lore(lore).build();
                }
            }
        }

        if ((perk.getPerkType() == PerkType.PERK && profile.getLevel() < perk.requireLevel() && !PlayerUtil.isPlayerUnlockedPerk(player, "the_way_perk")) || (perk.requirePrestige() > 0 && profile.getPrestige() < perk.requirePrestige())) {
            return new UnKnowButton().getButtonItem(player);
        }


        List<String> lore = new ArrayList<>(perk.getDescription(player));
        lore.add("");
        lore.add("&7价格: &6" + df.format(perk.requireCoins()) + " 硬币");
        if (perk.requireLevel() > profile.getLevel()) {
            if (!PlayerUtil.isPlayerUnlockedPerk(player, "the_way_perk")) {
                lore.add("&c等级不足: " + LevelUtil.getLevelTag(profile.getPrestige(), perk.requireLevel()));
                return new ItemBuilder(Material.BEDROCK)
                        .name("&c" + perk.getDisplayName())
                        .lore(lore)
                        .build();
            } else {
                if (perk.getPerkType() != PerkType.PERK) {
                    lore.add("&c等级不足: " + LevelUtil.getLevelTag(profile.getPrestige(), perk.requireLevel()));
                    return new ItemBuilder(Material.BEDROCK)
                            .name("&c" + perk.getDisplayName())
                            .lore(lore)
                            .build();
                }
            }
        }
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPerkInternalName().equals(perk.getInternalPerkName())) {
                if (page == entry.getKey()) {
                    lore.add("&c你已经装备了此%type%!".replace("%type%", perkType.getDisplayName()));
                } else {
                    lore.add("&c你在其他栏位装备了本%type%!".replace("%type%", perkType.getDisplayName()));
                }
                return perk.getIconWithNameAndLore("&c" + perk.getDisplayName(), lore, 0, 1);
            }
        }


        PerkData data = profile.getBoughtPerkMap().get(perk.getInternalPerkName());

        if (data != null) {
            if (profile.getChosePerk().get(page) != null) {
                lore.add("&a点击更换该%type%!".replace("%type%", perkType.getDisplayName()));
            } else {
                lore.add("&a点击使用该%type%!".replace("%type%", perkType.getDisplayName()));
            }
        } else {
            if (perk.requireLevel() > profile.getLevel()) {
                if (!PlayerUtil.isPlayerUnlockedPerk(player, "the_way_perk")) {
                    lore.add("&c等级不足: " + LevelUtil.getLevelTag(profile.getPrestige(), perk.requireLevel()));
                    return new ItemBuilder(Material.BEDROCK)
                            .name("&c" + perk.getDisplayName())
                            .lore(lore)
                            .build();
                } else {
                    if (perk.getPerkType() != PerkType.PERK) {
                        lore.add("&c等级不足: " + LevelUtil.getLevelTag(profile.getPrestige(), perk.requireLevel()));
                        return new ItemBuilder(Material.BEDROCK)
                                .name("&c" + perk.getDisplayName())
                                .lore(lore)
                                .build();
                    }
                }
            }
            if (profile.getCoins() < perk.requireCoins()) {
                lore.add("&c硬币不足!");
            } else {
                if (profile.getChosePerk().get(page) != null) {
                    lore.add("&e点击购买并更换该%type%!".replace("%type%", perkType.getDisplayName()));
                } else {
                    lore.add("&e点击购买并使用该%type%!".replace("%type%", perkType.getDisplayName()));
                }
            }
            return perk.getIconWithNameAndLore("&e" + perk.getDisplayName(), lore, 0, 1);
        }
        return perk.getIconWithNameAndLore("&a" + perk.getDisplayName(), lore, 0, 1);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        /*
        if (perk.getPerkType() != PerkType.PERK && !player.hasPermission("thepit.admin")) {
            player.sendMessage(CC.translate("&c玩法开发中!"));
            return;
        }
         */

        //check perk prestige requirement
        if (perk.requirePrestige() > profile.getPrestige()) {
            return;
        }
        //check perk level requirement
        if (perk.requireLevel() > profile.getLevel()) {
            //check if perk type is PERK and player unlocked The Way
            if (!PlayerUtil.isPlayerUnlockedPerk(player, "the_way_perk")) {
                return;
            } else {
                if (perk.getPerkType() != PerkType.PERK) {
                    return;
                }
            }
        }
        //check if player has equipped this perk
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue() != null && entry.getValue().getPerkInternalName().equals(perk.getInternalPerkName())) {
                if (page == entry.getKey()) {
                    player.sendMessage(CC.translate("&c你已经装备了此%type%!".replace("%type%", perkType.getDisplayName())));
                } else {
                    player.sendMessage(CC.translate("&c你在其他栏位装备了本%type%!".replace("%type%", perkType.getDisplayName())));
                }
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                return;
            }
        }

        PerkData perkData1 = profile.getBoughtPerkMap().get(perk.getInternalPerkName());

        //check if player bought the perk
        if (perkData1 != null) {
            //inactive the old perk
            if (profile.getChosePerk().get(page) != null) {
                ThePit.getInstance().getPerkFactory().getPerks().stream().filter(perk -> perk.getInternalPerkName().equals(profile.getChosePerk().get(page).getPerkInternalName())).findFirst().ifPresent(abstractPerk -> abstractPerk.onPerkInactive(player));
            }
            profile.getChosePerk().put(page, new PerkData(perkData1.getPerkInternalName(), perkData1.getLevel()));
            player.sendMessage(CC.translate("&a&l装备成功! &7成功在 &f#" + page + " &7%type%栏装备%type% &a".replace("%type%", perkType.getDisplayName()) + perk.getDisplayName() + " &7."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            perk.onPerkActive(player);
        } else {
            if (profile.getPrestige() >= perk.requirePrestige()) {
                boolean special = false; //if a perk is limited
                if (special || perk.requirePrestige() > 0 || perk.getPerkType() == PerkType.KILL_STREAK || perk.getPerkType() == PerkType.MEGA_STREAK) {
                    boolean unlocked = false;
                    if (perk.getPerkType() == PerkType.PERK) {
                        PerkData data = profile.getUnlockedPerkMap().get(perk.getInternalPerkName());
                        if (data != null) {
                            unlocked = true;
                        }
                    }
                    //killStreak purchase check
                    if (perk.getPerkType() == PerkType.KILL_STREAK || perk.getPerkType() == PerkType.MEGA_STREAK) {
                        unlocked = true;
                        if (perk.getInternalPerkName().equalsIgnoreCase("high_lander") || perk.getInternalPerkName().equalsIgnoreCase("khanate_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("gold_nano_factory_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("wither_craft_kill_streak")) {
                            unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "highlander_bundle");
                            //grand finale
                        } else if (perk.getInternalPerkName().equalsIgnoreCase("grand_finale") || perk.getInternalPerkName().equalsIgnoreCase("leech_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("assured_strike_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("rn_gesus_kill_streak")) {
                            unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "grand_finale_bundle");
                        } else if (perk.getInternalPerkName().equalsIgnoreCase("monster") || perk.getInternalPerkName().equalsIgnoreCase("r_and_r") || perk.getInternalPerkName().equalsIgnoreCase("tough_skin") || perk.getInternalPerkName().equalsIgnoreCase("tactical_retreat") || perk.getInternalPerkName().equalsIgnoreCase("beast_mode_mega_streak")) {
                            unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "beast_mode_bundle");
                        } else if (perk.getInternalPerkName().equalsIgnoreCase("hermit") || perk.getInternalPerkName().equalsIgnoreCase("pungent_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("aura_of_protection_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("glass_pickaxe_kill_streak") || perk.getInternalPerkName().equalsIgnoreCase("ice_cube_kill_streak")) {
                            unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "hermit_bundle");
                            //hermit
                        } else if (perk.getInternalPerkName().equalsIgnoreCase("to_the_moon") || perk.getInternalPerkName().equalsIgnoreCase("super_streaker") || perk.getInternalPerkName().equalsIgnoreCase("xp_stack") || perk.getInternalPerkName().equalsIgnoreCase("gold_stack")) {
                            unlocked = PlayerUtil.isPlayerUnlockedPerk(player, "to_the_moon_bundle");
                        }
                    }
                    if (!unlocked) {
                        return;
                    }
                }
                if (profile.getCoins() < perk.requireCoins()) {
                    player.sendMessage(CC.translate("&c你的硬币不足!"));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                } else {
                    if (profile.getChosePerk().get(page) != null) {
                        ThePit.getInstance().getPerkFactory().getPerks().stream().filter(perk -> perk.getInternalPerkName().equals(profile.getChosePerk().get(page).getPerkInternalName())).findFirst().ifPresent(abstractPerk -> abstractPerk.onPerkInactive(player));
                    }
                    profile.setCoins(profile.getCoins() - perk.requireCoins());
                    PerkData perkData = new PerkData(perk.getInternalPerkName(), 0);
                    profile.getBoughtPerkMap().put(perkData.getPerkInternalName(), perkData);
                    profile.getChosePerk().put(page, perkData);
                    player.sendMessage(CC.translate("&a&l装备成功! &7成功在 &f#" + page + " &7%type%栏装备%type% &a".replace("%type%", perkType.getDisplayName()) + perk.getDisplayName() + " &7."));
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    perk.onPerkActive(player);
                }
            }
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}

package cn.charlotte.pit.enchantment.menu.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.EnchantmentRecord;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.menu.MythicWellMenu;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.event.PitPlayerEnchantEvent;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.item.AbstractPitItem;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.menu.shop.button.AbstractShopButton;
import cn.charlotte.pit.util.MythicUtil;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.ChatComponentBuilder;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.random.RandomUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ItemBow;
import net.minecraft.server.v1_8_R3.ItemSword;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/11 16:51
 */
public class EnchantButton extends Button {
    private static final Random random = new Random();
    private final ItemStack item;
    private final MythicWellMenu menu;

    public EnchantButton(ItemStack item, MythicWellMenu menu) {
        this.item = item;
        this.menu = menu;
    }

    private int getPrice(Player player, int level, MythicColor color) {
        int price;
        if (color == MythicColor.DARK) {
            switch (level) {
                case 1:
                    price = 10000;
                    break;
                case 2:
                    price = 60000;
                    break;
                default:
                    price = 99999;
                    break;
            }
        } else {
            switch (level) {
                case 1:
                    price = 1000;
                    break;
                case 2:
                    price = 4000;
                    break;
                case 3:
                    price = 8000;
                    break;
                default:
                    price = 9999;
                    break;
            }
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTeam() == GenesisTeam.DEMON && profile.getGenesisData().getTier() >= 3) {
            return (int) (0.35 * AbstractShopButton.getDiscountPrice(player, price));
        }
        return AbstractShopButton.getDiscountPrice(player, price);
    }

    public IMythicItem getMythicItem(ItemStack item) {
        return MythicUtil.getMythicItem(item);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        try {
            if (item == null || item.getType() == Material.AIR) {
                return getDefaultDisplayItem();
            }
            IMythicItem mythicItem;
            if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemSword) {
                mythicItem = new MythicSwordItem();
            } else if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemBow) {
                mythicItem = new MythicBowItem();
            } else {
                mythicItem = new MythicLeggingsItem();
            }

            mythicItem.loadFromItemStack(item);
            MythicColor color = MythicColor.valueOf(ItemUtil.getItemStringData(mythicItem.toItemStack(), "mythic_color").toUpperCase());
            int level = mythicItem.getTier();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            List<String> lines = new ArrayList<>();
            if (level < (color == MythicColor.DARK ? 2 : 3)) {
                lines.add("&7升级至: &a" + RomanUtil.convert(level + 1) + " 阶");
                lines.add("&7价格: &6" + getPrice(player, level + 1, color) + " 硬币" + (level == (color == MythicColor.DARK ? 1 : 2) ? " &7+ " + color.getChatColor() + color.getDisplayName() + "色神话之甲" : ""));
                lines.add(" ");
                if (profile.getCoins() >= getPrice(player, level + 1, color)) {
                    if (PlayerUtil.getPlayerUnlockedPerkLevel(player, "Mythicism") < 4 && level == 2) {
                        lines.add("&c天赋 &6神话附魔师 &c等级 &eIV &c后解锁此功能!");
                    } else {
                        String sinceItem = profile.getEnchantingScience();
                        ItemStack item = InventoryUtil.deserializeItemStack(sinceItem);
                        if ((item == null || item.getType() == Material.AIR) && level == 2) {
                            lines.add("&e选择背包内的神话之甲作为材料以继续...");
                        } else {
                            lines.add("&e点击进行附魔!");
                        }
                    }
                } else {
                    lines.add("&c你的硬币不足!");
                }
            } else {
                lines.add("&a此附魔物品已被提升至最大等级!");
            }
            if ((color == MythicColor.DARK || color == MythicColor.RAGE) && !PlayerUtil.isPlayerUnlockedPerk(player, "heresy_perk")) {
                if (!PlayerUtil.isPlayerUnlockedPerk(player, "heresy_perk")) {
                    lines.clear();
                    lines.add("&c清先解锁精通天赋 &6邪术 &c后重试!");
                }
                if (PlayerUtil.getPlayerUnlockedPerkLevel(player, "heresy_perk") < 3 && level == 1) {
                    lines.clear();
                    lines.add("&c天赋 &6邪术 &c等级 &eIII &c后解锁此功能!");
                }
            }
            return new ItemBuilder(Material.ENCHANTMENT_TABLE)
                    .name("&d神话之井")
                    .lore(lines)
                    .build();
        } catch (Exception e) {
            CC.printError(player, e);
        }
        return getDefaultDisplayItem();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        IMythicItem mythicItem = getMythicItem(item);

        if (mythicItem == null) return;

        final String mythicColor = ItemUtil.getItemStringData(item, "mythic_color");
        if (mythicColor == null) {
            return;
        }

        MythicColor color = MythicColor.valueOf(mythicColor.toUpperCase());
        int level = mythicItem.getTier();

        if ((color == MythicColor.DARK || color == MythicColor.RAGE) && !PlayerUtil.isPlayerUnlockedPerk(player, "heresy_perk")) {
            if (!PlayerUtil.isPlayerUnlockedPerk(player, "heresy_perk")) {
                return;
            }
            if (PlayerUtil.getPlayerUnlockedPerkLevel(player, "heresy_perk") < 3 && level == 1) {
                return;
            }
        }
        if (level >= (color == MythicColor.DARK ? 2 : 3)) {
            return;
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getCoins() < getPrice(player, level + 1, color)) {
            return;
        }
        if (level == (color == MythicColor.DARK ? 1 : 2)) {
            if (PlayerUtil.getPlayerUnlockedPerkLevel(player, "Mythicism") < 4) {
                return;
            }
            if (!removeMythicLegWithColor(player, color)) {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(CC.translate("&c请放入一条额外的 " + color.getChatColor() + color.getDisplayName() + "色神话之甲 &c才能附魔!"));
                return;
            }
        }
        profile.setCoins(profile.getCoins() - getPrice(player, level + 1, color));
        //handle enchant
        doEnchant(item, player, mythicItem);

        menu.getAnimationData().setFinished(false);
        menu.getAnimationData().setStartEnchanting(true);
        menu.getAnimationData().setAnimationTick(0);

        menu.setClosedByMenu(true);
        menu.openMenu(player);
    }

    private void doEnchant(ItemStack item, Player player, IMythicItem mythicItem) {
        mythicItem.loadFromItemStack(item);
        IMythicItem beforeItem = mythicItem;
        MythicColor color = MythicColor.valueOf(ItemUtil.getItemStringData(item, "mythic_color").toUpperCase());
        int level = mythicItem.getTier();
        int maxLive = 0;
        if (level > 0) {
            maxLive = mythicItem.getMaxLive();
        }
        //根据附魔物品颜色的不同,maxLive也有所不同
        if (color == MythicColor.DARK) {
            switch (level) {
                case 0:
                    mythicItem.setMaxLive((Integer) RandomUtil.helpMeToChooseOne(20, 25, 30, 35));
                    break;
                case 1:
                    mythicItem.setMaxLive((Integer) RandomUtil.helpMeToChooseOne(40, 45, 50, 55, 60));
                    if (RandomUtil.hasSuccessfullyByChance(0.01)) {
                        mythicItem.setMaxLive(135);
                    }
                    break;
                default:
                    mythicItem.setMaxLive(random.nextInt(36) + 5); //5-40
                    break;
            }
        } else {
            if (color == MythicColor.RAGE && level == 0) {
                mythicItem.setMaxLive(((Integer) RandomUtil.helpMeToChooseOne(4, 5, 6, 7, 8, 9)));
            } else {
                switch (level) {
                    case 0:
                        mythicItem.setMaxLive(random.nextInt(7) + 3); //3-9
                        break;
                    case 1:
                        mythicItem.setMaxLive(random.nextInt(6) + 10); //10-15
                        break;
                    case 2:
                        if (RandomUtil.hasSuccessfullyByChance(0.01)) { //Artifact Prefix -> 100 Lives
                            mythicItem.setMaxLive(100);
                        } else {
                            mythicItem.setMaxLive(random.nextInt(8) + 16); //16-23
                        }
                        break;
                    default:
                        mythicItem.setMaxLive(random.nextInt(36) + 5); //5-40
                        break;
                }
            }
        }
        if (level > 0) {
            mythicItem.setLive(mythicItem.getLive() + mythicItem.getMaxLive() - maxLive);
        } else {
            mythicItem.setLive(mythicItem.getMaxLive());
        }
        level++;
        mythicItem.setTier(level);

        List<AbstractEnchantment> list = ThePit.getInstance()
                .getEnchantmentFactor()
                .getEnchantments()
                .stream()
                .filter(abstractEnchantment -> abstractEnchantment.canApply(item))
                .collect(Collectors.toList());
        List<AbstractEnchantment> enchantments = new ArrayList<>();
        if (level > 1) {
            enchantments = new ArrayList<>(mythicItem.getEnchantments().keySet());
        }
        boolean announcement = false;

        List<AbstractEnchantment> results = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.NORMAL).collect(Collectors.toList());
        List<AbstractEnchantment> rareResults = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.RARE).collect(Collectors.toList());

        //different type of mythic item have different rarity enchantments
        if (color == MythicColor.DARK) {
            results = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.DARK_NORMAL).collect(Collectors.toList());
            rareResults = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.DARK_RARE).collect(Collectors.toList());
        } else if (color == MythicColor.RAGE && level == 1) {
            results = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.RAGE).collect(Collectors.toList());
            rareResults = list.stream().filter(abstractEnchantment -> abstractEnchantment.getRarity() == EnchantmentRarity.RAGE_RARE).collect(Collectors.toList());
        }
        //Enchant Start
        switch (color) {
            case DARK: {
                if (level == 1) {
                    //t1 dark pants have only somber1
                    mythicItem.getEnchantments().put(ThePit.getInstance().getEnchantmentFactor().getEnchantmentMap().get("somber_enchant"), 1);
                    break;
                }
                if (level == 2) {
                    if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                        mythicItem.getEnchantments().put(enchantment, 1);
                        announcement = true;
                    } else {
                        results.removeIf(enchant -> enchant.getNbtName().equals("somber_enchant"));
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                        mythicItem.getEnchantments().put(enchantment, 1);
                    }
                }
                break;
            }
            case RAGE: {
                if (level == 1) {
                    if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                        mythicItem.getEnchantments().put(enchantment, RandomUtil.random.nextInt(enchantment.getMaxEnchantLevel() - 1) + 1);
                        announcement = true;
                    } else {
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                        mythicItem.getEnchantments().put(enchantment, RandomUtil.random.nextInt(enchantment.getMaxEnchantLevel() - 1) + 1);
                    }
                    break;
                }
            }
            default: {
                if (level == 1) {
                    //Tier 1 Enchant Start
                    int choice = random.nextInt(3);

                    boolean useBook = getPlayerMythicBook(player, item);
                    if (useBook) {
                        choice = 3;
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                        mythicItem.getEnchantments().put(enchantment, 3);
                        announcement = true;
                        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                        profile.setEnchantingBook(null);
                        mythicItem.boostedByBook = true;
                    }

                    switch (choice) {
                        case 0: { //choice 0: 1 of Lv1 Enchantment
                            AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                            enchantments.add(enchantment);
                            mythicItem.getEnchantments().put(enchantment, 1);
                            break;
                        }
                        case 1: { //choice 0: 2 of Lv1 Enchantment
                            for (int i = 0; i < 2; i++) {
                                results.removeAll(enchantments);
                                AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                                enchantments.add(enchantment);
                                mythicItem.getEnchantments().put(enchantment, 1);
                            }
                            break;
                        }
                        case 2: { //choice 0: 1 of Lv2 Enchantment
                            AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                            enchantments.add(enchantment);
                            mythicItem.getEnchantments().put(enchantment, Math.min(enchantment.getMaxEnchantLevel(), 2));
                            break;
                        }
                        default:
                            break;
                    }
                    //Tier 1 Enchant End
                } else if (level == 2) {
                    int amount = mythicItem.getEnchantments().size();

                    if (amount == 1) { // If this item has only 1 enchantment

                        boolean useBook = getPlayerMythicBook(player, item);

                        if (useBook) {
                            AbstractEnchantment rareEnchant = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                            mythicItem.getEnchantments().put(rareEnchant, 3);
                            announcement = true;
                            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                            profile.setEnchantingBook(null);
                            mythicItem.boostedByBook = true;
                        } else  {
                            int singleLevel = 0;
                            AbstractEnchantment enchantment = null;
                            for (Integer i : mythicItem.getEnchantments().values()) {
                                singleLevel = i;
                            }
                            for (AbstractEnchantment ae : mythicItem.getEnchantments().keySet()) {
                                enchantment = ae;
                            }
                            if (singleLevel == 1) { //Condition: 1 (Only 1 Lv1 Enchantment)
                                int choice = random.nextInt(3);
                                switch (choice) {
                                    case 0: { // 1->3
                                        mythicItem.getEnchantments().put(enchantment, 3);
                                        if (enchantment.getRarity() == EnchantmentRarity.RARE || enchantment.getRarity() == EnchantmentRarity.RAGE_RARE) {
                                            announcement = true;
                                        }
                                        break;
                                    }
                                    case 1: { // 1->21
                                        mythicItem.getEnchantments().put(enchantment, 2);
                                        announcement = shouldAnnouncement(player, color, mythicItem, enchantments, announcement, results, rareResults);
                                        break;
                                    }
                                    case 2: { // 1->211
                                        mythicItem.getEnchantments().put(enchantment, 2);
                                        for (int i = 0; i < 2; i++) {
                                            announcement = shouldAnnouncement(player, color, mythicItem, enchantments, announcement, results, rareResults);
                                        }
                                    }
                                    default:
                                        break;
                                }
                            } else if (singleLevel == 2) {
                                int choice = random.nextInt(2);
                                switch (choice) {
                                    case 0: { // 2->3
                                        mythicItem.getEnchantments().put(enchantment, 3);
                                        if (enchantment.getRarity() == EnchantmentRarity.RARE || enchantment.getRarity() == EnchantmentRarity.RAGE_RARE) {
                                            announcement = true;
                                        }
                                        break;
                                    }
                                    case 1: { // 2->21
                                        announcement = shouldAnnouncement(player, color, mythicItem, enchantments, announcement, results, rareResults);
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            } else {
                                announcement = shouldAnnouncement(player, color, mythicItem, enchantments, announcement, results, rareResults);
                            }
                        }
                    } else if (amount == 2) { //11
                        int choice = random.nextInt(2);
                        boolean useBook = getPlayerMythicBook(player, item);
                        if (useBook) {
                            choice = 3;
                            AbstractEnchantment rareEnchant = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                            mythicItem.getEnchantments().put(rareEnchant, 3);
                            announcement = true;
                            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                            profile.setEnchantingBook(null);
                            mythicItem.boostedByBook = true;
                        }

                        switch (choice) {
                            case 0: { // 11->21
                                AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(enchantments.toArray());
                                mythicItem.getEnchantments().put(enchantment, 2);
                                break;
                            }
                            case 1: { // 11->111
                                results.removeAll(enchantments);
                                AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                                if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
                                    announcement = true;
                                    rareResults.removeAll(enchantments);
                                    enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                                }
                                enchantments.add(enchantment);
                                mythicItem.getEnchantments().put(enchantment, 1);
                                break;
                            }
                            default:
                                break;
                        }
                    }
                } else if (level == 3) {
                    int amount = mythicItem.getEnchantments().size();
                    if (amount == 1) { // If this item have only 1 enchantment
                        AbstractEnchantment enchantment = null;
                        if (getPlayerMythicBook(player, item)) {
                            AbstractEnchantment rareEnchant = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                            mythicItem.getEnchantments().put(rareEnchant, 3);
                            announcement = true;
                            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                            profile.setEnchantingBook(null);
                        } else {
                            for (int i = 0; i < 2; i++) {
                                results.removeAll(enchantments);
                                if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
                                    announcement = true;
                                    rareResults.removeAll(enchantments);
                                    enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                                } else {
                                    enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                                }
                                enchantments.add(enchantment);
                                mythicItem.getEnchantments().put(enchantment, 1);
                            }
                            for (AbstractEnchantment abstractEnchantment : enchantments) {
                                mythicItem.getEnchantments().put(abstractEnchantment, Math.max(mythicItem.getEnchantments().get(abstractEnchantment), getRandomLevel()));
                            }
                            //set level of a new enchant to 1/2 (3 excluded cuz the limit)
                            Integer totalLevel = 0;
                            for (AbstractEnchantment abstractEnchantment : mythicItem.getEnchantments().keySet()) {
                                totalLevel += mythicItem.getEnchantments().get(abstractEnchantment);
                            }
                            if ((totalLevel == 8 && RandomUtil.hasSuccessfullyByChance(0.9)) || totalLevel == 9) {
                                if (enchantment != null) {
                                    mythicItem.getEnchantments().put(enchantment, RandomUtil.hasSuccessfullyByChance(0.1) ? 2 : 1);
                                }
                            }
                        }
                    } else if (amount == 2) { //21 -> 311
                        if (getPlayerMythicBook(player, item)) {
                            AbstractEnchantment rareEnchant = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                            mythicItem.getEnchantments().put(rareEnchant, 3);
                            announcement = true;
                            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                            profile.setEnchantingBook(null);
                        } else {
                            results.removeAll(enchantments);
                            AbstractEnchantment enchantment;
                            if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
                                announcement = true;
                                rareResults.removeAll(enchantments);
                                enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
                            } else {
                                enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
                            }
                            enchantments.add(enchantment);
                            mythicItem.getEnchantments().put(enchantment, 1);
                            for (AbstractEnchantment abstractEnchantment : enchantments) {
                                final int currentLevel = Math.max(mythicItem.getEnchantments().get(abstractEnchantment), getRandomLevel());
                                mythicItem.getEnchantments().put(abstractEnchantment, currentLevel);
                                if (currentLevel == 3 && (abstractEnchantment.getRarity() == EnchantmentRarity.RARE || abstractEnchantment.getRarity() == EnchantmentRarity.RAGE_RARE)) {
                                    announcement = true;
                                }
                            }
                            Integer totalLevel = 0;
                            for (AbstractEnchantment abstractEnchantment : mythicItem.getEnchantments().keySet()) {
                                totalLevel += mythicItem.getEnchantments().get(abstractEnchantment);
                            }
                            if ((totalLevel == 8 && RandomUtil.hasSuccessfullyByChance(0.9)) || totalLevel == 9) {
                                //set level of new enchant to 1/2 (3 excluded cuz the limit)
                                if (enchantment != null) {
                                    mythicItem.getEnchantments().put(enchantment, RandomUtil.hasSuccessfullyByChance(0.1) ? 2 : 1);
                                }
                            }
                        }


                    } else if (amount == 3) { // 111 -> 211/311
                        for (AbstractEnchantment abstractEnchantment : enchantments) {
                            final int currentLevel = Math.max(mythicItem.getEnchantments().get(abstractEnchantment), getRandomLevel());
                            mythicItem.getEnchantments().put(abstractEnchantment, currentLevel);
                            if (currentLevel == 3 && (abstractEnchantment.getRarity() == EnchantmentRarity.RARE || abstractEnchantment.getRarity() == EnchantmentRarity.RAGE_RARE)) {
                                announcement = true;
                            }
                        }
                        Integer totalLevel = 0;
                        for (AbstractEnchantment abstractEnchantment : mythicItem.getEnchantments().keySet()) {
                            totalLevel += mythicItem.getEnchantments().get(abstractEnchantment);
                        }
                        if ((totalLevel == 8 && RandomUtil.hasSuccessfullyByChance(0.9)) || totalLevel == 9) {
                            mythicItem.getEnchantments().put((AbstractEnchantment) RandomUtil.helpMeToChooseOne(mythicItem.getEnchantments().keySet().toArray()), 1);
                        }
                    }
                    boolean badLuck = true;
                    for (AbstractEnchantment abstractEnchantment : mythicItem.getEnchantments().keySet()) {
                        if (mythicItem.getEnchantments().get(abstractEnchantment) >= 3) {
                            badLuck = false;
                            break;
                        }
                    }
                    if (badLuck) {
                        AbstractEnchantment enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(mythicItem.getEnchantments().keySet().toArray());
                        mythicItem.getEnchantments().put(enchantment, 3);
                        if ((enchantment.getRarity() == EnchantmentRarity.RARE || enchantment.getRarity() == EnchantmentRarity.RAGE_RARE)) {
                            announcement = true;
                        }
                    }
                }
            }
        }

        mythicItem.getEnchantmentRecords()
                .add(new EnchantmentRecord(
                        player.getName(),
                        "Enchant Table",
                        System.currentTimeMillis()
                ));

        //Check if the mythic item have a prefix and announce it
        //reload it , maybe trash code
        mythicItem.loadFromItemStack(mythicItem.toItemStack());
        new PitPlayerEnchantEvent(player, beforeItem, mythicItem).callEvent();
        if (mythicItem.getPrefix() != null) {
            announcement = true;
        }

        if (announcement) {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

            net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(mythicItem.toItemStack());
            NBTTagCompound tag = new NBTTagCompound();
            nms.save(tag);
            BaseComponent[] hoverEventComponents = new BaseComponent[]{
                    new TextComponent(tag.toString())
            };
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.spigot().sendMessage(new ChatComponentBuilder(CC.translate("&d&l稀有附魔! &7" + profile.getFormattedNameWithRoman() + " &7在神话之井中获得了稀有物品: " + mythicItem.toItemStack().getItemMeta().getDisplayName() + " &e[查看]"))
                        .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents)).create());
            }
        }
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.setEnchantingItem(InventoryUtil.serializeItemStack(mythicItem.toItemStack()));
    }


    private boolean shouldAnnouncement(Player player, MythicColor color, AbstractPitItem mythicItem, List<AbstractEnchantment> enchantments, boolean announcement, List<AbstractEnchantment> results, List<AbstractEnchantment> rareResults) {
        AbstractEnchantment enchantment;
        results.removeAll(enchantments);
        if (RandomUtil.hasSuccessfullyByChance(NewConfiguration.INSTANCE.getChance(player, color))) {
            announcement = true;
            rareResults.removeAll(enchantments);
            enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(rareResults.toArray());
        } else {
            enchantment = (AbstractEnchantment) RandomUtil.helpMeToChooseOne(results.toArray());
        }
        enchantments.add(enchantment);
        mythicItem.getEnchantments().put(enchantment, 1);
        return announcement;
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

    public ItemStack getDefaultDisplayItem() {
        return new ItemBuilder(Material.ENCHANTMENT_TABLE)
                .name("&d神话之井")
                .lore("&7通过击杀玩家来获得", "&e神话之剑&7 &b神话之弓 &7以及", "&c神&6话&9之&a甲 &7等物品 ", " ", "&7在神话之井中为这些物品附魔", "&7可以赋予其大量的强大增益 ", " ", "&d放入神话物品到左侧空格中以开始!")
                .build();
    }

    /**
     * 返回升级至T3为附魔提供的随机等级(1~3) 概率分布6:2:2
     *
     * @return int Level
     */
    private int getRandomLevel() {
        if (RandomUtil.hasSuccessfullyByChance(0.6)) {
            return 1;
        } else {
            return random.nextInt(2) + 2;
        }
    }

    /**
     * 移除玩家指定颜色的裤子
     *
     * @param player 玩家
     * @param color  需要移除的颜色
     * @return 是否移除，返回false为移除失败
     */
    private boolean removeMythicLegWithColor(Player player, MythicColor color) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getEnchantingScience() == null) return false;
        ItemStack itemStack = InventoryUtil.deserializeItemStack(profile.getEnchantingScience());
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        String mythic_color = ItemUtil.getItemStringData(itemStack, "mythic_color");
        for (MythicColor mythicColor : MythicColor.values()) {
            if (mythicColor.getInternalName().equals(mythic_color)) {
                profile.setEnchantingScience(InventoryUtil.serializeItemStack(new ItemStack(Material.AIR)));
                return true;
            }
        }

        return false;
    }

    //获取玩家是否可在该物品上使用神话之书 可则返回书物品 否则返回null
    public static boolean getPlayerMythicBook(Player player, ItemStack item) {
        //设置神话之书物品
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        IMythicItem mythicItem = Utils.getMythicItem(item);

        if (mythicItem.getColor() == MythicColor.RAGE) {
            return false;
        }

        if (mythicItem.boostedByBook) {
            return false;
        }

        if (profile.getEnchantingBook() != null) {
            return "mythic_reel".equals(ItemUtil.getInternalName(InventoryUtil.deserializeItemStack(profile.getEnchantingBook())));
        }
        return false;
    }
}

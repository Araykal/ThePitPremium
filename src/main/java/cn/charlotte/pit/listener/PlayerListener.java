package cn.charlotte.pit.listener;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.data.sub.PlayerBanData;
import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.event.PitPlayerEnchantEvent;
import cn.charlotte.pit.event.PitProfileLoadedEvent;
import cn.charlotte.pit.medal.AbstractMedal;
import cn.charlotte.pit.medal.impl.challenge.LuckyDiamondMedal;
import cn.charlotte.pit.medal.impl.challenge.TrickleDownMedal;
import cn.charlotte.pit.menu.cactus.CactusMenu;
import cn.charlotte.pit.menu.cactus.DimensionalCactusMenu;
import cn.charlotte.pit.menu.cactus.SacrosanctCactusMenu;
import cn.charlotte.pit.mode.Mode;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.runnable.ProfileLoadRunnable;

import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.VectorUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 22:00
 */
@AutoRegister
public class PlayerListener implements Listener {
    private final Map<UUID, Long> goldenAppleCooldown = new HashMap<>();
    private final Map<UUID, Cooldown> firstAidEggCooldown = new HashMap<>();
    private final Random random = new Random();
    private final DecimalFormat numFormat = new DecimalFormat("0.00");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ThePit pit = ThePit.getInstance();
        Player player = event.getPlayer();
        PlayerUtil.clearPlayer(player, true);
        if (!pit.getPitConfig().getSpawnLocations().isEmpty()) {
            Location location = pit.getPitConfig()
                    .getSpawnLocations()
                    .get(random.nextInt(pit.getPitConfig().getSpawnLocations().size()));

            player.teleport(location);
        } else {
            player.sendMessage(CC.translate("&cNo spawn found "));
        }

        if (ProfileLoadRunnable.getInstance() == null) {
            event.getPlayer().kickPlayer(" ");
            return;
        }

        ProfileLoadRunnable.getInstance().handleJoin(player);
    }

    @EventHandler
    public void onProfileLoadComplete(PitProfileLoadedEvent event) {
        PlayerProfile profile = event.getPlayerProfile();
        Player player = Bukkit.getPlayer(profile.getPlayerUuid());

        if (player == null || !player.isOnline()) {
            return;
        }

        if (ThePit.getBungeeServerName().equals("NULL")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetServer");
            try {
                player.sendPluginMessage(ThePit.getInstance(), "BungeeCord", out.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                ThePit.getInstance().getLogger().warning("Failed to send getServerName message to the BungeeCord.");
            }
        }
        if (profile.isBanned()) {
            PlayerBanData banData = profile.getPlayerBanData();
            player.sendMessage(CC.translate("&4⚠ &c你当前已被禁止游玩天坑乱斗!"));
            player.sendMessage(CC.translate("&4⚠ &c此限制将在 &f" + TimeUtil.millisToRoundedTime(banData.getEnd() - System.currentTimeMillis()) + " &c后自动解除."));
            player.sendMessage(CC.translate("&4⚠ &c原因: &f" + profile.getPlayerBanData().getReason()));
            if (player.hasPermission("thepit.admin")) {
                player.sendMessage(CC.translate("&2⚠ &a但你是管理员,因此没有被移出房间."));
            } else {
                player.kickPlayer("You are currently suspended on this Pit server.");
            }
        } else {
            this.welcomePlayer(player);
        }

        PlayerInv playerInv = profile.getInventory();
        playerInv.applyItemToPlayer(player);
        InventoryUtil.supplyItems(player);
        profile.setLastLogoutTime(System.currentTimeMillis());
        profile.applyExperienceToPlayer(player);

        PlayerUtil.clearPlayer(player, false, false);

        //todo 修复最大生命值
        PerkData perkData = profile.getUnlockedPerkMap().get("extra_heart_perm_perk");
        if (perkData != null) {
            profile.getExtraMaxHealth().put("extra_heart_perm_perk", 2.0 * perkData.getLevel());
            player.setMaxHealth(profile.getMaxHealth());
        }
    }

    @EventHandler
    public void onPlayerInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (PlayerUtil.isStaffSpectating((Player) event.getPlayer())) {
                return;
            }
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId());
            if (!profile.isTempInvUsing()) {
                profile.setInventory(PlayerInv.fromPlayerInventory(event.getPlayer().getInventory()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        firstAidEggCooldown.remove(event.getPlayer().getUniqueId());
        goldenAppleCooldown.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        final String internalName = ItemUtil.getInternalName(event.getItem().getItemStack());
        if (internalName != null && (internalName.contains("fish") || internalName.equals("fishing_dia_armor") || internalName.contains("mythic"))) {
            return;
        }

        if (PlayerUtil.isStaffSpectating(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        event.getItem().remove();
        Player player = event.getPlayer();

        ItemStack stack = event.getItem().getItemStack();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);

        if (nmsItem.getItem() instanceof ItemArmor) {
            if (ItemUtil.getInternalName(stack) == null) {
                return;
            }
            ItemStack itemStack;
            if ("lucky_diamond".equals(internalName)) {
                new LuckyDiamondMedal().addProgress(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()), 1);
                itemStack = new ItemBuilder(stack.getType()).canDrop(false).canSaveToEnderChest(false).deathDrop(true).canTrade(false).removeOnJoin(true).internalName(internalName).lore("&b幸运钻石天赋物品").buildWithUnbreakable();
            } else {
                itemStack = new ItemBuilder(stack.getType()).canDrop(false).canSaveToEnderChest(true).deathDrop(true).internalName(internalName).buildWithUnbreakable();
            }

            for (ItemStack item : player.getInventory().getArmorContents()) {
                if (item != null && item.getType() != Material.AIR && itemStack.getType().name().contains("IRON") && item.getType() == itemStack.getType()) {
                    return;
                }
            }

            int slot = InventoryUtil.getArmorSlot(itemStack.getType());
            if (slot == -1) {
                return;
            }
            if (itemStack.getType().name().contains("BOOTS")) {
                if (PlayerUtil.isPlayerChosePerk(player, "marathon")) {
                    return;
                }
            }
            ItemStack[] armorContents = player.getInventory().getArmorContents();
            if (itemStack.getType().name().contains("IRON")) {
                if (armorContents[slot].getType().name().contains("DIAMOND")) {
                    return;
                }
                if (!itemStack.getType().name().contains("CHAINMAIL")) {
                    return;
                }
                //check if player is equipping a mythic pants
                final String internalName1 = ItemUtil.getInternalName(armorContents[slot]);
                if (internalName1 != null && (internalName1.equalsIgnoreCase("mythic_leggings") || internalName1.equals("armageddon_boots") || internalName1.equals("angel_chestplate") || internalName1.equals("kings_helmet"))) {
                    return;
                }
                armorContents[slot] = itemStack;
                player.getInventory().setArmorContents(armorContents);

                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1F, 1F);
            } else if (itemStack.getType().name().contains("DIAMOND")) {
                final String internalName1 = ItemUtil.getInternalName(armorContents[slot]);
                if (internalName1 != null && (internalName1.equalsIgnoreCase("mythic_leggings") || internalName1.equals("armageddon_boots") || internalName1.equals("angel_chestplate") || internalName1.equals("kings_helmet"))) {
                    //check if player is equipping a mythic pants
                    InventoryUtil.addInvReverse(player.getInventory(), itemStack);
                    return;
                } else if (armorContents[slot].getType().name().contains("DIAMOND") || armorContents[slot].getType().name().contains("IRON")) {
                    InventoryUtil.addInvReverse(player.getInventory(), armorContents[slot]);
                }

                armorContents[slot] = itemStack;
                player.getInventory().setArmorContents(armorContents);
                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1F, 1F);
            }
        } else if (stack.getType() == Material.ARROW) {
            ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW).internalName("default_arrow").defaultItem().canDrop(false).canSaveToEnderChest(false);
            player.getInventory().addItem(arrowBuilder.build());
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
        } else if ((stack.getType() == Material.BOW || CraftItemStack.asNMSCopy(stack).getItem() instanceof ItemSword) && !event.isCancelled()) {
            InventoryUtil.addInvReverse(player.getInventory(), event.getItem().getItemStack());
        } else if (stack.getType() == Material.GOLD_INGOT) {
            if (event.getItem().hasMetadata("gold")) {
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId());
                int gold = event.getItem().getMetadata("gold").get(0).asInt();
                if (PlayerUtil.isPlayerChosePerk(player, "trickle_down_perk")) {
                    gold += 10;
                    PlayerUtil.heal(player, 4);
                    new TrickleDownMedal().addProgress(profile, 10);
                }
                int level = Utils.getEnchantLevel(event.getPlayer().getInventory().getLeggings(), "pebble_enchant");
                if (PlayerUtil.isVenom(event.getPlayer()) || PlayerUtil.isEquippingSomber(event.getPlayer())) {
                    level = 0;
                }
                if (level > 0) {
                    gold += level * 10;
                    if (level >= 3) {
                        PlayerUtil.heal(player, 2);
                    }
                }
                profile.setCoins(profile.getCoins() + gold);

                profile.setGoldPicked(profile.getGoldPicked() + 1);
                player.sendMessage(CC.translate("&6&l捡起硬币! &7从地上找到了&6 " + gold + " &7硬币!"));
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.8F);
            }
        } else if (ThePit.mode == Mode.Normal) {
            player.getInventory().addItem(stack);
            player.sendMessage(CC.translate("§c你似乎得到了一些物品?"));
        }
    }

    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
                event.setCancelled(true);
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId());
                PerkData data = profile.getUnlockedPerkMap().get("Mythicism");
                if (data != null) {
                    ThePit.getApi().openMythicWellMenu(event.getPlayer());
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 1, 0.9F);
                    return;
                }
                event.getPlayer().sendMessage(CC.translate("&c你需要达到 " + LevelUtil.getLevelTag(0, 120) + " &c解锁精通玩法并解锁精通天赋 &6神话附魔师 &c以使用神话之井!"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            e.setCancelled(true);
            return;
        }
        Player player = e.getPlayer();
        if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.WORKBENCH
                || e.getClickedBlock().getType() == Material.ANVIL || e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
            e.setCancelled(true);
            return;
        }
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE && item.getType() == Material.SKULL_ITEM && item.getDurability() == 3 && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && "golden_head".equals(ItemUtil.getInternalName(item))) {
            e.setCancelled(true);
            if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                e.setUseItemInHand(Event.Result.DENY);
                return;
            }
            player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
            goldenAppleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
            PlayerUtil.takeOneItemInHand(player);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 140, 0), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1), true);
            (((CraftPlayer) player).getHandle()).setAbsorptionHearts(6.0F);
            if (PlayerUtil.isPlayerUnlockedPerk(player, "yummy_perk")) {
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                profile.setCoins(profile.getCoins() + 3);
            }
        } else if (item.getType() == Material.BAKED_POTATO) {
            if ("angry_potato".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
                goldenAppleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                PlayerUtil.takeOneItemInHand(player);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1), true);
                (((CraftPlayer) player).getHandle()).setAbsorptionHearts(4.0F);
            }
        } else if (item.getType() == Material.MAGMA_CREAM) {
            if ("broken_soul".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
                goldenAppleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                PlayerUtil.takeOneItemInHand(player);
                PlayerUtil.heal(player, 8);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1), true);
                (((CraftPlayer) player).getHandle()).setAbsorptionHearts(6.0F);
            }
        } else if (item.getType() == Material.MONSTER_EGG) {
            if ("first_aid_egg".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                final Cooldown cooldown = firstAidEggCooldown.get(player.getUniqueId());
                if (cooldown != null && !cooldown.hasExpired()) {
                    e.setUseItemInHand(Event.Result.DENY);
                    e.getPlayer().sendMessage(CC.translate("&c在再次使用此物品前,请等待" + TimeUtil.millisToRoundedTime(cooldown.getRemaining()).replace(" ", "") + "!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
                firstAidEggCooldown.put(player.getUniqueId(), new Cooldown(30, TimeUnit.SECONDS));
                player.removePotionEffect(PotionEffectType.SPEED);
                PlayerUtil.heal(player, 5);
            }
        } else if (item.getType() == Material.MUSHROOM_SOUP) {
            if ("perk_tasty_soup_kill".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
                goldenAppleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                PlayerUtil.takeOneItemInHand(player);
                PlayerUtil.heal(player, 2);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 7, 0), true);
                (((CraftPlayer) player).getHandle()).setAbsorptionHearts(Math.min(8F, ((CraftPlayer) player).getHandle().getAbsorptionHearts() + 2F));
            }
            if ("perk_tasty_soup_assist".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
                goldenAppleCooldown.put(player.getUniqueId(), System.currentTimeMillis());
                PlayerUtil.takeOneItemInHand(player);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1), true);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 7, 0), true);
                (((CraftPlayer) player).getHandle()).setAbsorptionHearts(Math.min(8F, ((CraftPlayer) player).getHandle().getAbsorptionHearts() + 2F));
            }
        } else if (item.getType() == Material.CACTUS) {
            if ("cactus".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                new CactusMenu().openMenu(player);
            } else if ("sacrosanct_cactus".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                new SacrosanctCactusMenu().openMenu(player);
            } else if ("dimensional_cactus".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                if (System.currentTimeMillis() - goldenAppleCooldown.getOrDefault(player.getUniqueId(), 0L) <= 1000L) {
                    e.setUseItemInHand(Event.Result.DENY);
                    return;
                }
                new DimensionalCactusMenu().openMenu(player);
            }
        } else if (item.getType() == Material.MILK_BUCKET) {
            if ("milk_bucket".equals(ItemUtil.getInternalName(item))) {
                e.setCancelled(true);
                PlayerUtil.takeOneItemInHand(e.getPlayer());
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 0));
                e.getPlayer().sendMessage(CC.translate("&f牛奶! &7你喝下了牛奶"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DRINK, 1f, 1f);
            }
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().getType() == Material.GOLDEN_APPLE) {
            (((CraftPlayer) event.getPlayer()).getHandle()).setAbsorptionHearts(8.0F);
            PlayerUtil.takeOneItemInHand(player);
            player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), true);
            if (PlayerUtil.isPlayerUnlockedPerk(player, "yummy_perk")) {
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                profile.setCoins(profile.getCoins() + 3);
            }

            event.setCancelled(true);
        }

        if (event.getItem() != null && "perk_olympus".equals(ItemUtil.getInternalName(event.getItem()))) {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            PlayerUtil.takeOneItemInHand(player);
            player.playSound(player.getLocation(), Sound.EAT, 1F, 1F);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 2), true);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 24 * 20, 0), true);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 1), true);
            profile.setExperience(profile.getExperience() + 27);

            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        final Player killer = event.getKiller();
        final Cooldown cooldown = firstAidEggCooldown.get(killer.getUniqueId());
        if (cooldown != null && !cooldown.hasExpired()) {
            firstAidEggCooldown.put(killer.getUniqueId(), new Cooldown(cooldown.getRemaining() - 5 * 1000L));
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (PlayerUtil.isStaffSpectating(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
        if (ItemUtil.isDefaultItem(event.getItemDrop().getItemStack())) {
            event.getItemDrop().remove();
        }

        if (!ItemUtil.canDrop(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        if ("golden_head".equals(ItemUtil.getInternalName(event.getCursor())) && event.getSlotType() != null && event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }

        if (event.getClickedInventory() instanceof CraftingInventory) {
            event.setCancelled(true);
        }
    }

    private void welcomePlayer(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.isNicked()) {
            player.sendMessage(CC.translate("&2&l匿名模式! &7你现在对外显示的游戏名为: " + profile.getFormattedNameWithRoman()));
        }
    }

    @EventHandler
    public void onThrowTnt(PlayerInteractEvent event) {
        if ("tnt".equals(ItemUtil.getInternalName(event.getItem()))) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                event.setCancelled(true);

                if (event.getPlayer().getItemInHand().getAmount() > 1) {
                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                } else {
                    event.getPlayer().setItemInHand(null);
                }

                BlockIterator blockIterator = new BlockIterator(event.getPlayer());
                TNTPrimed tntPrimed = (TNTPrimed) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks(30);
                for (int i = 0; i < 20; i++) {
                    blockIterator.next();
                }

                tntPrimed.setMetadata("internal", new FixedMetadataValue(ThePit.getInstance(), "tnt"));
                tntPrimed.setMetadata("shooter", new FixedMetadataValue(ThePit.getInstance(), event.getPlayer().getUniqueId().toString()));

                VectorUtil.entityPush(tntPrimed, blockIterator.next().getLocation(), 25);
            }
        } else if ("red_packet".equals(ItemUtil.getInternalName(event.getItem()))) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                event.setCancelled(true);

                if (event.getPlayer().getItemInHand().getAmount() > 1) {
                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                } else {
                    event.getPlayer().setItemInHand(null);
                }

                BlockIterator blockIterator = new BlockIterator(event.getPlayer());
                TNTPrimed tntPrimed = (TNTPrimed) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks(30);
                for (int i = 0; i < 20; i++) {
                    blockIterator.next();
                }

                Integer money = ItemUtil.getItemIntData(event.getItem(), "money");
                String sender = ItemUtil.getItemStringData(event.getItem(), "sender");
                if (!event.getPlayer().getUniqueId().toString().equals(sender)) {
                    event.getPlayer().sendMessage(CC.translate("&C你并非红包的主人"));
                    return;
                }

                tntPrimed.setMetadata("money", new FixedMetadataValue(ThePit.getInstance(), money));
                tntPrimed.setMetadata("internal", new FixedMetadataValue(ThePit.getInstance(), "red_packet"));
                tntPrimed.setMetadata("shooter", new FixedMetadataValue(ThePit.getInstance(), event.getPlayer().getUniqueId().toString()));

                VectorUtil.entityPush(tntPrimed, blockIterator.next().getLocation(), 25);
            }
        }
    }

    @EventHandler
    public void onPreExplosion(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            if (event.getEntity().hasMetadata("internal") && event.getEntity().getMetadata("internal").size() > 0 && "red_packet".equals(event.getEntity().getMetadata("internal").get(0).asString())) {
                Location location = event.getEntity().getLocation();
                float radius = event.getRadius();

                List<Player> players = PlayerUtil.getNearbyPlayers(location, radius);
                Map<Player, Double> distanceMap = new HashMap<>();
                double distanceSum = players.stream().mapToDouble(player -> {
                    double distance = player.getLocation().distance(location);
                    distanceMap.put(player, distance);
                    return distance;
                }).sum();

                int money = event.getEntity().getMetadata("money").get(0).asInt();
                String shooter = event.getEntity().getMetadata("shooter").get(0).asString();

                for (Map.Entry<Player, Double> entry : distanceMap.entrySet()) {
                    Double distance = entry.getValue();
                    Player player = entry.getKey();


                    double value = 1 - (distance / (distanceSum));
                    int given = (int) (money * value);
                    PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(UUID.fromString(shooter));
                    String name = profile.getPlayerName();

                    player.sendMessage(CC.translate("&c&l红包! &7你接受到了来自 &6" + name + " &7的红包 &6+" + given + "硬币&7(#" + numFormat.format(value * 100) + "%)"));
                    PlayerProfile profileByUuid = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                    profileByUuid.setCoins(profileByUuid.getCoins() + given);
                }

            }
        }
    }

    @EventHandler
    public void onTntExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata("shooter") && event.getEntity().getMetadata("shooter").size() > 0) {
            event.setYield(0);
            event.blockList().clear();
            if (event.getEntity().hasMetadata("internal") && event.getEntity().getMetadata("internal").size() > 0 && (event.getEntity().getMetadata("internal").get(0).asString().equalsIgnoreCase("tnt_enchant_item") || event.getEntity().getMetadata("internal").get(0).asString().equalsIgnoreCase("insta_boom_enchant_item"))) {
                return;
            }
            List<EntityFallingBlock> fallingBlocks = new ArrayList<>();
            List<Player> players = new ArrayList<>();

            for (Block b : event.blockList()) {

                if (RandomUtil.random.nextBoolean()) {
                    continue;
                }

                double x = -2.0d + (Math.random() * 4.0d);

                double y = -3.0d + (Math.random() * 6.0d);

                double z = -2.0d + (Math.random() * 4.0d);

                Location location = event.getLocation();
                WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
                int blockX = location.getBlockX();
                int blockY = location.getBlockY();
                int blockZ = location.getBlockZ();

                EntityFallingBlock fallingBlock = new EntityFallingBlock(world, blockX, blockY, blockZ, net.minecraft.server.v1_8_R3.Block.getById(b.getTypeId()).fromLegacyData(0));
                fallingBlock.motX = x;
                fallingBlock.motY = y;
                fallingBlock.motZ = z;
                fallingBlock.velocityChanged = true;

                fallingBlocks.add(fallingBlock);

                PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(fallingBlock, 70, net.minecraft.server.v1_8_R3.Block.getCombinedId(fallingBlock.getBlock()));
                PacketPlayOutEntityVelocity vectorPacket = new PacketPlayOutEntityVelocity(fallingBlock);


                for (Player player : PlayerUtil.getNearbyPlayers(event.getLocation(), 30)) {
                    players.add(player);
                    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                    entityPlayer.playerConnection.sendPacket(spawnPacket);
                    entityPlayer.playerConnection.sendPacket(vectorPacket);
                }
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), () -> {
                List<Integer> list = fallingBlocks.stream()
                        .map(Entity::getId)
                        .collect(Collectors.toList());

                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
                int[] a = new int[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    a[i] = list.get(i);
                }

                try {
                    Field field = packet.getClass().getDeclaredField("a");
                    field.setAccessible(true);
                    field.set(packet, a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (Player player : players) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }

            }, 20 * 5);
        }
    }

    @EventHandler
    public void onEnchant(PitPlayerEnchantEvent event) {
        List<AbstractEnchantment> beforeEnchants = new ArrayList<>(event.getBeforeItem().getEnchantments().keySet());
        List<AbstractEnchantment> afterEnchants = new ArrayList<>(event.getAfterItem().getEnchantments().keySet());
        beforeEnchants.removeIf(enchant -> enchant.getRarity() != EnchantmentRarity.RARE);
        afterEnchants.removeIf(enchant -> enchant.getRarity() != EnchantmentRarity.RARE);
        if (beforeEnchants.isEmpty() && !afterEnchants.isEmpty()) {
            Stream<AbstractMedal> stream = ThePit.getInstance().getMedalFactory().getMedals().stream()
                    .filter(abstractMedal -> abstractMedal.getInternalName().equals("GET_RARE_ENCHANT"));
            Optional<AbstractMedal> first = stream.findFirst();
            AbstractMedal medal = first.get();

            medal.addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
        }

        AbstractMedal medal = ThePit.getInstance().getMedalFactory().getMedals().stream()
                .filter(abstractMedal -> abstractMedal.getInternalName().equals("STATUS_ENCHANT_TIMES"))
                .findFirst()
                .get();

        medal.addProgress(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), 1);
    }


}

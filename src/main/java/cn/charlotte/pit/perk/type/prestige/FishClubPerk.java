package cn.charlotte.pit.perk.type.prestige;


import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.mode.Mode;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/8 23:38
 */

@AutoRegister
public class FishClubPerk extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "fishing_club_perk";
    }

    @Override
    public String getDisplayName() {
        return "钓鱼俱乐部";
    }

    @Override
    public Material getIcon() {
        return Material.WATER_BUCKET;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
//        return 999999999;
        if (level < 4) {
            return 5;
        } else {
            return 10;
        }
    }

    @Override
    public int requirePrestige() {
        return 3;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "&7I -> 您将可以在水中找到鲑鱼和鲽鱼.",
                "&7II -> 钓鱼时增加25经验",
                "&7III -> 钓鱼时有概率掉落钻石盔甲",
                "&7IV -> 钓鱼速度增加20%",
                "&7V -> 您可以在水中找到 &b青色神话之甲"
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            event.getCaught().remove();
            event.setExpToDrop(0);

            final boolean diamondSuccess = RandomUtil.hasSuccessfullyByChance(0.008);
            if (diamondSuccess) {
                final ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SWORD)
                        .canSaveToEnderChest(false)
                        .canTrade(false)
                        .canDrop(false)
                        .deathDrop(true)
                        .removeOnJoin(false)
                        .name("&6钻石剑")
                        .internalName("fishing_dia_armor")
                        .buildWithUnbreakable();

                Item item = event.getPlayer()
                        .getWorld()
                        .dropItemNaturally(
                                event.getCaught().getLocation(),
                                itemStack
                        );
                dropFishing(event.getPlayer(), event.getHook(), item);
                return;
            } else {
                Item item = event.getPlayer().getWorld().dropItemNaturally(event.getCaught().getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                item.setMetadata("gold", new FixedMetadataValue(ThePit.getInstance(), RandomUtil.random.nextInt(3) + 3));

                dropFishing(event.getPlayer(), event.getHook(), item);
            }

            final Player player = event.getPlayer();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            PerkData perkData = profile.getUnlockedPerkMap().get(getInternalPerkName());

            if (perkData == null) {
                return;
            }

            final int level = perkData.getLevel();
            if (level >= 1) {
                final boolean success = RandomUtil.hasSuccessfullyByChance(0.12);
                if (success) {
                    final Location location = player.getLocation();
                    final Entity caught = event.getCaught();
                    final Location caughtLocation = caught.getLocation();

                    final Item item = location.getWorld().dropItem(
                            caughtLocation,
                            (ItemStack) RandomUtil.helpMeToChooseOne(
                                    new ItemBuilder(Material.RAW_FISH)
                                            .internalName("fishing_fish")
                                            .name("&c大鲑鱼")

                                            .lore(
                                                    "&7一条很大的鲑鱼",
                                                    "",
                                                    "&7吃下它给予你 &c生命恢复III &f(00:03)",
                                                    "&8即使你不是很饿"
                                            )
                                            .build(),
                                    new ItemBuilder(Material.RAW_FISH)
                                            .name("&c鲑鱼")
                                            .internalName("fishing_fish")
                                            .lore(
                                                    "&7一条鲑鱼",
                                                    "",
                                                    "&7吃下它给予你 &c生命恢复III &f(00:03)",
                                                    "&8即使你不是很饿"
                                            )
                                            .build(),
                                    new ItemBuilder(Material.RAW_FISH)
                                            .name("&c小鲑鱼")
                                            .internalName("fishing_fish")
                                            .lore(
                                                    "&7一条很小的鲑鱼",
                                                    "",
                                                    "&7吃下它给予你 &c生命恢复III &f(00:03)",
                                                    "&8即使你不是很饿"
                                            )
                                            .build(),
                                    new ItemBuilder(Material.RAW_FISH)
                                            .name("&c大Irony鱼")
                                            .internalName("fishing_fish_irony")
                                            .lore(
                                                    "&7一条很大的Irony鱼",
                                                    "",
                                                    "&7吃下它给予你 &e2❤",
                                                    "&8即使你不是很饿"
                                            )
                                            .build(),
                                    new ItemBuilder(Material.RAW_FISH)
                                            .name("&cIrony鱼")
                                            .internalName("fishing_fish_irony")
                                            .lore(
                                                    "&7一条Irony鱼",
                                                    "",
                                                    "&7吃下它给予你 &e2❤",
                                                    "&8即使你不是很饿"
                                            )
                                            .build(),
                                    new ItemBuilder(Material.RAW_FISH)
                                            .name("&c小Irony鱼")
                                            .internalName("fishing_fish_irony")
                                            .lore(
                                                    "&7一条很小的Irony鱼",
                                                    "",
                                                    "&7吃下它给予你 &e2❤",
                                                    "&8即使你不是很饿"
                                            )
                                            .build()
                            )
                    );

                    dropFishing(player, caught, item);

                    player.sendMessage(CC.translate("&a钓鱼! 尊敬的钓鱼俱乐部会员, 您找到了一条 " + item.getItemStack().getItemMeta().getDisplayName() + "&a !"));
                }
            }

            if (level >= 2) {
                profile.setExperience(profile.getExperience() + 25);
                profile.applyExperienceToPlayer(player);
            }

            if (level >= 3) {
                final boolean success = RandomUtil.hasSuccessfullyByChance(0.02);
                if (success) {
                    final Location location = player.getLocation();
                    final Entity caught = event.getCaught();
                    final Location caughtLocation = caught.getLocation();

                    final Item item = location.getWorld().dropItem(
                            caughtLocation,
                            (ItemStack) RandomUtil.helpMeToChooseOne(
                                    new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石胸甲")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_LEGGINGS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石护腿")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_LEGGINGS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石护腿")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_LEGGINGS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石护腿")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable(),
                                    new ItemBuilder(Material.DIAMOND_BOOTS)
                                            .internalName("fishing_dia_armor")
                                            .removeOnJoin(false)
                                            .deathDrop(true)
                                            .canDrop(false)
                                            .canTrade(false)
                                            .canSaveToEnderChest(true)
                                            .name("&6钻石靴子")
                                            .buildWithUnbreakable()
                            )
                    );

                    dropFishing(player, caught, item);

                    player.sendMessage(CC.translate("&a钓鱼! 尊敬的钓鱼俱乐部会员, 您找到了钻石护甲!"));
                }
            }

            if (level >= 5) {
                if (RandomUtil.hasSuccessfullyByChance(0.06)) {
                    final MythicLeggingsItem item = new MythicLeggingsItem();
                    item.setColor(MythicColor.AQUA);

                    player.getInventory().addItem(item.toItemStack());
                }
            }

        } else if (event.getState() == PlayerFishEvent.State.FISHING) {
            final Player player = event.getPlayer();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

            PerkData perkData = profile.getUnlockedPerkMap().get(getInternalPerkName());

            if (perkData == null) {
                return;
            }

            if (perkData.getLevel() >= 3) {
                final Fish hook = event.getHook();
                hook.setBiteChance(Math.min(hook.getBiteChance() * 1.2, 1.0));
            }
        }
    }

    @EventHandler
    public void onInteractFish(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT")) {
            final Player player = event.getPlayer();
            if (player.getFoodLevel() >= 20) {
                final ItemStack itemInHand = player.getItemInHand();
                final String internalName = ItemUtil.getInternalName(itemInHand);
                if ("fishing_fish".equals(internalName) || "fishing_fish_irony".equals(internalName)) {
                    player.setFoodLevel(19);
                }
            }
        }
    }

    @EventHandler
    public void onEatFish(PlayerItemConsumeEvent event) {
        if (ThePit.mode == Mode.Normal){
            return;
        }
        final ItemStack item = event.getItem();
        final String internalName = ItemUtil.getInternalName(item);
        if ("fishing_fish".equals(internalName)) {
            final Player player = event.getPlayer();
            player.setFoodLevel(20);

            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 2));

        } else if ("fishing_fish_irony".equals(internalName)) {
            final Player player = event.getPlayer();
            player.setFoodLevel(20);

            final EntityPlayer handle = ((CraftPlayer) player).getHandle();
            handle.setAbsorptionHearts(handle.getAbsorptionHearts() + 2);
        }
    }

    @EventHandler
    public void onItemInHandChange(PlayerItemHeldEvent event) {
        if (ThePit.mode == Mode.Normal){
            return;
        }
        final Player player = event.getPlayer();
        if (player.getFoodLevel() < 20) {
            final ItemStack itemInHand = player.getItemInHand();
            final String internalName = ItemUtil.getInternalName(itemInHand);
            if (!"fishing_fish".equals(internalName) && !"fishing_fish_irony".equals(internalName)) {
                player.setFoodLevel(20);
            }
        }
    }

    public void dropFishing(Player player, Entity caught, Item item) {
        final Location location = player.getLocation();
        final Location caughtLocation = caught.getLocation();

        double d5 = location.getX() - caughtLocation.getX();
        double d6 = location.getY() - caughtLocation.getY();
        double d7 = location.getZ() - caughtLocation.getZ();
        double d8 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
        double d9 = 0.1D;

        item.setVelocity(new Vector(
                d5 * d9,
                d6 * d9 + (double) MathHelper.sqrt(d8) * 0.08D,
                d7 * d9));
    }
}

package cn.charlotte.pit.listener;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.KillRecap;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.EnchantmentFactor;
import cn.charlotte.pit.enchantment.param.event.NotPlayerOnly;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.event.OriginalTimeChangeEvent;
import cn.charlotte.pit.event.PitDamageEvent;
import cn.charlotte.pit.event.PitDamagePlayerEvent;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.game.Game;
import cn.charlotte.pit.parm.listener.*;
import cn.charlotte.pit.parm.type.BowOnly;
import cn.charlotte.pit.parm.type.ThrowOnly;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkFactory;
import cn.charlotte.pit.quest.AbstractQuest;
import cn.charlotte.pit.quest.QuestFactory;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MobEffectList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/2 12:40
 */
public class GameEffectListener implements Listener {
    private final DecimalFormat numFormatTwo = new DecimalFormat("0.00");

    @SneakyThrows
    public static void processKilled(IPlayerKilledEntity ins, int level, Player killer, Entity target, AtomicDouble coin, AtomicDouble exp) {
        if (level != -1) {
            final Method method = ins.getClass().getMethod("handlePlayerKilled", int.class, Player.class, Entity.class, AtomicDouble.class, AtomicDouble.class);
            if (method.isAnnotationPresent(PlayerOnly.class)) {
                if (target instanceof Player) {
                    Player player = (Player) target;
                    ins.handlePlayerKilled(level, killer, player, coin, exp);
                }
            } else if (method.isAnnotationPresent(NotPlayerOnly.class)) {
                if (!(target instanceof Player)) {
                    ins.handlePlayerKilled(level, killer, target, coin, exp);
                }
            } else {
                ins.handlePlayerKilled(level, killer, target, coin, exp);
            }
        }
    }

    @SneakyThrows
    public static void processBeKilledByEntity(IPlayerBeKilledByEntity ins, int level, Player myself, Entity target, AtomicDouble coin, AtomicDouble exp) {
        if (level != -1) {
            final Method method = ins.getClass().getMethod("handlePlayerBeKilledByEntity", int.class, Player.class, Entity.class, AtomicDouble.class, AtomicDouble.class);
            if (method.isAnnotationPresent(PlayerOnly.class)) {
                if (target instanceof Player) {
                    Player player = (Player) target;
                    ins.handlePlayerBeKilledByEntity(level, myself, player, coin, exp);
                }
            } else if (method.isAnnotationPresent(NotPlayerOnly.class)) {
                if (!(target instanceof Player)) {
                    ins.handlePlayerBeKilledByEntity(level, myself, target, coin, exp);
                }
            } else {
                ins.handlePlayerBeKilledByEntity(level, myself, target, coin, exp);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFired(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            if (player.getHealth() <= event.getFinalDamage()) {
                player.damage(player.getMaxHealth() * 100);
            } else {
                player.setHealth(player.getHealth() - event.getFinalDamage());
            }

            PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                    .setNoDamageAnimations(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void modifyLeatherArmor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if ("kings_helmet".equals(ItemUtil.getInternalName(((Player) event.getEntity()).getInventory().getHelmet()))) {
                //0.12是每点伤害 铁裤和皮革裤子的减免差距，所以减去0.12*伤害值，以增加皮革裤子为铁裤防御
                event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) - 0.06 * event.getDamage());
            }

            if ("mythic_leggings".equals(ItemUtil.getInternalName(((Player) event.getEntity()).getInventory().getLeggings()))) {
                if (ItemUtil.getItemStringData(((Player) event.getEntity()).getInventory().getLeggings(), "mythic_color").equals("dark")) {
                    return;
                }
                //0.12是每点伤害 铁裤和皮革裤子的减免差距，所以减去0.12*伤害值，以增加皮革裤子为铁裤防御
                event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, event.getDamage(EntityDamageEvent.DamageModifier.ARMOR) - 0.12 * event.getDamage());
            }
        }
    }

    @EventHandler
    public void addEnchantToArrow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            StringBuilder sb = new StringBuilder();
            for (IPlayerShootEntity instance : ThePit.getInstance()
                    .getEnchantmentFactor()
                    .getPlayerShootEntities()) {
                final AbstractEnchantment enchant = (AbstractEnchantment) instance;
                final int level = enchant.getItemEnchantLevel(event.getBow());
                if (level > 0) {
                    sb.append(enchant.getNbtName())
                            .append(":")
                            .append(level)
                            .append(";");
                }
            }

            if (sb.length() == 0) {
                return;
            }
            event.getProjectile().setMetadata("enchant", new FixedMetadataValue(ThePit.getInstance(), sb.substring(0, sb.length() - 1)));
        }
    }

    @EventHandler
    public void onTimeChange(OriginalTimeChangeEvent event) {
        CC.boardCast("Time change to: " + event.getTime());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            //修正伤害
            final Player attacker = (Player) event.getDamager();
            if (attacker.getItemInHand() == null || attacker.getItemInHand().getType() == Material.AIR || attacker.getItemInHand().getType() == Material.FISHING_ROD) {
                event.setDamage(1);
            } else {
                final EntityPlayer entityPlayer = ((CraftPlayer) attacker).getHandle();
                float f = (float) entityPlayer.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
                if (f > 9) {
                    event.setDamage(0);
                }
            }
        }


        PerkFactory perkFactory = ThePit.getInstance()
                .getPerkFactory();

        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        EnchantmentFactor enchantmentFactor = ThePit.getInstance().getEnchantmentFactor();
        QuestFactory questFactory = ThePit.getInstance().getQuestFactory();

        AtomicDouble finalDamage = new AtomicDouble(0);
        AtomicDouble boostDamage = new AtomicDouble(1);
        AtomicBoolean cancel = new AtomicBoolean(false);
        Player damager = null;

        final Game game = ThePit.getInstance().getGame();

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();

            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(damager.getUniqueId());
            if (NewConfiguration.INSTANCE.getNoobProtect() && profile.getPrestige() <= 0 && profile.getLevel() < NewConfiguration.INSTANCE.getNoobProtectLevel()) {
                boostDamage.getAndAdd(NewConfiguration.INSTANCE.getNoobDamageBoost() - 1);
            }

            //perk handler
            for (IAttackEntity ins : perkFactory.getAttackEntities()) {
                AbstractPerk perk = (AbstractPerk) ins;
                if (game.getDisabledPerks().stream().anyMatch(ignoredPerk -> ignoredPerk.getInternalPerkName().equals(perk.getInternalPerkName()))) {
                    continue;
                }
                int level = perk.getPlayerLevel(damager);
                processAttackEntity(ins, level, damager, event.getEntity(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
            }



            //enchant handler
            //we should check has somber enchant on leggings
            boolean somberFound = PlayerUtil.isEquippingSomber(damager);

            if (!somberFound && event.getEntity() instanceof Player) {
                somberFound = PlayerUtil.isEquippingSomber((Player) event.getEntity());
            }

            //checking damager has marked combo venom?
            boolean comboVenomFound = false;
            if (damager.hasMetadata("combo_venom")) {
                if (damager.getMetadata("combo_venom").get(0).asLong() > System.currentTimeMillis()) {
                    comboVenomFound = true;
                }
            }
            PlayerProfile damagerProfile = PlayerProfile.getPlayerProfileByUuid(damager.getUniqueId());

            //if found, dont process enchant

            for (IAttackEntity ins : enchantmentFactor.getAttackEntities()) {
                AbstractEnchantment enchant = (AbstractEnchantment) ins;
                if ((PlayerUtil.shouldIgnoreEnchant(damager) || (event.getEntity() instanceof Player && PlayerUtil.shouldIgnoreEnchant(damager, (Player) event.getEntity()))) && (enchant.getRarity() != EnchantmentRarity.DARK_NORMAL && enchant.getRarity() != EnchantmentRarity.DARK_RARE)) {
                    continue;
                }

                int level = enchant.getItemEnchantLevel(damager.getItemInHand());
                if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.AIR && damager.getItemInHand().getType() != Material.LEATHER_LEGGINGS) {
                    processAttackEntity(ins, level, damager, event.getEntity(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }
                if (damager.getInventory().getLeggings() != null && damager.getInventory().getLeggings().getType() != Material.AIR) {
                    level = enchant.getItemEnchantLevel(damager.getInventory().getLeggings());
                    processAttackEntity(ins, level, damager, event.getEntity(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }
            }


            for (IAttackEntity ins : questFactory.getAttackEntities()) {
                AbstractQuest quest = (AbstractQuest) ins;
                QuestData currentQuest = profile.getCurrentQuest();
                if (currentQuest != null && currentQuest.getInternalName().equals(quest.getQuestInternalName())) {
                    processAttackEntity(ins, currentQuest.getLevel(), damager, event.getEntity(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }
            }
        } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            damager = (Player) (((Projectile) event.getDamager()).getShooter());
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(damager.getUniqueId());

            if (NewConfiguration.INSTANCE.getNoobProtect() && profile.getPrestige() <= 0 && profile.getLevel() < NewConfiguration.INSTANCE.getNoobProtectLevel()) {
                boostDamage.getAndAdd(NewConfiguration.INSTANCE.getNoobDamageBoost() - 1);
            }

            for (IPlayerShootEntity ins : perkFactory.getPlayerShootEntities()) {
                AbstractPerk perk = (AbstractPerk) ins;
                int level = perk.getPlayerLevel(damager);
                processShootEntity(ins, level, damager, event.getEntity(), event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
            }

            //enchant handler
            //we should check has somber enchant on leggings
            boolean somberFound = PlayerUtil.isEquippingSomber(damager);
            if (!somberFound && event.getEntity() instanceof Player) {
                somberFound = PlayerUtil.isEquippingSomber((Player) event.getEntity());
            }
            //checking damager has marked combo venom?
            boolean comboVenomFound = false;
            if (damager.getMetadata("combo_venom") != null) {
                final long now = System.currentTimeMillis();
                final List<MetadataValue> metadata = damager.getMetadata("combo_venom");
                final Optional<MetadataValue> first = metadata.stream()
                        .filter(metadataValue -> metadataValue.getOwningPlugin() instanceof ThePit && now <= metadataValue.asLong())
                        .findFirst();
                if (first.isPresent()) {
                    comboVenomFound = true;
                }
            }
            //if found, dont process enchant

            final Projectile projectile = (Projectile) event.getDamager();
            if (projectile.hasMetadata("enchant")) {
                final MetadataValue value = projectile.getMetadata("enchant").get(0);
                final String enchants = value.asString();
                final String[] split = enchants.split(";");
                for (String enchantStr : split) {
                    final String[] enchStr = enchantStr.split(":");
                    final String enchantName = enchStr[0];
                    final int enchantValue = Integer.parseInt(enchStr[1]);

                    final AbstractEnchantment enchantment = enchantmentFactor.getEnchantmentMap().get(enchantName);

                    if ((PlayerUtil.shouldIgnoreEnchant(damager) || (event.getEntity() instanceof Player && PlayerUtil.shouldIgnoreEnchant(damager, (Player) event.getEntity()))) && (enchantment.getRarity() != EnchantmentRarity.DARK_NORMAL && enchantment.getRarity() != EnchantmentRarity.DARK_RARE)) {
                        continue;
                    }

                    this.processShootEntity((IPlayerShootEntity) enchantment, enchantValue, damager, event.getEntity(), event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }
            }


            for (IPlayerShootEntity ins : questFactory.getPlayerShootEntities()) {
                AbstractQuest quest = (AbstractQuest) ins;
                QuestData currentQuest = profile.getCurrentQuest();
                if (currentQuest != null && currentQuest.getInternalName().equals(quest.getQuestInternalName())) {
                    processShootEntity(ins, currentQuest.getLevel(), damager, event.getEntity(), event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }
            }
        }

        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getEntity().getUniqueId());

            if (NewConfiguration.INSTANCE.getNoobProtect() && profile.getPrestige() <= 0 && profile.getLevel() < NewConfiguration.INSTANCE.getNoobProtectLevel()) {
                boostDamage.getAndAdd(NewConfiguration.INSTANCE.getNoobDamageReduce() - 1);
            }

            if (PlayerUtil.isEquippingAngelChestplate(player)) {
                boostDamage.getAndAdd(-0.1);
            }

            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
                for (IPlayerDamaged ins : perkFactory.getPlayerDamageds()) {
                    AbstractPerk perk = (AbstractPerk) ins;
                    if (game.getDisabledPerks().stream().anyMatch(ignoredPerk -> ignoredPerk.getInternalPerkName().equals(perk.getInternalPerkName()))) {
                        continue;
                    }
                    int level = perk.getPlayerLevel(player);
                    processPlayerDamaged(ins, level, player, event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }

                //enchant handler
                //we should check has somber enchant on leggings
                boolean somberFound = PlayerUtil.isEquippingSomber(damager);

                if (!somberFound && event.getEntity() instanceof Player) {
                    somberFound = PlayerUtil.isEquippingSomber((Player) event.getEntity());
                }

                //if found, dont process enchant

                for (IPlayerDamaged ins : enchantmentFactor.getPlayerDamageds()) {
                    AbstractEnchantment enchant = (AbstractEnchantment) ins;

                    if ((PlayerUtil.shouldIgnoreEnchant(damager) || (event.getEntity() instanceof Player && PlayerUtil.shouldIgnoreEnchant(damager, (Player) event.getEntity()))) && (enchant.getRarity() != EnchantmentRarity.DARK_NORMAL && enchant.getRarity() != EnchantmentRarity.DARK_RARE)) {
                        continue;
                    }

                    //当神话之甲手持时不应该触发附魔
                    int level = enchant.getItemEnchantLevel(player.getItemInHand());
                    if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && player.getItemInHand().getType() != Material.LEATHER_LEGGINGS) {
                        processPlayerDamaged(ins, level, player, event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                    if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() != Material.AIR) {
                        level = enchant.getItemEnchantLevel(player.getInventory().getLeggings());
                        processPlayerDamaged(ins, level, player, event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                }


                for (IPlayerDamaged ins : questFactory.getPlayerDamageds()) {
                    AbstractQuest quest = (AbstractQuest) ins;
                    QuestData currentQuest = profile.getCurrentQuest();
                    if (currentQuest != null && currentQuest.getInternalName().equals(quest.getQuestInternalName())) {
                        processPlayerDamaged(ins, currentQuest.getLevel(), player, event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                }
            } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
                for (IPlayerDamaged ins : perkFactory.getPlayerDamageds()) {
                    AbstractPerk perk = (AbstractPerk) ins;
                    if (game.getDisabledPerks().stream().anyMatch(ignoredPerk -> ignoredPerk.getInternalPerkName().equals(perk.getInternalPerkName()))) {
                        continue;
                    }
                    int level = perk.getPlayerLevel(player);
                    processPlayerDamaged(ins, level, player, damager, event.getFinalDamage(), finalDamage, boostDamage, cancel);
                }


                //enchant handler
                //we should check has somber enchant on leggings
                boolean somberFound = PlayerUtil.isEquippingSomber(damager);

                if (!somberFound && event.getEntity() instanceof Player) {
                    somberFound = PlayerUtil.isEquippingSomber((Player) event.getEntity());
                }


                //if found, dont process enchant

                for (IPlayerDamaged ins : enchantmentFactor.getPlayerDamageds()) {
                    AbstractEnchantment enchant = (AbstractEnchantment) ins;
                    if ((PlayerUtil.shouldIgnoreEnchant(damager) || (event.getEntity() instanceof Player && PlayerUtil.shouldIgnoreEnchant(damager, (Player) event.getEntity()))) && (enchant.getRarity() != EnchantmentRarity.DARK_NORMAL && enchant.getRarity() != EnchantmentRarity.DARK_RARE)) {
                        continue;
                    }
                    int level = enchant.getItemEnchantLevel(player.getItemInHand());
                    if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && player.getItemInHand().getType() != Material.LEATHER_LEGGINGS) {
                        processPlayerDamaged(ins, level, player, event.getDamager(), event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                    if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() != Material.AIR) {
                        level = enchant.getItemEnchantLevel(player.getInventory().getLeggings());
                        processPlayerDamaged(ins, level, player, damager, event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                }


                for (IPlayerDamaged ins : questFactory.getPlayerDamageds()) {
                    AbstractQuest quest = (AbstractQuest) ins;
                    QuestData currentQuest = profile.getCurrentQuest();
                    if (currentQuest != null && currentQuest.getInternalName().equals(quest.getQuestInternalName())) {
                        processPlayerDamaged(ins, currentQuest.getLevel(), player, damager, event.getFinalDamage(), finalDamage, boostDamage, cancel);
                    }
                }
            }
        }

        if (cancel.get()) {
            event.setCancelled(true);

            assert damager != null;
        }

        event.setDamage(Math.max(1, event.getDamage()) * Math.max(0.2, boostDamage.get()));


        if (damager != null) {
            new PitDamageEvent(damager, event.getFinalDamage(), event.getDamage()).callEvent();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(damager.getUniqueId());
            List<KillRecap.DamageData> damageLogs = profile.getKillRecap().getDamageLogs();
            if (damageLogs.size() > 0) {
                damageLogs.get(damageLogs.size() - 1).setBoostDamage(boostDamage.get());
            }
        }


        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (damager != null) {
                new PitDamagePlayerEvent(damager, event.getFinalDamage(), event.getDamage(), player).callEvent();
            }

//            if (PlayerUtil.cantIgnore(player)) {
//                finalDamage.set(0.0);
//            }

            //mirror enchant code start
            if (player.getInventory().getLeggings() != null) {
                int enchantLevel = Utils.getEnchantLevel(player.getInventory().getLeggings(), "Mirror");
                if (enchantLevel > 1 && finalDamage.get() > 0 && finalDamage.get() < 1000) {
                    if (event.getDamager() instanceof Player && (!player.hasMetadata("mirror_latest_active") || (player.getMetadata("mirror_latest_active").size() > 0 && System.currentTimeMillis() - player.getMetadata("mirror_latest_active").get(0).asLong() > 500L))) {
                        //damage giveback
                        player.setMetadata("mirror_latest_active", new FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis()));
                        damager = (Player) event.getDamager();
                        if (!player.getUniqueId().equals(damager.getUniqueId())) {
                            damager.damage(0.01, player);
                        }
                        float mirrorDamage = (float) (((enchantLevel * 25 - 25) * 0.01) * finalDamage.get());
                        damager.setHealth(Math.max(0.1, damager.getHealth() - mirrorDamage));
                    }
                }
                if (enchantLevel > 0 && finalDamage.get() > 0 && finalDamage.get() < 1000) {
                    finalDamage.set(0);
                }
            }
            //mirror enchant code end
            player.setLastDamageCause(event);
            if (damager != null) {
                ((CraftPlayer) player).getHandle().killer = ((CraftPlayer) damager).getHandle();
            }

            if (player.getHealth() < finalDamage.get()) {
                player.damage(500000.0);
            } else {
                player.setHealth(Math.max(player.getHealth() - finalDamage.get(), 0));
            }
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if (profile.getPlayerOption().isDebugDamageMessage()) {
                player.sendMessage(CC.translate("&7受到伤害(Damage/Final Damage): &c" + numFormatTwo.format(event.getDamage()) + "&7/&c" + numFormatTwo.format(event.getFinalDamage())));
            }
        }
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if (profile.getPlayerOption().isDebugDamageMessage()) {
                player.sendMessage(CC.translate("&7造成伤害(Damage/Final Damage): &c" + numFormatTwo.format(event.getDamage()) + "&7/&c" + numFormatTwo.format(event.getFinalDamage())));
                final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                final double value = entityPlayer.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
                final float enchantDamage = EnchantmentManager.a(entityPlayer.bA(), ((CraftLivingEntity) event.getEntity()).getHandle().getMonsterType());
                final boolean critical = entityPlayer.fallDistance > 0.0F && !entityPlayer.onGround && !entityPlayer.k_() && !entityPlayer.V() && !entityPlayer.hasEffect(MobEffectList.BLINDNESS) && entityPlayer.vehicle == null;
                player.sendMessage(CC.translate("&7基础: &c" + value + "&7,附魔伤害: &c" + enchantDamage + "&7,暴击: &c" + critical));
            }
        }
    }

    @EventHandler
    public void onItemDamaged(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        for (IItemDamage itemDamage : ThePit.getInstance()
                .getEnchantmentFactor()
                .getiItemDamages()) {
            int level = ((AbstractEnchantment) itemDamage).getItemEnchantLevel(event.getPlayer().getItemInHand());

            itemDamage.handleItemDamaged(level, event.getItem(), event.getPlayer(), atomicBoolean);
        }
        if (atomicBoolean.get()) {
            event.setCancelled(true);
        }
    }

    @SneakyThrows
    private void processShootEntity(IPlayerShootEntity ins, int level, Player damager, Entity target, Entity damageSource, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (level != -1) {
            Method handleShootEntity = ins.getClass().getMethod("handleShootEntity", int.class, Player.class, Entity.class, double.class, AtomicDouble.class, AtomicDouble.class, AtomicBoolean.class);

            if (handleShootEntity.isAnnotationPresent(BowOnly.class) && !(damageSource instanceof Arrow)) {
                return;
            }
            if (handleShootEntity.isAnnotationPresent(ThrowOnly.class) && damager instanceof Arrow) {
                return;
            }
            if (ins.getClass().isAnnotationPresent(PlayerOnly.class) || handleShootEntity.isAnnotationPresent(PlayerOnly.class)) {
                if (target instanceof Player) {
                    Player player = (Player) target;
                    ins.handleShootEntity(level, damager, player, damage, finalDamage, boostDamage, cancel);
                }
            } else if (ins.getClass().isAnnotationPresent(NotPlayerOnly.class)) {
                if (!(target instanceof Player)) {
                    ins.handleShootEntity(level, damager, target, damage, finalDamage, boostDamage, cancel);
                }
            } else {
                ins.handleShootEntity(level, damager, target, damage, finalDamage, boostDamage, cancel);
            }
        }
    }

    @SneakyThrows
    private void processPlayerDamaged(IPlayerDamaged ins, int level, Player player, Entity damager, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (level != -1) {
            final Method method = ins.getClass().getMethod("handlePlayerDamaged", int.class, Player.class, Entity.class, double.class, AtomicDouble.class, AtomicDouble.class, AtomicBoolean.class);
            if (method.isAnnotationPresent(PlayerOnly.class)) {
                if (damager instanceof Player) {
                    Player damagerPlayer = (Player) damager;
                    ins.handlePlayerDamaged(level, player, damagerPlayer, damage, finalDamage, boostDamage, cancel);
                }
            } else if (method.isAnnotationPresent(NotPlayerOnly.class)) {
                if (!(damager instanceof Player)) {
                    ins.handlePlayerDamaged(level, player, damager, damage, finalDamage, boostDamage, cancel);
                }
            } else {
                ins.handlePlayerDamaged(level, player, damager, damage, finalDamage, boostDamage, cancel);
            }
        }
    }

    @SneakyThrows
    private void processAttackEntity(IAttackEntity ins, int level, Player damager, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (level != -1) {
            final Method method = ins.getClass().getMethod("handleAttackEntity", int.class, Player.class, Entity.class, double.class, AtomicDouble.class, AtomicDouble.class, AtomicBoolean.class);
            if (method.isAnnotationPresent(PlayerOnly.class)) {
                if (target instanceof Player) {
                    Player player = (Player) target;
                    ins.handleAttackEntity(level, damager, player, damage, finalDamage, boostDamage, cancel);
                }
            } else if (method.isAnnotationPresent(NotPlayerOnly.class)) {
                if (!(target instanceof Player) && damager == null) {
                    ins.handleAttackEntity(level, damager, target, damage, finalDamage, boostDamage, cancel);
                }
            } else {
                ins.handleAttackEntity(level, damager, target, damage, finalDamage, boostDamage, cancel);
            }
        }
    }

}
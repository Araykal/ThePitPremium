package cn.charlotte.pit.util;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.event.PitPotionEffectEvent;
import cn.charlotte.pit.event.PitRegainHealthEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.mode.Mode;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.MegaStreak;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.command.CommandData;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 22:30
 */
public class PlayerUtil {

    static {
        CommandData.classes.addAll(Arrays.asList(
                EventHandler.class,
                InventoryUtil.class,
                Math.class,
                Player.class
        ));
    }

    public static String getActiveMegaStreak(Player player) {
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isLoaded()) {
            return null;
        }

        if (profile.getChosePerk().get(5) == null) {
            return null;
        }

        final Optional<AbstractPerk> first = ThePit.getInstance()
                .getPerkFactory()
                .getPerks()
                .stream()
                .filter(abstractPerk -> abstractPerk.getPerkType() == PerkType.MEGA_STREAK && abstractPerk.getInternalPerkName().equals(profile.getChosePerk().get(5).getPerkInternalName()))
                .findFirst();

        if (first.isPresent()) {
            final AbstractPerk perk = first.get();
            if (perk instanceof MegaStreak) {
                if (profile.getStreakKills() >= ((MegaStreak) perk).getStreakNeed()) {
                    return CC.translate(perk.getDisplayName());
                }
            }
        }
        return null;
    }
    public static void playThunderEffect(Location thunderLocation) {
        EntityLightning lightning = new EntityLightning(
                ((CraftWorld) thunderLocation.getWorld()).getHandle(), thunderLocation.getX(), thunderLocation.getY(), thunderLocation.getZ(), true, true
        );

        PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(lightning);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static boolean isVenom(Player player) {
        return player.hasMetadata("combo_venom") && player.getMetadata("combo_venom").get(0).asLong() > System.currentTimeMillis();
    }

    public static boolean isEquippingSomber(Player player) {
        return player.getInventory().getLeggings() != null && ThePit.getApi().getItemEnchantLevel(player.getInventory().getLeggings(), "somber_enchant") > 0;
    }

    public static boolean isCritical(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        return entityPlayer.fallDistance > 0.0F && !entityPlayer.onGround && !entityPlayer.k_() && !entityPlayer.V() && !entityPlayer.hasEffect(MobEffectList.BLINDNESS) && entityPlayer.vehicle == null;
    }

    //如果自身穿着黑裤/被毒则无法使用附魔 (适用于无目标附魔)
    public static boolean shouldIgnoreEnchant(Player self) {
        if (PlayerUtil.isSinkingMoonlight(self)) return true;
        return isEquippingSomber(self) || isVenom(self) || isSinkingMoonlight(self);
    }

    //自身对其他人使用附魔时附魔是否应该失效 (适用于有目标附魔)
    public static boolean shouldIgnoreEnchant(Player self, Player target) {
        if (PlayerUtil.isSinkingMoonlight(self)) return true;

        //自身穿黑裤时必定失效 && 自身被沉默时必定生效 && 其他人穿黑裤且自身没有鞋子时失效 && 对方被沉默时失效
        return isEquippingSomber(self) || isVenom(self) || isSinkingMoonlight(self) || (isEquippingSomber(target) && !isEquippingArmageddon(self)) || isVenom(target);
    }

    public static boolean isSinkingMoonlight(Player player) {
        return player.hasMetadata("sinking_moonlight") && player.getMetadata("sinking_moonlight").get(0).asLong() > System.currentTimeMillis();
    }

    public static boolean isEquippingArmageddon(Player player) {
        return "armageddon_boots".equals(ItemUtil.getInternalName(player.getInventory().getBoots()));
    }

    public static boolean isEquippingAngelChestplate(Player player) {
        return "angel_chestplate".equals(ItemUtil.getInternalName(player.getInventory().getChestplate()));
    }

    /**
     * @param player
     * @return if player is vanished (SuperVanish Plugin based)
     */
    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    /**
     * 对玩家造成伤害,仅判断伤害能否被造成与造成伤害,不进行其他操作
     *
     * @param victim     受到伤害的目标
     * @param damageType 伤害类型
     * @param damage     伤害量
     * @param canImmune  此伤害能否被免疫 / 降低
     * @return 此伤害是否被免疫
     */
    public static boolean damage(Player victim, DamageType damageType, Double damage, Boolean canImmune) {
        boolean immune = false;
        if (victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType() != Material.AIR) {
            immune = canImmune && ThePit.getApi().getItemEnchantLevel(victim.getInventory().getLeggings(), "Mirror") >= 1;
        }
        if (immune && damageType == DamageType.TRUE) return true;
        switch (damageType) {
            case NORMAL:
                victim.damage(damage);
                break;
            case TRUE:
                if (victim.getHealth() > damage) {
                    victim.setHealth(Math.min(victim.getMaxHealth(), Math.max(victim.getHealth() - damage, 0.0)));
                } else {
                    victim.damage(victim.getMaxHealth() * 100);
                }
                break;
        }
        return false;
    }

    public static void damage(Entity victim, DamageType damageType, Double damage, Boolean canImmune) {

    }

    public static boolean cantIgnore(Player player) {
        return !(player.hasMetadata("true_damage_immune_ignore_immune") && player.getMetadata("true_damage_immune_ignore_immune").size() > 0 && player.hasMetadata("mirror_latest_active") && System.currentTimeMillis() < player.getMetadata("mirror_latest_active").get(0).asLong());
    }

    /**
     * 对玩家造成来源类型为玩家的伤害
     *
     * @param attacker   伤害来源(类型玩家)
     * @param victim     受到伤害的目标
     * @param damageType 伤害类型
     * @param damage     伤害量
     * @param canImmune  此伤害能否被免疫 / 降低
     * @return 此伤害是否被免疫
     */
    public static void damage(Player attacker, Player victim, DamageType damageType, Double damage, Boolean canImmune) {
        boolean immune = damage(victim, damageType, damage, canImmune);
        if (immune) {
            //Mirror附魔反弹伤害
            if (damageType == DamageType.TRUE && victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType() != Material.AIR) {
                int level = ThePit.getApi().getItemEnchantLevel(victim.getInventory().getLeggings(), "Mirror");
                if (level >= 2) damage(attacker, damageType, damage * (0.25 * level - 0.25), false);
            }
        }
    }

    public static void damage(Player attacker, Entity victim, DamageType damageType, Double damage, Boolean canImmune) {

    }

    public static boolean isStaffSpectating(Player player) {
        return false;
    }

    public static boolean isPlayerChosePerk(Player player, String internal) {
        if (player == null) return false;
        if (UtilKt.hasRealMan(player)) return false;
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        for (Map.Entry<Integer, PerkData> entry : profile.getChosePerk().entrySet()) {
            if (entry.getValue().getPerkInternalName().equals(internal)) {
                return true;
            }
        }
        return false;
    }

    public static int getPlayerHealItemLimit(Player player) {
        int limit = 2;
        boolean vampirePresent = PlayerUtil.isPlayerChosePerk(player, "Vampire");
        boolean ramboPresent = PlayerUtil.isPlayerChosePerk(player, "rambo");
        boolean overHeal = isPlayerChosePerk(player, "OverHeal");
        boolean olympusPresent = PlayerUtil.isPlayerChosePerk(player, "Olympus");
        int overHealEnchant = ThePit.getApi().getItemEnchantLevel(player.getInventory().getLeggings(), "over_heal_enchant");
        if (olympusPresent) {
            limit = 1;
        }
        if (overHeal) {
            limit += limit;
        }
        if (overHealEnchant > 0 && !isEquippingSomber(player) && !isVenom(player)) {
            limit += overHealEnchant;
        }
        if (ramboPresent || vampirePresent) {
            limit = -999;
        }
        return limit;
    }

    public static int getPlayerHealItemAmount(Player player) {
        int amount = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (ItemUtil.isHealingItem(itemStack)) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
    }

    public static int getAmountOfActiveHealingPerk(Player player) {
        boolean vampirePresent = PlayerUtil.isPlayerChosePerk(player, "Vampire");
        boolean goldenHeadPresent = PlayerUtil.isPlayerChosePerk(player, "GoldenHead");
        boolean ramboPresent = PlayerUtil.isPlayerChosePerk(player, "rambo");
        boolean olympusPresent = PlayerUtil.isPlayerChosePerk(player, "Olympus");
        boolean tastySoupPresent = PlayerUtil.isPlayerChosePerk(player, "tasty_soup_perk");
        int amount = 0;
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (vampirePresent || ramboPresent || !profile.isInArena()) {
            return Integer.MAX_VALUE;
        }
        if (goldenHeadPresent) {
            amount++;
        }
        if (olympusPresent) {
            amount++;
        }
        if (tastySoupPresent) {
            amount++;
        }
        return amount;
    }

    public static boolean isPlayerUnlockedPerk(Player player, String internal) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        return profile.getUnlockedPerkMap().get(internal) != null;
    }

    public static float getDistance(Player p1, Player p2) {
        Location lc1 = p1.getLocation();
        Location lc2 = p2.getLocation();
        if (lc1.getWorld() != lc2.getWorld()) {
            return Float.MAX_VALUE;
        }
        return (float) Math.sqrt(Math.pow(lc1.getX() - lc2.getX(), 2) + Math.pow(lc1.getY() - lc2.getY(), 2) + Math.pow(lc1.getZ() - lc2.getZ(), 2));
    }

    public static float getDistance(Location lc1, Location lc2) {
        if (lc1.getWorld() != lc2.getWorld()) {
            return Float.MAX_VALUE;
        }
        return (float) Math.sqrt(Math.pow(lc1.getX() - lc2.getX(), 2) + Math.pow(lc1.getY() - lc2.getY(), 2) + Math.pow(lc1.getZ() - lc2.getZ(), 2));
    }

    public static int getPlayerUnlockedPerkLevel(Player player, String internal) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PerkData data = profile.getUnlockedPerkMap().get(internal);
        if (data == null) {
            return 0;
        }
        return data.getLevel();
    }

    public static boolean isPlayerBoughtPerk(Player player, String internal) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        return profile.getBoughtPerkMap().containsKey(internal);
    }

    public static void deadPlayer(Player player) {
        PlayerUtil.damage(player, PlayerUtil.DamageType.TRUE, player.getMaxHealth() * 100, false);
    }

    public static int getPlayerArmorDefense(Player player) {
        int defense = 0;
        ItemStack[] is = player.getInventory().getArmorContents();
        for (ItemStack i : is) {
            if (i.getType() == Material.LEATHER_HELMET || i.getType() == Material.LEATHER_BOOTS || i.getType() == Material.GOLD_BOOTS || i.getType() == Material.CHAINMAIL_BOOTS) {
                defense += 1;
            }
            if (i.getType() == Material.LEATHER_LEGGINGS || i.getType() == Material.GOLD_HELMET || i.getType() == Material.CHAINMAIL_HELMET || i.getType() == Material.IRON_HELMET) {
                defense += 2;
            }
            if (i.getType() == Material.LEATHER_CHESTPLATE || i.getType() == Material.GOLD_LEGGINGS || i.getType() == Material.DIAMOND_HELMET || i.getType() == Material.DIAMOND_BOOTS) {
                defense += 3;
            }
            if (i.getType() == Material.CHAINMAIL_LEGGINGS) {
                defense += 4;
            }
            if (i.getType() == Material.GOLD_CHESTPLATE || i.getType() == Material.CHAINMAIL_CHESTPLATE || i.getType() == Material.IRON_LEGGINGS) {
                defense += 5;
            }
            if (i.getType() == Material.IRON_CHESTPLATE || i.getType() == Material.DIAMOND_LEGGINGS) {
                defense += 6;
            }
            if (i.getType() == Material.DIAMOND_CHESTPLATE) {
                defense += 8;
            }
        }
        return Math.min(20, defense);
    }

    public static Player getStaffSpectating(Player player) {
        return player.hasMetadata("STAFF_SPECTATOR") ? (Player) player.getMetadata("STAFF_SPECTATOR").get(0).value() : null;
    }

    public static void clearStaffSpectateTarget(Player player) {
        if (isStaffSpectating(player)) {
            player.removeMetadata("STAFF_SPECTATOR", ThePit.getInstance());
        }
    }

    public static void setStaffSpectateTarget(Player player, Player target) {
        if (isStaffSpectating(player)) {
            player.setMetadata("STAFF_SPECTATOR", new FixedMetadataValue(ThePit.getInstance(), target));
        }
    }

    public static boolean isStaff(Player player) {
        return player.hasPermission(getStaffPermission()) || player.hasPermission("pit.admin");
    }

    public static String getStaffPermission() {
        return "pit.staff.default";
    }

    public static void setFirstSlotOfType(Player player, Material type, ItemStack itemStack) {
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack itemStack1 = player.getInventory().getContents()[i];
            if (itemStack1 == null || itemStack1.getType() == type || itemStack1.getType() == Material.AIR) {
                player.getInventory().setItem(i, itemStack);
                break;
            }
        }
    }

    public static int getPing(Player player) {
        int ping = ((CraftPlayer) player).getHandle().ping;

        if (ping >= 100) {
            return ping - 30;
        }

        if (ping >= 50) {
            return ping - 20;
        }

        if (ping >= 20) {
            return ping - 10;
        }

        return ping;
    }

    public static void clearPlayer(Player player) {
        clearPlayer(player, true, true);
    }

    public static void clearPlayer(Player player, boolean closeInventor) {
        clearPlayer(player, closeInventor, true);
    }

    public static void clearPlayer(Player player, boolean closeInventory, boolean clearInventory) {
        player.setSaturation(12.8F);
        player.setMaximumNoDamageTicks(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.getInventory().setHeldItemSlot(0);
        player.setAllowFlight(false);
        player.setCanPickupItems(true);
        if (clearInventory) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            player.setItemOnCursor(new ItemStack(Material.AIR));

            player.getEnderChest().clear();
        }
        if (closeInventory) {
            player.closeInventory();
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.getDataWatcher().watch(9, (byte) 0);
        entityPlayer.setAbsorptionHearts(0.0F);

        //apply stats - start
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (ThePit.mode == Mode.Myhic) {
            player.setFoodLevel(profile.getFoodLevel());
        }
        //temp disable
        player.setWalkSpeed(0.2F);

        double extraMaxHealth = profile.getExtraMaxHealthValue();

        if (ThePit.getInstance().getEventFactory().getActiveEpicEvent() != null && ((IEvent) ThePit.getInstance().getEventFactory().getActiveEpicEvent()).getEventInternalName().equals("rage_pit")) {
            player.setMaxHealth(40.0 + extraMaxHealth);
        } else {
            player.setMaxHealth(profile.getMaxHealth());
        }
        //apply stats - end
        //heal player
        player.setHealth(player.getMaxHealth());

        player.updateInventory();
    }

    public static void sendMessage(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public static void sendMessage(String message, Set<Player> players) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public static void lightningEffect(Player player, Player target) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(new EntityLightning(((CraftPlayer) target).getHandle().getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), true, false)));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 100F, 100));
    }

    public static void lightningEffect(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(new EntityLightning(((CraftPlayer) player).getHandle().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), true, false)));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1.0F, 63));
    }

    public static void sendFirework(FireworkEffect effect, Location location) {
        Firework f = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(effect);
        f.setFireworkMeta(fm);

        try {
            Class<?> entityFireworkClass = getClass("net.minecraft.server.", "EntityFireworks");
            Class<?> craftFireworkClass = getClass("org.bukkit.craftbukkit.", "entity.CraftFirework");
            Object firework = craftFireworkClass.cast(f);
            Method handle = firework.getClass().getMethod("getHandle");
            Object entityFirework = handle.invoke(firework);
            Field expectedLifespan = entityFireworkClass.getDeclaredField("expectedLifespan");
            Field ticksFlown = entityFireworkClass.getDeclaredField("ticksFlown");
            ticksFlown.setAccessible(true);
            ticksFlown.setInt(entityFirework, expectedLifespan.getInt(entityFirework) - 1);
            ticksFlown.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void respawnPlayer(PlayerDeathEvent event) {
        new BukkitRunnable() {
            public void run() {
                try {
                    Object nmsPlayer = event.getEntity().getClass().getMethod("getHandle").invoke(event.getEntity());
                    Object con = nmsPlayer.getClass().getDeclaredField("playerConnection").get(nmsPlayer);

                    Class<?> EntityPlayer = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EntityPlayer");

                    Field minecraftServer = con.getClass().getDeclaredField("minecraftServer");
                    minecraftServer.setAccessible(true);
                    Object mcserver = minecraftServer.get(con);

                    Object playerlist = mcserver.getClass().getDeclaredMethod("getPlayerList").invoke(mcserver);
                    Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", EntityPlayer, int.class, boolean.class);
                    moveToWorld.invoke(playerlist, nmsPlayer, 0, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskLater(ThePit.getInstance(), 2L);
    }

    private static Class<?> getClass(String prefix, String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = prefix + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    public static List<Player> getNearbyPlayers(Location location, double radius) {
        List<Player> players = new ArrayList<>();
        for (org.bukkit.entity.Entity e : location.getWorld()
                .getNearbyEntities(location, radius, radius, radius)) {
            if (e instanceof Player && e.getLocation().distance(location) <= radius) {
                players.add((Player) e);
            }
        }
        return players;
    }

    public static void heal(Player player, double heal) {
        PitRegainHealthEvent event = new PitRegainHealthEvent(player, heal);
        event.callEvent();
        if (event.isCancelled()) {
            return;
        }
        heal = Math.max(event.getAmount(), 0);
        player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
    }

    public static void food(Player player, int level) {
        player.setFoodLevel(
                Math.min(player.getFoodLevel() + level, 20));
    }

    public static void takeOneItemInHand(Player player) {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack.getAmount() == 1) {
            player.setItemInHand(null);
            return;
        }
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setItemInHand(itemStack);
    }

    public static void denyMovement(Player player) {
        player.setFlying(false);
        player.setWalkSpeed(0.0F);
        player.setFoodLevel(0);
        player.setSprinting(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
    }

    public static void allowMovement(Player player) {
        player.setFlying(false);
        player.setWalkSpeed(0.2F);
        player.setFoodLevel(20);
        player.setSprinting(true);
        player.removePotionEffect(PotionEffectType.JUMP);
    }

    public static void deathEffect(Player player, Player attacker) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

        EntityPlayer npc;

        npc = new EntityPlayer(server, world, new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager(world));

        Location loc = player.getLocation();
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        PlayerConnection connection = ((CraftPlayer) attacker).getHandle().playerConnection;
        // connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityStatus(npc, (byte) 3));
    }

    public static void ewplayThunderEffect(Location thunderLocation) {
        EntityLightning lightning = new EntityLightning(
                ((CraftWorld) thunderLocation.getWorld()).getHandle(), thunderLocation.getX(), thunderLocation.getY(), thunderLocation.getZ(), true, true
        );

        PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(lightning);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void addPotionEffect(Player player, PotionEffect effect) {
        PlayerUtil.addPotionEffect(player, effect, false);
    }

    public static void addPotionEffect(Player player, PotionEffect effect, boolean force) {
        PitPotionEffectEvent event = new PitPotionEffectEvent(player, effect);
        event.callEvent();
        if (event.isCancelled()) {
            return;
        }
        event.getPlayer().addPotionEffect(event.getPotionEffect(), force);
    }
    public static void sendParticle(Location location, EnumParticle particle, int count) {
        new ParticleBuilder(location, particle).setCount(count).play();
    }
    public enum DamageType {
        NORMAL,
        TRUE
    }
}

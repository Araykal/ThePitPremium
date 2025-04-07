package cn.charlotte.pit.perk.type.streak.grandfinale;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.item.type.ChunkOfVileItem;
import cn.charlotte.pit.item.type.mythic.MythicBowItem;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.item.type.mythic.MythicSwordItem;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.random.RandomUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/25 16:21
 */
@AutoRegister
public class ApostleForTheGesusKillStreak extends AbstractPerk implements Listener, IAttackEntity, IPlayerShootEntity {

    public static final HashMap<UUID, Cooldown> strength = new HashMap<>();

    @Override
    public String getInternalPerkName() {
        return "rn_gesus_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "RNGesus的使徒";
    }

    @Override
    public Material getIcon() {
        return Material.EYE_OF_ENDER;
    }

    @Override
    //50000
    public double requireCoins() {
        return 50000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 100;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c25 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时进行一次掷骰 (1~100) 并按结果获得物品/效果:");
        list.add("&7(满足条件的项目全部获得,触发顺序从上到下)");
        list.add("  &e▶ &727 或 43 : &e+1 声望");
        list.add("  &e▶ &745 : &6+10❤ 生命吸收");
        list.add("  &e▶ &750 : &b钻石头盔");
        list.add("  &e▶ &713 或 66 : &5+1 暗聚块");
        list.add("  &e▶ &777 或 88 : &d+1 随机神话掉落物品");
        list.add("  &e▶ &799 : &7受到 &c4❤ &c必中&7伤害");
        list.add("  &e▶ &72 的倍数: &c生命恢复 I &f(00:20)");
        list.add("  &e▶ &73 的倍数: &b速度 I &f(00:15)");
        list.add("  &e▶ &74 的倍数: &c生命恢复 II &f(00:10)");
        list.add("  &e▶ &77 的倍数: &6500 ~ 5000 硬币");
        list.add("  &e▶ &78 的倍数: &b500 ~ 5000 经验值");
        list.add("  &e▶ &712 的倍数 : &6+4❤ 生命吸收");
        list.add("  &e▶ &713 的倍数 : &c+25% 攻击伤害 &f(00:30)");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.KILL_STREAK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onStreak(PitStreakKillChangeEvent event) {
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        PlayerProfile profile = event.getPlayerProfile();
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        //trigger check (every X streak)
        int streak = 25;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            //1d100
            int dice = RandomUtil.random.nextInt(100) + 1;
            myself.sendMessage(CC.translate("&e&l掷骰! &7你掷到了 &e" + dice + " &7!"));
            if (dice == 27 || dice == 43) {
                profile.setRenown(profile.getRenown() + 1);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &e1 声望 &7."));
            } else if (dice == 45) {
                float heart = (((CraftPlayer) myself).getHandle()).getAbsorptionHearts();
                (((CraftPlayer) myself).getHandle()).setAbsorptionHearts(heart + 20);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &610❤ 伤害吸收效果 &7."));
            } else if (dice == 50) {
                myself.getInventory().addItem(
                        new ItemBuilder(Material.DIAMOND_HELMET)
                                .canTrade(true)
                                .canSaveToEnderChest(true)
                                .deathDrop(true)
                                .lore("&e掷骰获得")
                                .internalName("dice_award")
                                .buildWithUnbreakable()
                );
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &b钻石头盔 &7."));
            } else if (dice == 13 || dice == 66) {
                myself.getInventory().addItem(ChunkOfVileItem.toItemStack());
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &51 暗聚块 &7."));
            } else if (dice == 77 || dice == 88) {
                myself.getInventory().addItem((ItemStack) RandomUtil.helpMeToChooseOne(
                        new MythicSwordItem().toItemStack(),
                        new MythicBowItem().toItemStack(),
                        new MythicLeggingsItem().toItemStack()
                ));
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &d随机神话物品 &7."));
            } else if (dice == 99) {
                PlayerUtil.damage(myself, PlayerUtil.DamageType.TRUE, 8D, false);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &c4❤ 必中伤害 &7. :("));
            }
            if (dice % 2 == 0) {
                myself.removePotionEffect(PotionEffectType.REGENERATION);
                myself.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 0), false);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &c生命恢复 I &f(00:20) &7."));
            }
            if (dice % 3 == 0) {
                myself.removePotionEffect(PotionEffectType.SPEED);
                myself.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 0), false);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &b速度 I &f(00:15) &7."));
            }
            if (dice % 4 == 0) {
                myself.removePotionEffect(PotionEffectType.REGENERATION);
                myself.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1), false);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &c生命恢复 II &f(00:10) &7."));
            }
            if (dice % 7 == 0) {
                int diceCoins = RandomUtil.random.nextInt(4501) + 500;
                profile.setCoins(profile.getCoins() + diceCoins);
                profile.grindCoins(diceCoins);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &6" + diceCoins + " 硬币 &7."));
            }
            if (dice % 8 == 0) {
                int diceExperience = RandomUtil.random.nextInt(4501) + 500;
                profile.setExperience(profile.getExperience() + diceExperience);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &b" + diceExperience + " 经验值 &7."));
            }
            if (dice % 12 == 0) {
                float heart = (((CraftPlayer) myself).getHandle()).getAbsorptionHearts();
                (((CraftPlayer) myself).getHandle()).setAbsorptionHearts(heart + 8);
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &64❤ 伤害吸收效果 &7."));
            }
            if (dice % 13 == 0) {
                strength.put(myself.getUniqueId(), new Cooldown(30, TimeUnit.SECONDS));
                myself.sendMessage(CC.translate("&e&l掷骰! &7你获得了 &c+25% 攻击伤害 &f(00:30) &7."));
            }
        }
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (!strength.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            boostDamage.getAndAdd(0.25);
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (!strength.getOrDefault(attacker.getUniqueId(), new Cooldown(0)).hasExpired()) {
            boostDamage.getAndAdd(0.25);
        }
    }
}

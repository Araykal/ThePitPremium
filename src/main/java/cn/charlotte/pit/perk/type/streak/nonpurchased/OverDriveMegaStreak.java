package cn.charlotte.pit.perk.type.streak.nonpurchased;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IPlayerBeKilledByEntity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.MegaStreak;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/14 23:37
 */
@AutoRegister
public class OverDriveMegaStreak extends AbstractPerk implements Listener, IPlayerKilledEntity, ITickTask, IPlayerDamaged, IPlayerBeKilledByEntity, MegaStreak {
    @Override
    public String getInternalPerkName() {
        return "over_drive";
    }

    @Override
    public String getDisplayName() {
        return "&e超速传动";
    }

    @Override
    public Material getIcon() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public double requireCoins() {
        return 0;
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
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.MEGA_STREAK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7激活要求连杀数: &c50 连杀");
        list.add(" ");
        list.add("&7激活后:");
        list.add("  &a▶ &7获得 &b速度 I &6&o持续时间无限");
        list.add("  &a▶ &7击杀获得 &6+50% 硬币");
        list.add("  &a▶ &7击杀获得 &b+100% 经验值");
        list.add("  &c▶ &7激活后每击杀5名玩家,受击时");
        list.add("  &7额外受到 &c+0.1❤ &7的&c必中&7伤害 (可堆叠,无上限)");
        list.add("  &c(必中伤害无法被免疫与抵抗)");
        list.add(" ");
        list.add("&7激活后死亡时:");
        list.add("  &a▶ &7获得 &b4,000 经验值");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    //MegaStreak
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onStreak(PitStreakKillChangeEvent event) {
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        int trigger = 50;
        //trigger check (get X streak)
        if (event.getFrom() < trigger && event.getTo() >= trigger) {
            CC.boardCast(MessageType.COMBAT, "&c&l超级连杀! " + event.getPlayerProfile().getFormattedNameWithRoman() + " &7激活了 &e&l超速传动 &7!");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 0.8F, 1.5F));
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            coins.getAndAdd(0.5 * coins.get());
            experience.getAndAdd(experience.get());
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isInArena()) {
            profile.setStreakKills(0);
        }
        boolean speedFound = false;
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getName().equalsIgnoreCase("speed")) {
                speedFound = true;
                //自身有速度 I 且时间小于<30秒时直接覆盖
                if (potionEffect.getAmplifier() == 0 && potionEffect.getDuration() < 30 * 20 && profile.getStreakKills() >= 50) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 86400 * 20, 0), true);
                    return;
                }
                //如果不满足连杀条件有时间过长的速度 I 则直接清除
                if (potionEffect.getAmplifier() == 0 && potionEffect.getDuration() > 8640 * 20 && profile.getStreakKills() < 50) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    return;
                }
            }
        }
        //自身没有速度效果且连杀条件满足时补充速度效果
        if (profile.getStreakKills() >= 50 && !speedFound) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 86400 * 20, 0), true);
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 3 * 20;
    }

    @Override
    public void handlePlayerBeKilledByEntity(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            profile.setExperience(profile.getExperience() + 4000);
            myself.sendMessage(CC.translate("&e&l超速传动! &7你获得了 &b4,000 &7经验值."));
        }
    }


    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() > 50 && profile.getChosePerk().get(5) != null && profile.getChosePerk().get(5).getPerkInternalName().equals(this.getInternalPerkName())) {
            double tier = (profile.getStreakKills() - 50 - (profile.getStreakKills() - 50) % 5) / 5;
            if (myself.getHealth() > tier * 0.2) {
                myself.setHealth(Math.max(0.1, myself.getHealth() - tier * 0.2));
            } else {
                cancel.set(true);
                finalDamage.getAndAdd(9999);
            }
        }
    }

    @Override
    public int getStreakNeed() {
        return 50;
    }
}

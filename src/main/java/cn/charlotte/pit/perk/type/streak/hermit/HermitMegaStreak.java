package cn.charlotte.pit.perk.type.streak.hermit;

import cn.charlotte.pit.ThePit;
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
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Misoryan
 * @Date 2022/11/22 19:00
 */
@AutoRegister
public class HermitMegaStreak extends AbstractPerk implements Listener, IPlayerDamaged, ITickTask, IPlayerKilledEntity, IPlayerBeKilledByEntity, MegaStreak {

    @Override
    public String getInternalPerkName() {
        return "hermit";
    }

    @Override
    public String getDisplayName() {
        return "&9隐士";
    }

    @Override
    public Material getIcon() {
        return Material.BED;
    }

    @Override
    public double requireCoins() {
        return 20000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 4;
    }

    @Override
    public int requireLevel() {
        return 50;
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
        list.add("&7装备此连杀时:");
        list.add("  &a▶ &7放置方块的持续时间 &a+100%");
        list.add("  &c▶ &7获得 &c缓慢 I &6&o持续时间无限");
        list.add(" ");
        list.add("&7激活后:");
        list.add("  &a▶ &7获得 &3抗性提升 I &6&o持续时间无限");
        list.add("  &a▶ &7获得状态 &a真实伤害抗性 &6&o持续时间无限");
        list.add("  &a▶ &7立刻获得 &f32 * &c基岩");
        list.add("  &a▶ &7激活后每击杀10名玩家,获得 &f16 * &c基岩 &7");
        list.add("  &a▶ &7激活后每击杀10名玩家,此后击杀获得的硬币与经验值 &a+5% &7(可堆叠,最高75%)");
        list.add("  &c▶ &7激活后每击杀1名玩家,自身受到的伤害 &c+0.3% (可堆叠,无上限)");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 0), true);
    }

    @Override
    public void onPerkInactive(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }

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
            CC.boardCast(MessageType.COMBAT, "&c&l超级连杀! " + event.getPlayerProfile().getFormattedNameWithRoman() + " &7激活了 &9&l隐士 &7!");
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 0.8F, 1.5F);
            });
            myself.getInventory().addItem(new ItemBuilder(Material.BEDROCK).canDrop(false).removeOnJoin(true).canSaveToEnderChest(false).deathDrop(true).internalName("perk_hermit").amount(32).build());
        }
        if (event.getFrom() > trigger && Math.floor(event.getFrom() / 10) != Math.floor(event.getTo() / 10)) {
            myself.getInventory().addItem(new ItemBuilder(Material.BEDROCK).canDrop(false).removeOnJoin(true).canSaveToEnderChest(false).deathDrop(true).internalName("perk_hermit").amount(32).build());
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            coins.getAndAdd(Math.min(0.75, Math.floor((profile.getStreakKills() - 50) / 10)) * coins.get());
            experience.getAndAdd(Math.min(0.75, Math.floor((profile.getStreakKills() - 50) / 10)) * experience.get());
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isInArena()) {
            profile.setStreakKills(0);
        }
        boolean slowFound = false;
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getName().equalsIgnoreCase("slow")) {
                slowFound = true;
                //自身有缓慢 I 且时间小于<30秒时直接覆盖
                if (potionEffect.getAmplifier() == 0 && potionEffect.getDuration() < 30 * 20) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 0), true);
                    return;
                }
            }
        }
        //自身没有缓慢效果时补充缓慢效果
        if (!slowFound) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 86400 * 20, 0), true);
        }
        boolean resistanceFound = false;
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getName().equalsIgnoreCase("speed")) {
                resistanceFound = true;
                //自身有抗性提升 I 且时间小于<30秒时直接覆盖
                if (potionEffect.getAmplifier() == 0 && potionEffect.getDuration() < 30 * 20 && profile.getStreakKills() >= 50) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 86400 * 20, 0), true);
                    return;
                }
                //如果不满足连杀条件有时间过长的抗性提升 I 则直接清除
                if (potionEffect.getAmplifier() == 0 && potionEffect.getDuration() > 8640 * 20 && profile.getStreakKills() < 50) {
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    return;
                }
            }
        }
        //自身没有速度效果且连杀条件满足时补充速度效果
        if (profile.getStreakKills() >= 50 && !resistanceFound) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 86400 * 20, 0), true);
        }
        if (profile.getStreakKills() >= 50) {
            player.setMetadata("true_damage_immune", new FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis() + 4 * 1000L));
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 3 * 20;
    }

    @Override
    @PlayerOnly
    public void handlePlayerBeKilledByEntity(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
        }
    }


    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() > 50) {
            boostDamage.getAndAdd(Math.max(0.75, 0.003 * (profile.getStreakKills() - 50)));
        }
    }

    @Override
    public int getStreakNeed() {
        return 50;
    }
}

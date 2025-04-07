package cn.charlotte.pit.perk.type.streak.beastmode;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Creator Misoryan
 * @Date 2021/4/27 17:11
 */
@AutoRegister
public class BeastModeMegaStreak extends AbstractPerk implements Listener, ITickTask, IPlayerKilledEntity, IAttackEntity, IPlayerShootEntity, IPlayerBeKilledByEntity, MegaStreak {

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
            CC.boardCast(MessageType.COMBAT, "&c&l超级连杀! " + event.getPlayerProfile().getFormattedNameWithRoman() + " &7激活了 &a&l野兽模式 &7!");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 0.8F, 1.5F));
            myself.getInventory().addItem(
                    new ItemBuilder(Material.DIAMOND_HELMET)
                            .removeOnJoin(true)
                            .deathDrop(true)
                            .lore(
                                    "&7超级连杀物品",
                                    "&c激活连杀后死亡保留",
                                    "&c使用指令回城或重新进入房间后消失"
                            )
                            .internalName("beast_mode_helmet")
                            .buildWithUnbreakable()
            );
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (!profile.isInArena()) {
            profile.setStreakKills(0);
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 3 * 20;
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            boostDamage.getAndAdd(0.25);
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            coins.getAndAdd(0.5 * coins.get());
            experience.getAndAdd(0.75 * experience.get());
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            boostDamage.getAndAdd(0.25);
        }
    }

    @Override
    public String getInternalPerkName() {
        return "beast_mode_mega_streak";
    }

    @Override
    public String getDisplayName() {
        return "&a野兽模式";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public double requireCoins() {
        return 10000;
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
        return 30;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7激活要求连杀数: &c50 连杀");
        list.add(" ");
        list.add("&7激活后:");
        list.add("  &a▶ &7获得一个临时的 &b钻石头盔");
        list.add("  &a▶ &7攻击造成的伤害 &c+25%");
        list.add("  &a▶ &7击杀获得 &6+75% 硬币");
        list.add("  &a▶ &7击杀获得 &b+50% 经验值");
        list.add("  &c▶ &7激活后每击杀5名玩家,受到的基础伤害 &c+0.1❤");
        list.add(" ");
        list.add("&7激活后死亡时:");
        list.add("  &6▶ &7保留获得的 &b钻石头盔");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.MEGA_STREAK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handlePlayerBeKilledByEntity(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() >= 50) {
            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                myself.getInventory().addItem(
                        new ItemBuilder(Material.DIAMOND_HELMET)
                                .name("&a野兽之甲")
                                .canSaveToEnderChest(true)
                                .deathDrop(true)
                                .internalName("beast_mode_helmet_award")
                                .buildWithUnbreakable()
                );
                myself.sendMessage(CC.translate("&a&l野兽模式! &7你获得了 &b钻石头盔 &7."));
            }, 20L);
        }
    }

    @Override
    public int getStreakNeed() {
        return 50;
    }
}

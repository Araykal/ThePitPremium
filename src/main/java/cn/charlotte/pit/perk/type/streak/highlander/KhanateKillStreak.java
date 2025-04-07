package cn.charlotte.pit.perk.type.streak.highlander;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/26 17:47
 */
@AutoRegister
public class KhanateKillStreak extends AbstractPerk implements Listener, IAttackEntity {

    @Override
    public String getInternalPerkName() {
        return "khanate_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "可汗";
    }

    @Override
    public Material getIcon() {
        return Material.DIRT;
    }

    @Override
    public double requireCoins() {
        return 6000;
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
        return 60;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c3 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得 &6+8 硬币");
        list.add("  &a▶ &7获得效果: 攻击被悬赏的玩家造成的伤害 &c+4% &7(可叠加,最高10层)");
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
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        //trigger check (every X streak)
        int streak = 3;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            event.getPlayerProfile().setCoins(event.getPlayerProfile().getCoins() + 8);
        }
    }


    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        Player targetPlayer = (Player) target;
        PlayerProfile targetProfile = PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId());
        if (targetProfile.getBounty() > 0) {
            double strength = Math.min(10, Math.floor(profile.getStreakKills() / 3));
            boostDamage.getAndAdd(strength * 0.04);
        }
    }
}

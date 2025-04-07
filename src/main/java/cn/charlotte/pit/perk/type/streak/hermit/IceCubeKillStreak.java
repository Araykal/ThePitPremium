package cn.charlotte.pit.perk.type.streak.hermit;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/23 18:24
 */
@AutoRegister
public class IceCubeKillStreak extends AbstractPerk implements Listener {

    @Override
    public String getInternalPerkName() {
        return "ice_cube_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "冰立方";
    }

    @Override
    public Material getIcon() {
        return Material.ICE;
    }

    @Override
    public double requireCoins() {
        return 9000;
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
        list.add("&7此天赋每 &c10 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得一个 &b冰立方");
        list.add("&7物品 &b冰立方&7: &c手持攻击消耗,对目标造成 &c1❤ &f真实&7伤害与 &c缓慢 I &f(00:04) &7,自身获得 &b40 经验值 &7.");
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

    @EventHandler(ignoreCancelled = true)
    public void onStreak(PitStreakKillChangeEvent event) {
        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (myself == null || !myself.isOnline()) {
            return;
        }
        if (!PlayerUtil.isPlayerChosePerk(myself, getInternalPerkName())) {
            return;
        }
        //trigger check (every X streak)
        int streak = 10;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            myself.getInventory().addItem(
                    new ItemBuilder(Material.ICE)
                            .canDrop(false)
                            .removeOnJoin(true)
                            .canSaveToEnderChest(false)
                            .deathDrop(true)
                            .internalName("ice_cube")
                            .name("&b冰立方")
                            .lore("&7手持攻击造成 &c1❤ &f真实&7伤害与 &c缓慢 I &f(00:04) &7,自身获得 &b40 经验值 &7. &c(使用后消耗)")
                            .build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (((Player) event.getDamager()).getItemInHand() != null && "ice_cube".equals(ItemUtil.getInternalName(((Player) event.getDamager()).getItemInHand()))) {
                PlayerUtil.takeOneItemInHand(((Player) event.getDamager()));
                PlayerUtil.damage(((Player) event.getDamager()), ((Player) event.getEntity()), PlayerUtil.DamageType.TRUE, 2D, true);
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(event.getDamager().getUniqueId());
                profile.setExperience(profile.getExperience() + 40);
            }
        }
    }

    @EventHandler
    public void onPlayerBuild(BlockCanBuildEvent event) {
        if (event.getMaterial() == Material.ICE) {
            event.setBuildable(false);
        }
    }
}


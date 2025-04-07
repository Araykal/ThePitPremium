package cn.charlotte.pit.perk.type.streak.hermit;

import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/22 20:03
 */
@AutoRegister
public class GlassPickaxeKillStreak extends AbstractPerk implements Listener {

    @Override
    public String getInternalPerkName() {
        return "glass_pickaxe_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "玻璃稿";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_PICKAXE;
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
        list.add("&7此天赋每 &c7 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得一个 &b玻璃稿");
        list.add("&7物品 &b玻璃稿&7: &c耐久为1&7,攻击造成 &c4.25❤ &7普通伤害与 &c0.5❤ &f真实&7伤害.");
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
        int streak = 7;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            myself.getInventory().addItem(
                    new ItemBuilder(Material.DIAMOND_PICKAXE)
                            .canDrop(false)
                            .removeOnJoin(true)
                            .canSaveToEnderChest(false)
                            .deathDrop(true)
                            .internalName("glass_pickaxe")
                            .name("&b玻璃稿")
                            .lore("&7攻击额外造成 &c0.5❤ &f真实&7伤害.")
                            .itemDamage(8.5)
                            .durability(1560)
                            .build());
        }
    }

    /*
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (((Player) event.getDamager()).getItemInHand() != null && "glass_pickaxe".equals(ItemUtil.getInternalName(((Player) event.getDamager()).getItemInHand()))) {
                PlayerUtil.damage(((Player) event.getDamager()), ((Player) event.getEntity()), PlayerUtil.DamageType.TRUE, 1D, true);
            }
        }
    }

     */
}

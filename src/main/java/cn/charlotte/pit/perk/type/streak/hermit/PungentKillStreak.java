package cn.charlotte.pit.perk.type.streak.hermit;

import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/22 19:47
 */
@AutoRegister
public class PungentKillStreak extends AbstractPerk implements Listener {

    @Override
    public String getInternalPerkName() {
        return "pungent_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "臭气弹";
    }

    @Override
    public Material getIcon() {
        return Material.REDSTONE;
    }

    @Override
    public double requireCoins() {
        return 5000;
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
        return 50;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c7 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得一个 &c臭气弹");
        list.add("&7物品 &c臭气弹&7: 使用后对周围 &f3 &7格内的其他玩家施加 &c缓慢 I &f(00:03) &7效果.");
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
                    new ItemBuilder(Material.REDSTONE)
                            .canDrop(false)
                            .removeOnJoin(true)
                            .canSaveToEnderChest(false)
                            .deathDrop(true)
                            .internalName("smelly_bomb")
                            .name("&c臭气弹")
                            .lore("&7使用后对周围 &f3 &7格内的其他玩家施加 &c缓慢 I &f(00:03) &7效果.")
                            .build());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (ItemUtil.getInternalName(item) != null && ItemUtil.getInternalName(item).equalsIgnoreCase("smelly_bomb")) {
            PlayerUtil.takeOneItemInHand(player);
            player.sendMessage(CC.translate("&a你释放的臭气弹命中了 " + (PlayerUtil.getNearbyPlayers(player.getLocation(), 3).size() - 1) + " 名玩家!"));
            //effect goes here
            PlayerUtil.getNearbyPlayers(player.getLocation(), 3).forEach(p -> {
                if (!p.getName().equals(player.getName())) {
                    p.removePotionEffect(PotionEffectType.SLOW);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 0));
                }
            });
        }
    }
}

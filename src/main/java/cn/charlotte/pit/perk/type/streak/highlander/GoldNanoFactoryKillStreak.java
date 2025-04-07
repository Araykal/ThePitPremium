package cn.charlotte.pit.perk.type.streak.highlander;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/26 17:57
 */
@AutoRegister
public class GoldNanoFactoryKillStreak extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "gold_nano_factory_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "纳米黄金工厂";
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
        return 50;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c7 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻在原地生成并拾取 &67 金锭");
        list.add("  &a▶ &7立刻获得 &c生命恢复 IV &f(00:02)");
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
        int streak = 7;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            //effect goes here
            for (int i = 0; i < 7; i++) {
                Bukkit.getScheduler().runTask(ThePit.getInstance(), () ->
                {
                    Item item = myself.getWorld().dropItemNaturally(myself.getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                    item.setMetadata("gold", new FixedMetadataValue(ThePit.getInstance(), RandomUtil.random.nextInt(3) + 3));
                    Bukkit.getPluginManager().callEvent(new PlayerPickupItemEvent(myself, item, 1));
                });
            }
            myself.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2 * 20, 3), false);
        }
    }

}

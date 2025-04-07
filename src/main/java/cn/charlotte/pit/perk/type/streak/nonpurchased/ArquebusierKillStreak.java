package cn.charlotte.pit.perk.type.streak.nonpurchased;

import cn.charlotte.pit.data.PlayerProfile;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/21 13:50
 */
@AutoRegister
public class ArquebusierKillStreak extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "arquebusier";
    }

    @Override
    public String getDisplayName() {
        return "火枪手";
    }

    @Override
    public Material getIcon() {
        return Material.ARROW;
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
        return 10;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c3 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得 &6+7 硬币");
        list.add("  &a▶ &7立刻获得 &f16 * 箭 &7(上限128)");
        list.add("  &a▶ &7立刻获得 &b速度 I &7(00:10)");
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

    //KillStreak
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
        int streak = 3;
        if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
            profile.setCoins(profile.getCoins() + 7);
            myself.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0), true);
            int count = 0;
            for (ItemStack item : myself.getInventory()) {
                if (item != null && item.getType() == Material.ARROW) {
                    count += item.getAmount();
                }
            }
            if (count >= 128) {
                return;
            }
            int amount = 16;
            ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW).internalName("default_arrow").defaultItem().canDrop(false).canSaveToEnderChest(false);
            if (count + amount > 128) {
                amount = 128 - count;
            }
            myself.getInventory().addItem(arrowBuilder.amount(amount).build());
        }
    }

}

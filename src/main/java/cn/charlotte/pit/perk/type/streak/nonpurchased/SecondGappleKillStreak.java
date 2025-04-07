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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/21 13:38
 */
@AutoRegister
public class SecondGappleKillStreak extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "second_apple";
    }

    @Override
    public String getDisplayName() {
        return "第二个金苹果";
    }

    @Override
    public Material getIcon() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public double requireCoins() {
        return 1500;
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
        list.add("  &a▶ &7立刻获得 &6+5 硬币");
        list.add("  &a▶ &7立刻获得 &b+5 经验值");
        list.add("  &a▶ &7立刻获得 &f1 * &e金苹果");
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
        PlayerProfile profile = event.getPlayerProfile();
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
            profile.setExperience(profile.getExperience() + 5);
            profile.setCoins(profile.getCoins() + 5);
            if (PlayerUtil.getAmountOfActiveHealingPerk(myself) <= 1 && PlayerUtil.getPlayerHealItemAmount(myself) < PlayerUtil.getPlayerHealItemLimit(myself)) {
                myself.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE).canDrop(false).canSaveToEnderChest(false).deathDrop(true)
                        .removeOnJoin(true)
                        .canSaveToEnderChest(false)
                        .canDrop(false)
                        .isHealingItem(true)
                        .canTrade(false).build());
            }
        }
    }
}

package cn.charlotte.pit.perk.type.streak.grandfinale;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.item.IMythicItem;
import cn.charlotte.pit.item.type.ChunkOfVileItem;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.MegaStreak;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.MythicUtil;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/20 23:05
 */
@AutoRegister
public class GrandFinaleMegaStreak extends AbstractPerk implements Listener, MegaStreak {
    @Override
    public String getInternalPerkName() {
        return "grand_finale";
    }

    @Override
    public String getDisplayName() {
        return "&c杰作";
    }

    @Override
    public Material getIcon() {
        return Material.NETHER_STAR;
    }

    @Override
    public double requireCoins() {
        return 40000;
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
        return 70;
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
        list.add("  &c▶ &7自爆!!!");
        list.add(" ");
        list.add("&7激活后死亡时:");
        list.add("  &a▶ &7获得 &e1 声望");
        list.add("  &a▶ &7消耗背包内4件 &5暗聚块 &7,");
        list.add("  &a▶ &7恢复 &d神话之井 &7内物品 &c4 &7生命.");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
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
        int trigger = 50;
        //trigger check (get X streak)
        if (event.getFrom() < trigger && event.getTo() >= trigger) {
            myself.sendMessage(CC.translate("&c&l杰作! &7你自爆了并获得 &e1 声望 &7."));
            CC.boardCast("&c&l杰作! " + event.getPlayerProfile().getFormattedName() + " &7达到了 &c50 &7连杀并谢幕了!");
            PlayerUtil.deadPlayer(myself);
            event.getPlayerProfile().setRenown(event.getPlayerProfile().getRenown() + 1);
            Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                event.getPlayerProfile().setStreakKills(0.0);
            });
            witherCraft(event.getPlayerProfile(), myself);
        }
    }

    private void witherCraft(PlayerProfile profile, Player myself) {
        //effect goes here
        if (profile.getEnchantingItem() == null) {
            return;
        }
        ItemStack item = InventoryUtil.deserializeItemStack(profile.getEnchantingItem());
        IMythicItem mythicItem = MythicUtil.getMythicItem(item);

        if (mythicItem == null) {
            return;
        }

        mythicItem.loadFromItemStack(item);

        if (mythicItem.getLive() <= 0 || mythicItem.getMaxLive() <= 0 || mythicItem.getMaxLive() == mythicItem.getLive()) {
            return;
        }

        if (!InventoryUtil.removeItem(myself, ChunkOfVileItem.getInternalName(), 4)) {
            return;
        }

        mythicItem.setLive(Math.min(mythicItem.getLive() + 4, mythicItem.getMaxLive()));
        profile.setEnchantingItem(InventoryUtil.serializeItemStack(mythicItem.toItemStack()));
        myself.sendMessage(CC.translate("&5&l凋灵工艺! &7成功修复了神话之井中放入的神话物品."));
    }

    @Override
    public int getStreakNeed() {
        return 50;
    }
}

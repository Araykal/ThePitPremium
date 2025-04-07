package cn.charlotte.pit.perk.type.streak.hermit;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.inventory.InventoryUtil;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Misoryan
 * @Date 2022/11/22 21:35
 */
@AutoRegister
public class AuraOfProtectionKillStreak extends AbstractPerk implements Listener {

    @Override
    public String getInternalPerkName() {
        return "aura_of_protection_kill_streak";
    }

    @Override
    public String getDisplayName() {
        return "保护光环";
    }

    @Override
    public Material getIcon() {
        return Material.SLIME_BALL;
    }

    @Override
    public double requireCoins() {
        return 8000;
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
        list.add("&7此天赋每 &c10 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7立刻获得一个 &a保护光环触发器");
        list.add("&7物品 &a保护光环触发器&7: 使用后对自身施加 &3抗性提升 II &f(00:04) &7与 &a真实伤害抗性 &f(00:15) &7状态");
        list.add("&7状态 &a真实伤害抗性&7: 免疫自身受到的&f真实&7伤害");
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
            final int amount = InventoryUtil.getAmountOfItem(myself, "aura_of_protection");
            if (amount < 2) {
                myself.getInventory().addItem(
                        new ItemBuilder(Material.SLIME_BALL)
                                .canDrop(false)
                                .removeOnJoin(true)
                                .canSaveToEnderChest(false)
                                .deathDrop(true)
                                .internalName("aura_of_protection")
                                .name("&a保护光环触发器")
                                .lore("&7使用后对自身施加 &3抗性提升 II &f(00:15) &7与 &a真实伤害抗性 &f(00:15) &7状态")
                                .build());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if ("aura_of_protection".equals(ItemUtil.getInternalName(item))) {
            PlayerUtil.takeOneItemInHand(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 1), true);
            player.setMetadata("true_damage_immune", new FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis() + 15 * 1000L));
            //effect goes here
        }
    }
}

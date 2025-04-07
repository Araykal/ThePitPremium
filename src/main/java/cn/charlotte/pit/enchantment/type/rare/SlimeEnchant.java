package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/21 18:32
 */
@ArmorOnly
@AutoRegister
public class SlimeEnchant extends AbstractEnchantment implements Listener {
    @Override
    public String getEnchantName() {
        return "召唤: 史莱姆宠物 &c(临时禁用)";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "slime";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7击杀玩家可以召唤/复活史莱姆宠物协助战斗(上限1),"
                + "/s&7每击杀 &e" + (getUpdateRequire(enchantLevel) + " &7名玩家可以提升史莱姆等级(上限11级).")
                + "/s&7史莱姆升级会提升史莱姆的生命上限,攻击力与护甲值";
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onStreak(PitStreakKillChangeEvent event) {
//        Player myself = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
//        if (myself == null || !myself.isOnline()) {
//            return;
//        }
//        if (myself.getInventory().getLeggings() == null || getItemEnchantLevel(myself.getInventory().getLeggings()) <= 0) {
//            return;
//        }
//        final PetFactory pet = ThePit.getInstance().getPetFactory();
//        final PetFactory.PetData data = pet.getPetMap().get(myself.getUniqueId());
//        if (data != null) {
//            if (data.getEntity() instanceof Slime) {
//                //trigger check (every X streak)
//                int enchantLevel = getItemEnchantLevel(myself.getInventory().getLeggings());
//                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
//                int streak = getUpdateRequire(enchantLevel);
//                if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
//                    final Slime slime = (Slime) data.getEntity();
//                    if (slime.getSize() < 12) {
//                        slime.setSize(slime.getSize() + 1);
//                        slime.setHealth(slime.getMaxHealth());
//
//                        data.getHologram().setText(CC.translate(profile.getFormattedName() + " &a&l" + (slime.getSize() - 1)));
//                        CC.send(MessageType.COMBAT, myself, "&2史莱姆升级! &7你的史莱姆从你的战斗中学会了一些技巧,成长了!");
//                    }
//                }
//            }
//        } else {
//            Bukkit.getScheduler()
//                    .runTask(ThePit.getInstance(), () -> pet.spawnPet("slime_pet", myself));
//        }
    }

    private int getUpdateRequire(int enchant) {
        switch (enchant) {
            case 2:
                return 6;
            case 3:
                return 3;
            default:
                return 9;
        }
    }
}

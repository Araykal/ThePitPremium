package cn.charlotte.pit.enchantment.type.op;

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
 * @Creator Misoryan
 * @Date 2021/4/29 17:28
 */
@ArmorOnly
@AutoRegister
public class SuperSlimeEnchant extends AbstractEnchantment implements Listener {

    @Override
    public String getEnchantName() {
        return "召唤: 超级史莱姆宠物 &c(临时禁用)";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "super_slime_pet";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.OP;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7击杀玩家可以召唤/复活史莱姆宠物协助战斗(上限1),"
                + "/s&7每次提升连杀数可以提升史莱姆等级(上限20级)."
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
//                int streak = 1;
//                if (Math.floor(event.getFrom()) % streak != 0 && Math.floor(event.getTo()) % streak == 0) {
//                    final Slime slime = (Slime) data.getEntity();
//                    if (slime.getSize() < 20) {
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
//                    .runTask(ThePit.getInstance(), () -> pet.spawnPet("super_slime_pet", myself));
//        }
    }
}

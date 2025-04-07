package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@WeaponOnly
@AutoRegister
public class LifeStealEnchant extends AbstractEnchantment implements Listener {
    @Override
    public String getEnchantName() {
        return "灵魂汲取";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "life_steal_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7攻击恢复自身相当于伤害量 &c" + (enchantLevel * 4) + "% &7的生命 (上限&c1.5❤&7)";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        Player attacker = null;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        }
        if (attacker != null && attacker.getItemInHand() != null) {
            int level = this.getItemEnchantLevel(attacker.getItemInHand());
            if (level > 0) {
                double heal = Math.min(3, event.getFinalDamage() * level * 0.04);
                PlayerUtil.heal(attacker, heal);
            }
        }
    }
}

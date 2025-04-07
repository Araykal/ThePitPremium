package cn.charlotte.pit.buff.impl;

import cn.charlotte.pit.buff.AbstractPitBuff;
import cn.charlotte.pit.event.PitStackBuffEvent;
import cn.charlotte.pit.event.PotionAddEvent;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;
@AutoRegister
public class PinDownDeBuff extends AbstractPitBuff implements Listener {
    public String getInternalBuffName() {
        return "pin_down_de_buff";
    }

    public String getDisplayName() {
        return "&2&l阻滞";
    }

    public List<String> getDescription() {
        return Collections.singletonList("&7无法受到与被施加 &b速度 &7与 &a跳跃提升 &7效果");
    }

    @EventHandler
    public void onPotionAdd(PotionAddEvent event) {
        if (this.getPlayerBuffData(event.getPlayer()).getTier() > 0
                && (event.getEffect().getType().getName().equalsIgnoreCase("speed") || event.getEffect().getType().getName().equalsIgnoreCase("jump"))) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onBuffStack(PitStackBuffEvent event) {
        if (!event.isCancel() && event.getBuff().getInternalBuffName().equalsIgnoreCase(this.getInternalBuffName())) {
            event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
        }
    }

    public int getMaxTier() {
        return 1;
    }
}

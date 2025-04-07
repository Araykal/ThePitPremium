package cn.charlotte.pit.buff.impl;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.buff.AbstractPitBuff;
import cn.charlotte.pit.event.PitStackBuffEvent;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/11 7:36
 */
@AutoRegister
public class HemorrhageDeBuff extends AbstractPitBuff implements Listener {

    @Override
    public String getInternalBuffName() {
        return "hemorrhage_de_buff";
    }

    @Override
    public String getDisplayName() {
        return "&c流血";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("&7无法受到与被施加 &6生命吸收 &7效果");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBuffStack(PitStackBuffEvent event) {
        if (event.isCancel() || !event.getBuff().getInternalBuffName().equalsIgnoreCase(getInternalBuffName())) {
            return;
        }
        if (UtilKt.hasRealMan(event.getPlayer())) return;
        if (getPlayerBuffData(event.getPlayer()).getTier() < 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ((CraftPlayer) event.getPlayer()).getHandle().setAbsorptionHearts(0F);
                    event.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
                    if (getPlayerBuffData(event.getPlayer()).getTier() < 1) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(ThePit.getInstance(), 0, 10);
        }
    }
}

package cn.charlotte.pit.buff.impl;

import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.buff.AbstractPitBuff;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitRegainHealthEvent;
import cn.charlotte.pit.parm.AutoRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Collections;
import java.util.List;

//trying for fun

/**
 * @Creator Misoryan
 * @Date 2021/5/9 16:49
 */
@AutoRegister
public class HealPoisonDeBuff extends AbstractPitBuff implements Listener {

    @Override
    public String getInternalBuffName() {
        return "heal_poison_de_buff";
    }

    @Override
    public String getDisplayName() {
        return "&a不愈之毒";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("&7恢复生命值时每层效果使恢复量 &a-1% &7,可叠加");
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (UtilKt.hasRealMan(player)) return;

            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            event.setAmount(event.getAmount() * Math.max(0, 1 - 0.01 * profile.getBuffData().getBuff(this.getInternalBuffName()).getTier()));
        }
    }

    @EventHandler
    public void onRegain(PitRegainHealthEvent event) {
        Player player = event.getPlayer();
        if (UtilKt.hasRealMan(player)) return;
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        event.setAmount(event.getAmount() * Math.max(0, 1 - 0.01 * profile.getBuffData().getBuff(this.getInternalBuffName()).getTier()));
    }
}

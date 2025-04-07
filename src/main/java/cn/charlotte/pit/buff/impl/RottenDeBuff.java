package cn.charlotte.pit.buff.impl;


import cn.charlotte.pit.buff.AbstractPitBuff;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitRegainHealthEvent;
import cn.charlotte.pit.parm.AutoRegister;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@AutoRegister
public class RottenDeBuff extends AbstractPitBuff implements Listener {

    @Override
    public String getInternalBuffName() {
        return "rotten_de_buff";
    }

    @Override
    public String getDisplayName() {
        return "&4凝血";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("&7丧失血量恢复能力, 不可叠加");
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            event.setAmount(event.getAmount() * Math.max(0.0D, 1.0D - 1.0D * profile.getBuffData().getBuff(getInternalBuffName()).getTier()));
        }
    }

    @EventHandler
    public void onRegain(PitRegainHealthEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        event.setAmount(event.getAmount() * Math.max(0.0D, 1.0D - 1.0D * profile.getBuffData().getBuff(getInternalBuffName()).getTier()));
    }
}
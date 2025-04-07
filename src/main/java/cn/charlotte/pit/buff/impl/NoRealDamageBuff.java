package cn.charlotte.pit.buff.impl;

import cn.charlotte.pit.buff.AbstractPitBuff;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@JsonIgnoreProperties(ignoreUnknown=true)
@AutoRegister
public class NoRealDamageBuff extends AbstractPitBuff implements IPlayerDamaged {
    @Override
    public String getInternalBuffName() {
        return "no_real_damage_buff";
    }

    @Override
    public String getDisplayName() {
        return "&a真实伤害抗性";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("&7抵御所有&f真实&7攻击伤害.");
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        myself.setHealth(myself.getHealth() + finalDamage.get());
    }

    @Override
    public String toString() {
        return "NoRealDamagerBuff()";
    }
}


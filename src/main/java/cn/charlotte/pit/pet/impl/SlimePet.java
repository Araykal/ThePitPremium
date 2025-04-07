package cn.charlotte.pit.pet.impl;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.pet.IPet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.Collections;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/22 10:05
 */
public class SlimePet implements IPet {
    private Player owner;

    @Override
    public String getInternalName() {
        return "slime_pet";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SLIME;
    }

    @Override
    public List<String> getCustomName() {
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(owner.getUniqueId());
        return Collections.singletonList(
                profile.getFormattedName() + " &a&l1"
        );
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public void insertEntity(Entity entity) {
        final Slime slime = (Slime) entity;
        slime.setSize(2);
        slime.setMaxHealth(50);
        slime.setHealth(50);
    }
}

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
 * @Creator Misoryan
 * @Date 2021/4/29 17:39
 */
public class SuperSlimePet implements IPet {
    private Player owner;

    @Override
    public String getInternalName() {
        return "super_slime_pet";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SLIME;
    }

    @Override
    public List<String> getCustomName() {
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(owner.getUniqueId());
        return Collections.singletonList(
                profile.getFormattedName() + " &a&l10"
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
        slime.setSize(12);
        slime.setMaxHealth(300);
        slime.setHealth(300);
    }
}

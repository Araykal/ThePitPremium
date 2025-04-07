package cn.charlotte.pit.pet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/22 10:04
 */
public interface IPet {

    String getInternalName();

    EntityType getEntityType();

    List<String> getCustomName();

    Player getOwner();

    void setOwner(Player player);

    void insertEntity(Entity entity);

}

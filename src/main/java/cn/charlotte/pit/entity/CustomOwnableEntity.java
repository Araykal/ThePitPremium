package cn.charlotte.pit.entity;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPlayer;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 15:58
 */
public interface CustomOwnableEntity {

    EntityPlayer getOwner();

    void setOwner(EntityPlayer player);

    EntityInsentient getCreature();
}

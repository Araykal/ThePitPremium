package cn.charlotte.pit.entity.custom;

import cn.charlotte.pit.entity.CustomOwnableEntity;
import cn.charlotte.pit.entity.EntityUtil;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 16:12
 */
public class CustomSlime extends EntitySlime implements CustomOwnableEntity {
    private EntityPlayer player;

    public CustomSlime(CraftWorld world, EntityPlayer player) {
        super(world.getHandle());
        this.player = player;

        EntityUtil.insertAi(this, player);
    }

    @Override
    public EntityPlayer getOwner() {
        return player;
    }

    @Override
    public void setOwner(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public EntityInsentient getCreature() {
        return this;
    }
}

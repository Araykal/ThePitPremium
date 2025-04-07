package cn.charlotte.pit.entity.ai;

import cn.charlotte.pit.entity.CustomOwnableEntity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 15:56
 */
public class PathfinderGoalTargetOwner extends PathfinderGoalCustomTarget {
    CustomOwnableEntity self;
    EntityLiving lastDamager;
    private int hurtTimestamp;

    public PathfinderGoalTargetOwner(CustomOwnableEntity var1) {
        super(var1.getCreature(), false);
        this.self = var1;
        this.a(1);
    }

    public boolean a() {
        EntityLiving owner = this.self.getOwner();
        if (owner == null) {
            return false;
        } else {
            this.lastDamager = owner.getLastDamager();
            int hurtTimestamp = owner.be();
            return hurtTimestamp != this.hurtTimestamp && this.a(this.lastDamager, false);
        }
    }

    public void c() {
        this.e.setGoalTarget(this.lastDamager, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
        EntityLiving owner = this.self.getOwner();
        if (owner != null) {
            this.hurtTimestamp = owner.be();
        }

        super.c();
    }
}

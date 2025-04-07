package cn.charlotte.pit.entity.ai;

import cn.charlotte.pit.entity.CustomOwnableEntity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 16:10
 */
public class PathfinderGoalOwnerHurt extends PathfinderGoalCustomTarget {
    CustomOwnableEntity a;
    EntityLiving b;
    private int c;

    public PathfinderGoalOwnerHurt(CustomOwnableEntity var1) {
        super(var1.getCreature(), false);
        this.a = var1;
        this.a(1);
    }

    public boolean a() {
        EntityLiving var1 = this.a.getOwner();
        if (var1 == null) {
            return false;
        } else {
            this.b = var1.bf();
            int var2 = var1.bg();
            return var2 != this.c && this.a(this.b, false);
        }
    }

    public void c() {
        this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true);
        EntityLiving var1 = this.a.getOwner();
        if (var1 != null) {
            this.c = var1.bg();
        }

        super.c();
    }
}

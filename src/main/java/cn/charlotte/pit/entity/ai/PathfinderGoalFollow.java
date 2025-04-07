package cn.charlotte.pit.entity.ai;

import cn.charlotte.pit.entity.CustomOwnableEntity;
import net.minecraft.server.v1_8_R3.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 16:08
 */
public class PathfinderGoalFollow extends PathfinderGoal {
    private final CustomOwnableEntity self;
    private final double f;
    private final NavigationAbstract g;
    World a;
    float b;
    float c;
    private EntityLiving e;
    private int h;
    private boolean i;

    public PathfinderGoalFollow(CustomOwnableEntity self, double var2, float var4, float var5) {
        this.self = self;
        this.a = self.getCreature().world;
        this.f = var2;
        this.g = self.getCreature().getNavigation();
        this.c = var4;
        this.b = var5;
        this.a(3);
        if (!(self.getCreature().getNavigation() instanceof Navigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean a() {
        EntityHuman owner = this.self.getOwner();
        if (owner == null) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.self.getCreature().h(owner) < (double) (this.c * this.c)) {
            return false;
        } else {
            this.e = owner;
            return true;
        }
    }

    public boolean b() {
        return !this.g.m() && this.self.getCreature().h(this.e) > (double) (this.b * this.b);
    }

    public void c() {
        this.h = 0;
        this.i = ((Navigation) this.self.getCreature().getNavigation()).e();
//        this.self.getCreature().getNavigation().a(false);
    }

    public void d() {
        this.e = null;
        this.g.n();
//        this.self.getCreature().getNavigation().a(true);
    }

    private boolean a(BlockPosition var1) {
        IBlockData var2 = this.a.getType(var1);
        Block var3 = var2.getBlock();
        if (var3 == Blocks.AIR) {
            return true;
        } else {
            return !var3.d();
        }
    }

    public void e() {
        this.self.getCreature().getControllerLook().a(this.e, 10.0F, (float) this.self.getCreature().bQ());
        if (--this.h <= 0) {
            this.h = 10;
            if (!this.g.a(this.e, this.f) && !this.self.getCreature().cc() && this.self.getCreature().h(this.e) >= 144.0D) {
                int var1 = MathHelper.floor(this.e.locX) - 2;
                int var2 = MathHelper.floor(this.e.locZ) - 2;
                int var3 = MathHelper.floor(this.e.getBoundingBox().b);

                for (int var4 = 0; var4 <= 4; ++var4) {
                    for (int var5 = 0; var5 <= 4; ++var5) {
                        if ((var4 < 1 || var5 < 1 || var4 > 3 || var5 > 3) && World.a(this.a, new BlockPosition(var1 + var4, var3 - 1, var2 + var5)) && this.a(new BlockPosition(var1 + var4, var3, var2 + var5)) && this.a(new BlockPosition(var1 + var4, var3 + 1, var2 + var5))) {
                            this.self.getCreature().setPositionRotation((float) (var1 + var4) + 0.5F, var3, (float) (var2 + var5) + 0.5F, this.self.getCreature().yaw, this.self.getCreature().pitch);
                            this.g.n();
                            return;
                        }
                    }
                }
            }
        }

    }
}

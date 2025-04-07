package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Entity;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/2 13:17
 */
public class DroppedEntityData {
    private Entity entity;
    private Cooldown timer;

    public DroppedEntityData() {
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Cooldown getTimer() {
        return this.timer;
    }

    public void setTimer(Cooldown timer) {
        this.timer = timer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DroppedEntityData)) return false;
        final DroppedEntityData other = (DroppedEntityData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$entity = this.getEntity();
        final Object other$entity = other.getEntity();
        if (this$entity == null ? other$entity != null : !this$entity.equals(other$entity)) return false;
        final Object this$timer = this.getTimer();
        final Object other$timer = other.getTimer();
        if (this$timer == null ? other$timer != null : !this$timer.equals(other$timer)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DroppedEntityData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $entity = this.getEntity();
        result = result * PRIME + ($entity == null ? 43 : $entity.hashCode());
        final Object $timer = this.getTimer();
        result = result * PRIME + ($timer == null ? 43 : $timer.hashCode());
        return result;
    }

    public String toString() {
        return "DroppedEntityData(entity=" + this.getEntity() + ", timer=" + this.getTimer() + ")";
    }
}

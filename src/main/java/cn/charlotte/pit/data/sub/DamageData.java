package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.util.cooldown.Cooldown;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 12:12
 */
public class DamageData {
    private UUID uuid;
    private double damage;
    private Cooldown timer;

    public DamageData(UUID uuid) {
        this.uuid = uuid;
        this.damage = 0;
        this.timer = new Cooldown(30, TimeUnit.SECONDS);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public Cooldown getTimer() {
        return this.timer;
    }

    public void setTimer(Cooldown timer) {
        this.timer = timer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DamageData)) return false;
        final DamageData other = (DamageData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        if (Double.compare(this.getDamage(), other.getDamage()) != 0) return false;
        final Object this$timer = this.getTimer();
        final Object other$timer = other.getTimer();
        if (this$timer == null ? other$timer != null : !this$timer.equals(other$timer)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DamageData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final long $damage = Double.doubleToLongBits(this.getDamage());
        result = result * PRIME + (int) ($damage >>> 32 ^ $damage);
        final Object $timer = this.getTimer();
        result = result * PRIME + ($timer == null ? 43 : $timer.hashCode());
        return result;
    }

    public String toString() {
        return "DamageData(uuid=" + this.getUuid() + ", damage=" + this.getDamage() + ", timer=" + this.getTimer() + ")";
    }
}

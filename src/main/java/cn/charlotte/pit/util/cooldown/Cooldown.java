package cn.charlotte.pit.util.cooldown;

import cn.charlotte.pit.util.time.TimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 19:07
 */
public class Cooldown {
    private long start = System.currentTimeMillis();
    private long expire;
    private boolean notified;

    private long duration;

    public Cooldown(long duration) {
        this.duration = duration;
        this.expire = this.start + duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }

    public Cooldown(long duration, TimeUnit timeUnit) {
        this(timeUnit.toMillis(duration));
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0L;
    }

    public String getTimeLeft() {
        return this.getRemaining() >= 60000L ? TimeUtil.millisToRoundedTime(this.getRemaining()) : TimeUtil.millisToSeconds(this.getRemaining());
    }

    public void reset() {
        this.start = System.currentTimeMillis();
        this.expire = this.start + this.duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getExpire() {
        return this.expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public boolean isNotified() {
        return this.notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Cooldown)) return false;
        final Cooldown other = (Cooldown) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getStart() != other.getStart()) return false;
        if (this.getExpire() != other.getExpire()) return false;
        if (this.isNotified() != other.isNotified()) return false;
        if (this.getDuration() != other.getDuration()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Cooldown;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $start = this.getStart();
        result = result * PRIME + (int) ($start >>> 32 ^ $start);
        final long $expire = this.getExpire();
        result = result * PRIME + (int) ($expire >>> 32 ^ $expire);
        result = result * PRIME + (this.isNotified() ? 79 : 97);
        final long $duration = this.getDuration();
        result = result * PRIME + (int) ($duration >>> 32 ^ $duration);
        return result;
    }

    public String toString() {
        return "Cooldown(start=" + this.getStart() + ", expire=" + this.getExpire() + ", notified=" + this.isNotified() + ", duration=" + this.getDuration() + ")";
    }
}

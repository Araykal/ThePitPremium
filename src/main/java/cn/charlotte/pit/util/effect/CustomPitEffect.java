package cn.charlotte.pit.util.effect;

import cn.charlotte.pit.util.cooldown.Cooldown;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/9 14:40
 */
public class CustomPitEffect {

    private CustomPitEffectType type;
    private int level;
    private Cooldown duration;
    public CustomPitEffect(CustomPitEffectType type, int level, Cooldown duration) {
        this.type = type;
        this.level = level;
        this.duration = duration;
    }

    public CustomPitEffectType getType() {
        return this.type;
    }

    public void setType(CustomPitEffectType type) {
        this.type = type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Cooldown getDuration() {
        return this.duration;
    }

    public void setDuration(Cooldown duration) {
        this.duration = duration;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomPitEffect)) return false;
        final CustomPitEffect other = (CustomPitEffect) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        if (this.getLevel() != other.getLevel()) return false;
        final Object this$duration = this.getDuration();
        final Object other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !this$duration.equals(other$duration)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CustomPitEffect;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + this.getLevel();
        final Object $duration = this.getDuration();
        result = result * PRIME + ($duration == null ? 43 : $duration.hashCode());
        return result;
    }

    public String toString() {
        return "CustomPitEffect(type=" + this.getType() + ", level=" + this.getLevel() + ", duration=" + this.getDuration() + ")";
    }
}

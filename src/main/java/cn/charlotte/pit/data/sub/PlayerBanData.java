package cn.charlotte.pit.data.sub;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/14 15:59
 */
public class PlayerBanData {

    private String executor = "none";
    private String reason = "none";
    private long start = 0;
    private long duration = 0;
    private long end = 0;

    public PlayerBanData() {
    }

    public String getExecutor() {
        return this.executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerBanData)) return false;
        final PlayerBanData other = (PlayerBanData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$executor = this.getExecutor();
        final Object other$executor = other.getExecutor();
        if (this$executor == null ? other$executor != null : !this$executor.equals(other$executor)) return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        if (this.getStart() != other.getStart()) return false;
        if (this.getDuration() != other.getDuration()) return false;
        if (this.getEnd() != other.getEnd()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerBanData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $executor = this.getExecutor();
        result = result * PRIME + ($executor == null ? 43 : $executor.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        final long $start = this.getStart();
        result = result * PRIME + (int) ($start >>> 32 ^ $start);
        final long $duration = this.getDuration();
        result = result * PRIME + (int) ($duration >>> 32 ^ $duration);
        final long $end = this.getEnd();
        result = result * PRIME + (int) ($end >>> 32 ^ $end);
        return result;
    }

    public String toString() {
        return "PlayerBanData(executor=" + this.getExecutor() + ", reason=" + this.getReason() + ", start=" + this.getStart() + ", duration=" + this.getDuration() + ", end=" + this.getEnd() + ")";
    }
}

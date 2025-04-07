package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.data.PlayerProfile;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/22 22:34
 */
public class WipedData {
    private long wipedTimestamp;
    private PlayerProfile wipedProfile;
    private String reason;
    private boolean know;

    public WipedData() {
    }

    public long getWipedTimestamp() {
        return this.wipedTimestamp;
    }

    public void setWipedTimestamp(long wipedTimestamp) {
        this.wipedTimestamp = wipedTimestamp;
    }

    public PlayerProfile getWipedProfile() {
        return this.wipedProfile;
    }

    public void setWipedProfile(PlayerProfile wipedProfile) {
        this.wipedProfile = wipedProfile;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isKnow() {
        return this.know;
    }

    public void setKnow(boolean know) {
        this.know = know;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WipedData)) return false;
        final WipedData other = (WipedData) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getWipedTimestamp() != other.getWipedTimestamp()) return false;
        final Object this$wipedProfile = this.getWipedProfile();
        final Object other$wipedProfile = other.getWipedProfile();
        if (this$wipedProfile == null ? other$wipedProfile != null : !this$wipedProfile.equals(other$wipedProfile))
            return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        if (this.isKnow() != other.isKnow()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WipedData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $wipedTimestamp = this.getWipedTimestamp();
        result = result * PRIME + (int) ($wipedTimestamp >>> 32 ^ $wipedTimestamp);
        final Object $wipedProfile = this.getWipedProfile();
        result = result * PRIME + ($wipedProfile == null ? 43 : $wipedProfile.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        result = result * PRIME + (this.isKnow() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "WipedData(wipedTimestamp=" + this.getWipedTimestamp() + ", wipedProfile=" + this.getWipedProfile() + ", reason=" + this.getReason() + ", know=" + this.isKnow() + ")";
    }
}

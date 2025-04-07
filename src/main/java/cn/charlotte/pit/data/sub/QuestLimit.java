package cn.charlotte.pit.data.sub;

/**
 * @Author: Misoryan
 * @Created_In: 2021/4/7 19:04
 */
public class QuestLimit {
    private long lastRefresh;
    private int times;

    public QuestLimit() {
    }

    public long getLastRefresh() {
        return this.lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getTimes() {
        return this.times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QuestLimit)) return false;
        final QuestLimit other = (QuestLimit) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getLastRefresh() != other.getLastRefresh()) return false;
        if (this.getTimes() != other.getTimes()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuestLimit;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $lastRefresh = this.getLastRefresh();
        result = result * PRIME + (int) ($lastRefresh >>> 32 ^ $lastRefresh);
        result = result * PRIME + this.getTimes();
        return result;
    }

    public String toString() {
        return "QuestLimit(lastRefresh=" + this.getLastRefresh() + ", times=" + this.getTimes() + ")";
    }
}

package cn.charlotte.pit.data.sub;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/20 22:27
 */
public class QuestCenter {
    private List<String> todayQuest = new ArrayList<>();
    private long lastRefreshTime = 0L;

    public QuestCenter() {
    }

    public List<String> getTodayQuest() {
        return this.todayQuest;
    }

    public void setTodayQuest(List<String> todayQuest) {
        this.todayQuest = todayQuest;
    }

    public long getLastRefreshTime() {
        return this.lastRefreshTime;
    }

    public void setLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QuestCenter)) return false;
        final QuestCenter other = (QuestCenter) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$todayQuest = this.getTodayQuest();
        final Object other$todayQuest = other.getTodayQuest();
        if (this$todayQuest == null ? other$todayQuest != null : !this$todayQuest.equals(other$todayQuest))
            return false;
        if (this.getLastRefreshTime() != other.getLastRefreshTime()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuestCenter;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $todayQuest = this.getTodayQuest();
        result = result * PRIME + ($todayQuest == null ? 43 : $todayQuest.hashCode());
        final long $lastRefreshTime = this.getLastRefreshTime();
        result = result * PRIME + (int) ($lastRefreshTime >>> 32 ^ $lastRefreshTime);
        return result;
    }

    public String toString() {
        return "QuestCenter(todayQuest=" + this.getTodayQuest() + ", lastRefreshTime=" + this.getLastRefreshTime() + ")";
    }
}

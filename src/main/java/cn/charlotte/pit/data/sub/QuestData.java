package cn.charlotte.pit.data.sub;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 16:18
 */
public class QuestData {
    private String internalName;
    private int level;
    private long startTime;
    private long endTime;
    private int current;
    private int total;
    private Set<String> killed;

    public QuestData() {
        this.killed = new HashSet<>();
    }

    public String getInternalName() {
        return this.internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Set<String> getKilled() {
        return this.killed;
    }

    public void setKilled(Set<String> killed) {
        this.killed = killed;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof QuestData)) return false;
        final QuestData other = (QuestData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$internalName = this.getInternalName();
        final Object other$internalName = other.getInternalName();
        if (this$internalName == null ? other$internalName != null : !this$internalName.equals(other$internalName))
            return false;
        if (this.getLevel() != other.getLevel()) return false;
        if (this.getStartTime() != other.getStartTime()) return false;
        if (this.getEndTime() != other.getEndTime()) return false;
        if (this.getCurrent() != other.getCurrent()) return false;
        if (this.getTotal() != other.getTotal()) return false;
        final Object this$killed = this.getKilled();
        final Object other$killed = other.getKilled();
        if (this$killed == null ? other$killed != null : !this$killed.equals(other$killed)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuestData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $internalName = this.getInternalName();
        result = result * PRIME + ($internalName == null ? 43 : $internalName.hashCode());
        result = result * PRIME + this.getLevel();
        final long $startTime = this.getStartTime();
        result = result * PRIME + (int) ($startTime >>> 32 ^ $startTime);
        final long $endTime = this.getEndTime();
        result = result * PRIME + (int) ($endTime >>> 32 ^ $endTime);
        result = result * PRIME + this.getCurrent();
        result = result * PRIME + this.getTotal();
        final Object $killed = this.getKilled();
        result = result * PRIME + ($killed == null ? 43 : $killed.hashCode());
        return result;
    }

    public String toString() {
        return "QuestData(internalName=" + this.getInternalName() + ", level=" + this.getLevel() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", current=" + this.getCurrent() + ", total=" + this.getTotal() + ", killed=" + this.getKilled() + ")";
    }
}

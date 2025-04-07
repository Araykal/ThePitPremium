package cn.charlotte.pit.util.update;

public class UpdateStatus {
    private int completed, total;

    public UpdateStatus() {
    }

    public void markCompleted() {
        this.completed++;
    }

    public int getCompleted() {
        return this.completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UpdateStatus)) return false;
        final UpdateStatus other = (UpdateStatus) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getCompleted() != other.getCompleted()) return false;
        if (this.getTotal() != other.getTotal()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UpdateStatus;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getCompleted();
        result = result * PRIME + this.getTotal();
        return result;
    }

    public String toString() {
        return "UpdateStatus(completed=" + this.getCompleted() + ", total=" + this.getTotal() + ")";
    }
}

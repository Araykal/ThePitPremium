package cn.charlotte.pit.data.sub;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 21:44
 */
public class PerkData {
    private String perkInternalName;
    private int level;

    public PerkData(String perkInternalName, int level) {
        this.perkInternalName = perkInternalName;
        this.level = level;
    }

    public PerkData() {
    }

    public String getPerkInternalName() {
        return this.perkInternalName;
    }

    public void setPerkInternalName(String perkInternalName) {
        this.perkInternalName = perkInternalName;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PerkData)) return false;
        final PerkData other = (PerkData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$perkInternalName = this.getPerkInternalName();
        final Object other$perkInternalName = other.getPerkInternalName();
        if (this$perkInternalName == null ? other$perkInternalName != null : !this$perkInternalName.equals(other$perkInternalName))
            return false;
        if (this.getLevel() != other.getLevel()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PerkData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $perkInternalName = this.getPerkInternalName();
        result = result * PRIME + ($perkInternalName == null ? 43 : $perkInternalName.hashCode());
        result = result * PRIME + this.getLevel();
        return result;
    }

    public String toString() {
        return "PerkData(perkInternalName=" + this.getPerkInternalName() + ", level=" + this.getLevel() + ")";
    }
}

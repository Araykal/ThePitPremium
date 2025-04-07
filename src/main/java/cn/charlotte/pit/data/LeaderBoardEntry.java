package cn.charlotte.pit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/3 12:57
 */
public class LeaderBoardEntry {
    private static List<LeaderBoardEntry> leaderBoardEntries = new ArrayList<>();

    private final String name;
    private final UUID uuid;
    private final int rank;
    private final double experience;
    private final int prestige;

    public LeaderBoardEntry(String name, UUID uuid, int rank, double experience, int prestige) {
        this.name = name;
        this.uuid = uuid;
        this.rank = rank;
        this.experience = experience;
        this.prestige = prestige;
    }

    public static List<LeaderBoardEntry> getLeaderBoardEntries() {
        return LeaderBoardEntry.leaderBoardEntries;
    }

    public static void setLeaderBoardEntries(List<LeaderBoardEntry> leaderBoardEntries) {
        LeaderBoardEntry.leaderBoardEntries = leaderBoardEntries;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getRank() {
        return this.rank;
    }

    public double getExperience() {
        return this.experience;
    }

    public int getPrestige() {
        return this.prestige;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LeaderBoardEntry)) return false;
        final LeaderBoardEntry other = (LeaderBoardEntry) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        if (this.getRank() != other.getRank()) return false;
        if (Double.compare(this.getExperience(), other.getExperience()) != 0) return false;
        if (this.getPrestige() != other.getPrestige()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LeaderBoardEntry;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        result = result * PRIME + this.getRank();
        final long $experience = Double.doubleToLongBits(this.getExperience());
        result = result * PRIME + (int) ($experience >>> 32 ^ $experience);
        result = result * PRIME + this.getPrestige();
        return result;
    }

    @Override
    public String toString() {
        return "LeaderBoardEntry(name=" + this.getName() + ", uuid=" + this.getUuid() + ", rank=" + this.getRank() + ", experience=" + this.getExperience() + ", prestige=" + this.getPrestige() + ")";
    }
}

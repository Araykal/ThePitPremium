package cn.charlotte.pit.data.sub;


import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.Location;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:15
 */
public class PlacedBlockData {
    private final Location location;
    private final Cooldown cooldown;

    public PlacedBlockData(Location location, Cooldown cooldown) {
        this.location = location;
        this.cooldown = cooldown;
    }

    public Location getLocation() {
        return this.location;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlacedBlockData)) return false;
        final PlacedBlockData other = (PlacedBlockData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$location = this.getLocation();
        final Object other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlacedBlockData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $location = this.getLocation();
        result = result * PRIME + ($location == null ? 43 : $location.hashCode());
        return result;
    }
}

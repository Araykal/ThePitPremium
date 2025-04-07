package cn.charlotte.pit.hologram.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.hologram.AbstractHologram;
import cn.charlotte.pit.util.level.LevelUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnderChestHologram extends AbstractHologram {

    @Override
    public String getInternalName() {
        return "ender_chest_holo";
    }

    @Override
    public List<String> getText(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&5&l末影箱");
        lines.add("&7永久储存物品");
        return lines;
    }

    @Override
    public boolean shouldLoop() {
        return true;
    }

    @Override
    public int loopTicks() {
        return 20;
    }

    @Override
    public double getHologramHighInterval() {
        return 0.35;
    }

    @Override
    public Location getLocation() {
        return ThePit.getInstance().getPitConfig().getChestHologram();
    }
}

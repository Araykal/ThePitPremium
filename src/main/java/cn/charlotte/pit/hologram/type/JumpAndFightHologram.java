package cn.charlotte.pit.hologram.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.hologram.AbstractHologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:39
 */
public class JumpAndFightHologram extends AbstractHologram {
    private int animationTick = 0;
    private long lastTickTime = 0;

    @Override
    public String getInternalName() {
        return "jump_and_fight";
    }

    @Override
    public List<String> getText(Player player) {
        return Arrays.asList("&e&l天坑乱斗", "&a&l跳下去! &c&l战斗!");
    }

    @Override
    public boolean shouldLoop() {
        return true;
    }

    @Override
    public int loopTicks() {
        return 2;
    }

    @Override
    public double getHologramHighInterval() {
        return 0.35;
    }

    @Override
    public Location getLocation() {
        return ThePit.getInstance().getPitConfig().getHologramLocation();
    }
}

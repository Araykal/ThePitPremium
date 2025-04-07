package cn.charlotte.pit.hologram;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.hologram.Hologram;
import cn.charlotte.pit.util.hologram.HologramAPI;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:25
 */
public class HologramRunnable implements Runnable {
    private long tick = 0L;

    public HologramRunnable() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this, 50L, 50L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            HologramListener.PlayerHologram hologram = HologramListener.hologramMap.get(player.getUniqueId());
            if (hologram == null) {
                continue;
            }
            Map<String, HologramListener.HologramData> data = hologram.getHologramData();
            for (AbstractHologram abstractHologram : (ThePit.getInstance().getHologramFactory()).loopHologram) {
                HologramListener.HologramData hologramData = data.get(abstractHologram.getInternalName());
                if (hologramData == null) {
                    continue;
                }
                if (this.tick % abstractHologram.loopTicks() != 0L) {
                    continue;
                }
                List<String> text = abstractHologram.getText(player);
                for (int i = 0; i < text.size(); i++) {
                    if (hologramData.getHolograms().size() <= i) {
                        Hologram holo = HologramAPI.createHologram(abstractHologram.getLocation(), CC.translate(text.get(i)));
                        holo.spawn(Collections.singletonList(player));
                        hologramData.getHolograms().add(holo);
                    }
                    hologramData.getHolograms().get(i).setText(CC.translate(text.get(i)));
                }
            }
        }
        this.tick++;
    }
}

package cn.charlotte.pit.util.hologram.touch;

import cn.charlotte.pit.util.hologram.Hologram;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface TouchHandler {

    void onTouch(@Nonnull Hologram hologram, @Nonnull Player player, @Nonnull TouchAction action);

}

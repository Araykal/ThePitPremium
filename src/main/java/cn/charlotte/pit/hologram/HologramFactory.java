package cn.charlotte.pit.hologram;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.command.util.ClassUtil;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:06
 */

public class HologramFactory {
    protected final List<AbstractHologram> loopHologram;
    protected final List<AbstractHologram> normalHologram;

    public HologramFactory() {
        this.loopHologram = new ArrayList<>();
        this.normalHologram = new ArrayList<>();
    }

    @SneakyThrows
    public void init() {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(ThePit.getInstance(), "cn.charlotte.pit.hologram.type")) {
            if (AbstractHologram.class.isAssignableFrom(clazz)) {
                AbstractHologram hologram = (AbstractHologram) clazz.newInstance();
                if (hologram.shouldLoop()) {
                    this.loopHologram.add(hologram);
                } else {
                    this.normalHologram.add(hologram);
                }
            }
        }

        new HologramRunnable();
    }
}

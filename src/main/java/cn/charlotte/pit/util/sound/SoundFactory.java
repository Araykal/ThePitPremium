package cn.charlotte.pit.util.sound;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.command.util.ClassUtil;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/26 13:31
 */
public class SoundFactory extends BukkitRunnable {
    private Map<String, AbstractPitSound> pitSounds;


    @SneakyThrows
    public void init() {
        this.pitSounds = new HashMap<>();

        final Collection<Class<?>> classes = ClassUtil.getClassesInPackage(ThePit.getInstance(), "cn.charlotte.pit.util.sound.type");
        for (Class<?> clazz : classes) {
            if (AbstractPitSound.class.isAssignableFrom(clazz)) {
                final AbstractPitSound sound = (AbstractPitSound) clazz.newInstance();
                this.pitSounds.put(sound.getMusicInternalName(), sound);
            }
        }

        this.runTaskTimerAsynchronously(ThePit.getInstance(), 1, 1);
    }

    public void registerSound(AbstractPitSound sound) {
        this.pitSounds.put(sound.getMusicInternalName(), sound);
    }

    public void playSound(String name, Player player) {
        final AbstractPitSound sound = pitSounds.get(name);
        if (sound != null) {
            sound.play(player);
        }
    }

    @Override
    public void run() {
        for (Map.Entry<String, AbstractPitSound> entry : this.pitSounds.entrySet()) {
            entry.getValue().tick();
        }
    }
}

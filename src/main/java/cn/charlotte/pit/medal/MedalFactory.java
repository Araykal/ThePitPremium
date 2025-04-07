package cn.charlotte.pit.medal;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.MedalData;
import cn.charlotte.pit.event.PitProfileLoadedEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.command.util.ClassUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/6/8 20:13
 */
@Data
@AutoRegister
public class MedalFactory implements Listener {

    private final List<AbstractMedal> medals;

    public MedalFactory() {
        this.medals = new ArrayList<>();
    }

    @SneakyThrows
    public void init() {
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(ThePit.getInstance(), "cn.charlotte.pit.medal.impl");
        for (Class<?> clazz : classes) {
            if (AbstractMedal.class.isAssignableFrom(clazz)) {
                Object instance = clazz.newInstance();
                medals.add((AbstractMedal) instance);
            }
        }
    }

    public int getMedalAmount() {
        int amount = 0;
        for (AbstractMedal medal : medals) {
            amount += medal.getMaxLevel();
        }
        return amount;
    }

    @EventHandler
    public void onProfileLoaded(PitProfileLoadedEvent event) {
        try {
            PlayerProfile profile = event.getPlayerProfile();
            MedalData medalData = profile.getMedalData();
            //fill empty medal data
            List<AbstractMedal> medals = ThePit.getInstance().getMedalFactory().getMedals();
            for (AbstractMedal medal : medals) {
                for (int i = 0; i < medal.getMaxLevel(); i++) {
                    if (!medalData.getMedalStatus().containsKey(medal.getInternalName() + "#" + (i + 1))) {
                        medalData.getMedalStatus().put(medal.getInternalName() + "#" + (i + 1), new MedalData.MedalStatus());
                    }
                }
                medal.handleProfileLoaded(profile);
            }
        } catch (Exception e) {
            if (Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid()) != null) {
                CC.printError(Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid()), e);
            }
        }
    }
}

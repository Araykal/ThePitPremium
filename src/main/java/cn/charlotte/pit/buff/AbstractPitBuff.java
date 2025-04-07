package cn.charlotte.pit.buff;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.event.PitStackBuffEvent;
import cn.charlotte.pit.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/9 17:00
 */
public abstract class AbstractPitBuff {

    public abstract String getInternalBuffName();

    public abstract String getDisplayName();

    public abstract List<String> getDescription();

    public BuffData.Buff getPlayerBuffData(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        return profile.getBuffData().getBuff(this.getInternalBuffName());
    }

    public void stackBuff(Player player, long duration) {
        stackBuff(player, duration, 1);
    }

    public void stackBuff(Player player, long duration, int times) {
        PitStackBuffEvent event = new PitStackBuffEvent(player, this, System.currentTimeMillis() + duration);
        event.callEvent();
        if (event.isCancel()) {
            return;
        }
        for (int i = 0; i < times; i++) {
            getPlayerBuffData(player).stackBuffData(duration);
        }

        player.sendMessage(CC.translate("&9" + getDisplayName() + "! &7你得到了 &c" + times + "&7 层" + getDisplayName() + "效果&c " + duration / 1000 + " &7秒."));
    }

}

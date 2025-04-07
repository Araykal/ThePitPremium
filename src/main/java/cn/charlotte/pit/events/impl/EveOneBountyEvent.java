package cn.charlotte.pit.events.impl;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.INormalEvent;
import cn.charlotte.pit.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/8 21:25
 */
public class EveOneBountyEvent implements IEvent, INormalEvent {
    @Override
    public String getEventInternalName() {
        return "everyone_bounty_event";
    }

    @Override
    public String getEventName() {
        return "全员通缉";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @Override
    public void onActive() {
        Bukkit.broadcastMessage(CC.translate("&6&l全员通缉! &7所有人现在都被以 &6100g &7的赏金悬赏了!"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if (profile.isInArena()) {
                profile.setBounty(profile.getBounty() + 100);
            }
        }
        ThePit.getInstance()
                .getEventFactory()
                .inactiveEvent(this);
    }

    @Override
    public void onInactive() {

    }
}

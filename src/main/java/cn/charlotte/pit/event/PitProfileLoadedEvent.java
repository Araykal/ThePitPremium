package cn.charlotte.pit.event;

import cn.charlotte.pit.data.PlayerProfile;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 0:54
 */
public class PitProfileLoadedEvent extends PitEvent {
    private final PlayerProfile playerProfile;

    public PitProfileLoadedEvent(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }
}

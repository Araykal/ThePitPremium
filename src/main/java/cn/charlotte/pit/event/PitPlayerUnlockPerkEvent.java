package cn.charlotte.pit.event;

import cn.charlotte.pit.perk.AbstractPerk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 17:55
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class PitPlayerUnlockPerkEvent extends PitEvent {
    private final Player player;
    private final AbstractPerk perk;
}

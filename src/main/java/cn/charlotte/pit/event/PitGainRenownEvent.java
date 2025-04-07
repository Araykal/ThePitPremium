package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @Creator Misoryan
 * @Date 2021/6/10 17:24
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class PitGainRenownEvent extends PitEvent {
    private final Player player;
    private final int renown;

}

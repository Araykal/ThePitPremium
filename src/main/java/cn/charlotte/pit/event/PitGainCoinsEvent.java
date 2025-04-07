package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/25 22:05
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class PitGainCoinsEvent extends PitEvent {
    private final Player player;
    private final double coins;


}

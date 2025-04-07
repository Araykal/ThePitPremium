package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/20 17:25
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class OriginalTimeChangeEvent extends PitEvent implements Cancellable {
    private final long time;
    private boolean cancelled;
}

package cn.charlotte.pit.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class ItemLiveDropEvent extends PitEvent implements Cancellable {
    private final double chance;
    boolean cancelled;

}

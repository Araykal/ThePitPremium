package cn.charlotte.pit.event;

import cn.charlotte.pit.buff.AbstractPitBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * @Creator Misoryan
 * @Date 2021/5/11 9:49
 */
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class PitStackBuffEvent extends PitEvent implements Cancellable {
    private final Player player;
    private final AbstractPitBuff buff;
    private final long buffExpire;
    private boolean cancel;

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}

package cn.charlotte.pit.game;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.game.runnable.TradeMonitorRunnable;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.runnable.ClearRunnable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 21:59
 */
@AutoRegister
@Getter
public class Game {
    private final List<AbstractPerk> disabledPerks = new ArrayList<>();


    public void initRunnable() {
        new ClearRunnable()
                .runTaskTimer(ThePit.getInstance(), 20, 20);

        new TradeMonitorRunnable()
                .runTaskTimer(ThePit.getInstance(), 20, 20);
    }


}

package cn.charlotte.pit.buff.impl;

import cn.charlotte.pit.buff.AbstractPitBuff;

import java.util.Collections;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/14 19:15
 */
public class BountySolventBuff extends AbstractPitBuff {
    @Override
    public String getInternalBuffName() {
        return "bounty_solvent";
    }

    @Override
    public String getDisplayName() {
        return "&6赏金溶剂";
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList("&7受到带有 &6&l1000g &7以上赏金攻击时受到的伤害 &9-30% &7;赏金获取量 &6+50%");
    }
}

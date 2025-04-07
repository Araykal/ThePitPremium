package cn.charlotte.pit.buff;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/9 16:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BuffData {
    private HashMap<String, Buff> buffs = new HashMap<>();

    public Buff getBuff(String internal) {
        buffs.putIfAbsent(internal, new Buff());
        return buffs.get(internal);
    }

    @Data
    public class Buff {
        private List<Long> buff;

        public Buff() {
            this.buff = new ArrayList<>();
        }

        public Buff(List<Long> buff) {
            this.buff = buff;
        }

        public void refreshBuff() {
            buff.removeIf(mills -> mills < System.currentTimeMillis());
        }

        public long getLongestExpireStack() {
            return Collections.max(buff);
        }

        //don't use this method to stack buff to player, use AbstractPitBuff.stackBuff();
        public void stackBuffData(long duration) {
            buff.add(System.currentTimeMillis() + duration);
        }

        public int getTier() {
            refreshBuff();
            return buff.size();
        }
    }
}
